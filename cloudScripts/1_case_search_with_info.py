import json
from botocore.vendored import requests
import os
from math import sin, cos, sqrt, atan2, radians
import math
import boto3
from decimal import Decimal
from pprint import pprint

class DecimalEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Decimal):
            return float(obj)
        return super(CustomJsonEncoder, self).default(obj)
        
        
def get_distance(lat1,lon1,lat2,lon2):
    # approximate radius of earth in km
    R = 6373.0
    
    lat1 = radians(lat1)
    lon1 = radians(lon1)
    lat2 = radians(lat2)
    lon2 = radians(lon2)
    
    dlon = lon2 - lon1
    dlat = lat2 - lat1
    
    a = sin(dlat / 2)**2 + cos(lat1) * cos(lat2) * sin(dlon / 2)**2
    c = 2 * atan2(sqrt(a), sqrt(1 - a))
    
    return R * c


def get_weather(lat, lon):
    r =  requests.get(f'http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid='+os.environ['open_weather']).json()
    payload = {}
    payload['weather'] = r['weather']
    payload['temperature'] = int(float(r['main']['temp']) - 273.15)
    return payload
    

def evaluate_tags(request, city):
    tags_req = request['tags']
    tags_city = city['tags']
    tags_score = 0
    for tag_name, tag_value in tags_req.items():
        if tags_city[tag_name] >= tag_value:
            tags_score += 5 + tag_value * 5
        else:
            distance = tag_value - tags_city[tag_name]
            tags_score += 5 + tag_value * 5 - distance * 5
    #print("tag score ", tags_score)
    return tags_score
    
    
def evaluate_costs(request, city):
    cost_req = request['cost']
    cost_city = city['tags']['costs']
    dist = abs(cost_req - cost_city)
    similarity = (2 - dist) * 25
    #print("cost score", similarity)
    return similarity
    
    
def evaluate_distance(request, city):
    #y different names
    dist = get_distance(request['gps_coords'][0]['lat'],request['gps_coords'][0]['lon'],city['coordinates'][0]['lat'],city['coordinates'][0]['lon'])
    cost = request['cost']
    if cost == 2:
        score = 20 - int(math.log(dist))
    elif cost == 1:
        score = 20 - 2*int(math.log(dist))
    else:
        score = 20
    return score
    
    
def evaluate_weather(request, city):
    weather = get_weather(city['coordinates'][0]['lat'],city['coordinates'][0]['lon']) 
    #pprint(weather)
    score = weather['weather'][0]['id'] * 5/100 + (int(weather['temperature'] / 5) + 1 if weather['temperature']< 24 else 9 - int(weather['temperature'] / 5))*2
    #score = 800 *5/100 + (int(30 / 5) + 1 if 30 < 24 else 9 - int(30 / 5))*2 
    return score
    
    
def get_user_city_score(request, city):
    if request['flag_lastminute'] == "True":
        score = evaluate_tags(request,city) + evaluate_costs(request,city) + evaluate_distance(request, city) + evaluate_weather(request, city) 
        norm_score = float(score)/3
    else:
        score = evaluate_tags(request,city) + evaluate_costs(request,city) + evaluate_distance(request, city)
        norm_score = float(score)/2.5
    return norm_score
    

def rank(request, db):
    response_body = {}
    lst = []
    OldMax = 0
    OldMin = 100
    for city in db:
        record = {}
        #record['name'] = city['name']
        record['id'] = city['ID']
        #record['tags'] = city['tags']
        #record['user_tags'] = request['tags']
        record['tag_score'] = evaluate_tags(request,city)
        if request['flag_lastminute'] == "True":
            record['weather'] = get_weather(city['coordinates'][0]['lat'],city['coordinates'][0]['lon']) 
            record['weather_score'] = evaluate_weather(request, city)
        record['distance'] = get_distance(request['gps_coords'][0]['lat'],request['gps_coords'][0]['lon'],city['coordinates'][0]['lat'],city['coordinates'][0]['lon'])
        record['distance_score'] = evaluate_distance(request, city)
        #record['cost'] = request['cost']
        record['cost_score'] = evaluate_costs(request, city)
        record['score'] = get_user_city_score(request, city)
        OldMin = OldMin if OldMin < record['score'] else record['score']
        OldMax = OldMax if OldMax > record['score'] else record['score'] 
        lst.append(record)
    OldRange = (OldMax - OldMin)  
    NewRange = 100
    for elem in lst:
        print(elem['score'])
        elem['score'] = (((elem['score'] - OldMin) * NewRange) / OldRange)
        print(elem['score'])
    from operator import itemgetter
    ranked_list = sorted(lst, key=itemgetter('score'), reverse=True) 
    #pprint(ranked_list[:10])
    return ranked_list[:10]
    
    
def lambda_handler(event, context):
    
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table('test_suggestrip')
    response = table.scan()
    response = json.loads(json.dumps(response['Items'], cls=DecimalEncoder))
    #pprint(response)
    request_body = json.loads(event['body'])
    

    response_body = rank(request_body, response)
    return {
        'statusCode': 200,
        'body': json.dumps(response_body),
        'headers': {'Content-Type': 'application/json'}
    }


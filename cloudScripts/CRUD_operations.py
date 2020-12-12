import json
import boto3
from decimal import Decimal
from botocore.vendored import requests
import os
from random import randint

class DecimalEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Decimal):
            return float(obj)
        return super(CustomJsonEncoder, self).default(obj)
        
        
def lambda_handler(event, context):
    # TODO implement
    # Get the service resource.
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table('test_suggestrip')
    
    request_body =json.loads(event['body'])
    
    if(request_body['msg_id'] == "CREATE"):
        response = create_city(request_body['city'],table)
    elif(request_body['msg_id'] == "DELETE"):
        response = delete_city(request_body['city_id'],table)
    else:
        response = update_city(request_body['city'],table)
    
    

    return {
        'statusCode': 200,
        'body': json.dumps(response, cls=DecimalEncoder)
    }


def delete_city(city_id,table):
    response = table.delete_item(Key={'ID': city_id})
    return response
    
    
def update_city(city, table):
    response = table.update_item(
        Key={'ID': city['ID']},
        UpdateExpression="set description=:d, tags.costs=:c, tags.sports=:s, tags.nature=:n, tags.culture=:cu, tags.infrastructure=:i, tags.food=:f, tags.night_life=:nl",
        ExpressionAttributeValues={
            ':d': city['description'],
            ':c': Decimal(city['tags']['costs']),
            ':s': Decimal(city['tags']['sports']),
            ':n': Decimal(city['tags']['nature']),
            ':cu': Decimal(city['tags']['culture']),
            ':i': Decimal(city['tags']['infrastructure']),
            ':f': Decimal(city['tags']['food']),
            ':nl': Decimal(city['tags']['night_life'])
        },
        ReturnValues="UPDATED_NEW"
    )
    return response
    

def create_city(city, table):

    city['ID'] = str(randint(101,10000))
    city['brief_description'] = "no brief"
    city['img_url'] = get_image(city['name'])
    city['coordinates']=get_coord(city['name'])
    city['country'] = get_country(city)
    city = json.loads(json.dumps(city), parse_float=Decimal)
    response = table.put_item(Item=city)
    
    return response
    
        
def get_country(c):  
    lat = c['coordinates'][0]['lat']
    lon = c['coordinates'][0]['lon']
    country = requests.get(f'http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid='+os.environ['open_weather']).json()['sys']['country']
    return country

def get_coord(city_name):
    try:
        # Use wikipedia api to get article
        response = requests.get(
            'https://en.wikipedia.org/w/api.php',
            params={
            'action': 'query',
            'format': 'json',
            'titles': city_name,
            'prop': 'coordinates',
            'explaintext': True,
            'exlimit': 'max',
        }
        ).json()
    except:
        return [
           {
              "lon":2.352222,
              "globe":"earth",
              "lat":48.856613,
              "primary":""
           }
        ]
    for idx, val in response["query"]["pages"].items():
        return val["coordinates"]
    
    
def get_image(city_name):
    try:
    # Use wikipedia api to get article
        response = requests.get(
            'https://en.wikipedia.org/w/api.php',
            params={
            'action': 'query',
            'format': 'json',
            'titles': city_name,
            'prop': 'info|pageimages',
            'pithumbsize': '800'
            }
        ).json()
    except:
        return "previews.123rf.com/images/venimo/venimo1703/venimo170300016/73225884-vector-illustration-in-trendy-line-flat-style-404-page-design-template-and-web-site-under-constructi.jpg"
    for idx, val in response["query"]["pages"].items():
        return val["thumbnail"]["source"].strip("https://")

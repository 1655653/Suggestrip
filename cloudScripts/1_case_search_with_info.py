import json
from botocore.vendored import requests
import os
from math import sin, cos, sqrt, atan2, radians



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
    return requests.get(f'http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid='+os.environ['open_weather']).json()


def lambda_handler(event, context):
    
    
    request_body = json.loads(event['body'])
    '''request_body = {
"msg_id":"01, codice del messaggio, esempio 01 per messaggio caso uso 1",
"gps_coords":[
    {
        "lat":48.856613,
        "lon":2.352222,
        "primary":"",
        "globe":"earth"
    }
],
"flag_lastminute":"True",
"tags":[
    {
        "night_life":"3, an int from 1 to 5 ",
        "nature":"3, an in from 1 to 5",
        "sports":"3, an int from 1 to 5",
        "food":"3, an int from 1 to 5",
        "culture":"3, an int from 1 to 5",
        "infrastructure":"3, an int from 1 to 5",
        "costs":"2, an int from 1 to 3"
    }
],
"target":{
    "travel_type":"identifier of family, couple, friends",
    "age_type":"ages"
}
}'''
    user_position = request_body['gps_coords'][0]
    is_last_minute = request_body['flag_lastminute']
    
    response_body = {}
    response_body['ranking_list'] = []
    
    
    # TODO implement
    ranking_object = {
        "brief_description": "The Eternal City has always been coveted. These days, the bounty is an immersive step back in time.",
        "coordinates": [
            {
                "globe": "earth",
                "lat": 41.88333333,
                "lon": 12.5,
                "primary": ""
            }
        ],
        "description": "Few cities serve up the ability to walk the history of the Western world like Roma. Heck, Palatine Hill alone invites you into two millennia of Western civilization, if you’ve got an hour. Mix a safe, accessible modern city and its thousands of portals back in time and it’s easy to see how Rome almost cracked the Best Cities Top 10 again this year. Declarations of love for the city have multiplied with the channels of self-expression, of course, and the city’s #7 ranking in our extensive Place category has directly fueled its #5 Promotion ranking, including the second-most number of TripAdvisor reviews on the planet and very frequent Google searches. The curiosity about the Eternal City will only increase as Rome reopens carefully to visitors, who, after gorging on six months of local ’Grams featuring empty summer streets and iconic sites with nary a tourist umbrella to clutter the shot, are keen to time their return before the crowds come back. As of late summer 2020, most sights were reopened, as were most bars and restaurants. It’s not exactly la dolce vita quite yet, but Roma has persevered once again.",
        "name": "Rome",
        "tags": {
            "costs": 3,
            "culture": 4,
            "food": 3,
            "infrastructure": 4,
            "nature": 1,
            "night_life": 1,
            "sports": 3
        },
        "country": "IT"
    }
    
    #RETRIEVE COVID INFO
    covid_info = requests.get('http://corona-api.com/countries/'+ranking_object['country']).json()
    our_covid_info_city = {}
    our_covid_info_city['city'] = ranking_object['name']
    our_covid_info_city['date'] = covid_info['data']['updated_at']
    our_covid_info_city['today'] = covid_info['data']['today']
    our_covid_info_city['latest_data'] = covid_info['data']['latest_data']
    
    ranking_object['covid_info'] = our_covid_info_city
    
    #RETRIEVE Wheather info
    if is_last_minute =="True":
        weather = get_weather(ranking_object["coordinates"][0]['lat'],ranking_object["coordinates"][0]['lon'])
        ranking_object['weather'] = weather
    
    
    
    # Retrieve distance in Kmeters
    ranking_object['distance'] = get_distance(user_position['lat'],user_position['lon'], ranking_object['coordinates'][0]['lat'],ranking_object['coordinates'][0]['lon'])
    
    
    
    response_body['ranking_list'].append(ranking_object)
    
    return {
        'statusCode': 200,
        'body': json.dumps(response_body),
        'headers': {'Content-Type': 'application/json'}
    }

import json
import boto3
from botocore.vendored import requests
from decimal import Decimal
import os

class DecimalEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Decimal):
            return float(obj)
        return super(CustomJsonEncoder, self).default(obj)

def get_weather(lat, lon):
    r =  requests.get(f'http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid='+os.environ['open_weather']).json()
    payload = {}
    payload['weather'] = r['weather']
    payload['temperature'] = int(float(r['main']['temp']) - 273.15)
    return payload
    

def get_covid_info(c):
    covid_info = requests.get('http://corona-api.com/countries/'+c['country']).json()
    our_covid_info_city = {}
    our_covid_info_city['city'] = c['name']
    our_covid_info_city['date'] = covid_info['data']['updated_at']
    our_covid_info_city['today'] = covid_info['data']['today']
    our_covid_info_city['latest_data'] = covid_info['data']['latest_data']
    
    return our_covid_info_city

       
def lambda_handler(event, context):
    # TODO implement
    # Get the service resource.
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table('test_suggestrip')
    city_id = event["queryStringParameters"]['ID']
    response = table.get_item(Key={'ID': city_id})
    item = response['Item']
    item['covid_info'] = get_covid_info(item)
    item['weather'] = get_weather(item['coordinates'][0]['lat'],item['coordinates'][0]['lon'])
    #print(item)

    
    return {
        'statusCode': 200,
        'body': json.dumps(item, cls=DecimalEncoder)
    }

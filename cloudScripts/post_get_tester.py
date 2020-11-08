import requests
from pprint import pprint 
import json

with open("pwds.json",encoding="utf-8") as fp:
    pwds = json.load(fp)


def post_tester():
    url = pwds['1_case_post_url']

    body_example = {
    "msg_id":"01, codice del messaggio, esempio 01 per messaggio caso uso 1",
    "gps_coords":[
        {
            "lat":48.856613,
            "lon":2.352222,
            "primary":"",
            "globe":"earth"
        }
    ],
    "flag_lastminute":"True, bool value for last minute",
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
    }
    headers = {'Content-type': 'application/json'}
    x = requests.post(url, headers= headers, json=body_example)
    pprint(x.json())

def get_tester():
    body_get_city = {"city_id" : "1"}

    url_get = "https://darpioxoei.execute-api.us-east-1.amazonaws.com/default/dynamo_getter"
    url_get_all="https://krjkmpvu2c.execute-api.us-east-1.amazonaws.com/default/dynamo_getter_all"
    #CHANGE KEY con quello che ti serve
    '''
    "x-api-key" : pwds['get_all_api_key'] se ti serve la get_all
    "x-api-key" : pwds['get_city_api_key'] se ti serve la get_city
    '''
    headers = {'Content-type': 'application/json', "x-api-key" : pwds['get_all_api_key']}    
    
    y = requests.get(url, headers = headers)
    pprint(y.json())


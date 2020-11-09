import requests
from pprint import pprint
import json
import time


with open("pwds.json", encoding="utf-8") as fp:
    pwds = json.load(fp)


def post_tester():
    url = pwds['1_case_post_url']

    body_example = {
        "msg_id": "01, codice del messaggio, esempio 01 per messaggio caso uso 1",
        "gps_coords": [
            {
                "lat": 48.86613,
                "lon": 2.352222,
                "primary": "",
                "globe": "earth"
            }
        ],
        "flag_lastminute": "True",
        "tags":
        {
            "night_life": 5,
            "nature": 5,
            "sports": 5,
            "food": 5,
            "culture": 5,
            "infrastructure": 5
        },
        "cost": 1,
        "target": {
            "travel_type": "identifier of family, couple, friends",
            "age_type": "ages"
        }
    }

    headers = {'Content-type': 'application/json'}
    x = requests.post(url, headers=headers, json=body_example)
    pprint(x.json())


def get_tester():
    body_get_city = {"city_id": "1"}

    url_get = "https://darpioxoei.execute-api.us-east-1.amazonaws.com/default/dynamo_getter"
    url_get_all = "https://krjkmpvu2c.execute-api.us-east-1.amazonaws.com/default/dynamo_getter_all"
    #CHANGE KEY con quello che ti serve
    '''
    "x-api-key" : pwds['get_all_api_key'] se ti serve la get_all
    "x-api-key" : pwds['get_city_api_key'] se ti serve la get_city
    '''
    headers = {'Content-type': 'application/json',
               "x-api-key": pwds['get_all_api_key']}

    y = requests.get(url, headers=headers)
    pprint(y.json())

t = time.time()
post_tester()
print(t-time.time())
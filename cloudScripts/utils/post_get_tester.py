import requests
from pprint import pprint
import json
import time


with open("pwds.json", encoding="utf-8") as fp:
    pwds = json.load(fp)


def case_1_tester():
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

def get_all_tester():
    headers = {'Content-type': 'application/json'}
    url = pwds['get_all_api_url']
    y = requests.get(url, headers=headers)
    pprint(y.json())

def get_city_tester(city_id):
    headers = {'Content-type': 'application/json'}
    url = pwds['get_city_api_url'] + str(city_id)
    y = requests.get(url, headers=headers)
    pprint(y.json())




t = time.time()

#case_1_tester()
#get_city_tester(1)
get_all_tester()

print(time.time()-t)

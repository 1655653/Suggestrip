import requests
from pprint import pprint
import json
import time


with open("pwds.json", encoding="utf-8") as fp:
    pwds = json.load(fp)


def get_covid():
    with open("covid.json", encoding="utf-8") as fp:
        covid_js = json.load(fp)
    useful = covid_js['data']['timeline'][:7]
    case_per_million = covid_js['data']["latest_data"]['calculated']['cases_per_million_population']
    avg_recovered = sum([e['new_recovered'] for e in useful])/7
    avg_deaths = sum([e['new_deaths'] for e in useful])/7
    avg_confirmed = sum([e['new_confirmed'] for e in useful])/7
    print(avg_confirmed, avg_recovered, avg_deaths, case_per_million)


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


def crud_tester(crud_op):
    url = pwds['crud']
    if crud_op == "UPDATE":
        body_example = {
            "msg_id": crud_op,
            "city_id": "2596",
            "city": {
                "brief_description": "Just as terrorism subsided, the Notre-Dame inferno struck Parisian resolve anew. But the City of Light builds on resiliently.",
                "img_url": "upload.wikimedia.org/wikipedia/commons/thumb/4/4b/La_Tour_Eiffel_vue_de_la_Tour_Saint-Jacques%2C_Paris_ao%C3%BBt_2014_%282%29.jpg/800px-La_Tour_Eiffel_vue_de_la_Tour_Saint-Jacques%2C_Paris_ao%C3%BBt_2014_%282%29.jpg",
                "description": "updated",
                "ID": "2596",
                "country": "FR",
                "name": "Rome",
                "coordinates": [
                    {
                        "lon": 2.352222,
                        "globe": "earth",
                        "lat": 48.856613,
                        "primary": ""
                    }
                ],
                "tags": {
                    "costs": 5.0,
                    "night_life": 5.0,
                    "sports": 5.0,
                    "nature": 5.0,
                    "culture": 5.0,
                    "infrastructure": 5.0,
                    "food": 5.0
                }
            }
        }
    elif crud_op == "CREATE":
        body_example = {
            "msg_id": crud_op,
            "city":{
                "description": "On April 15, 2019, as the Notre-Dame cathedral went up in flames, people in Paris gathered in the streets to pray. Joining them was just about everyone else across the globe with access to a screen. This, of course, was not an isolated catastrophe for the French capital over the past few years. But despite multiple terrorist attacks and the destruction of its iconic cathedral\u2014seemingly insurmountable disaster and hardship\u2014Paris prevails and comes back brighter. Amazingly, despite the tragic fire and a year of the Yellow Vest protests that deterred tourists from visiting the city, Paris matched its 2018 in 2019, with 35.4 million visitors, according to Statista. The City of Light ranks #3 in our Product category, with the second-best Airport Connectivity in the world, a #5 ranking for Museums (the city has more than a hundred) and #7 for Attractions. Leading up to the 2024 Summer Olympics, infrastructure investment has sped up and the city continues to build hotels at a dizzying rate. Two dozen opened in 2019 and the same amount were scheduled to debut in 2020, including the first Bulgari Hotel at 30 Avenue George V. There have been plenty of pandemic-related delays. As the saying goes, Paris is always a good idea. And now that swimming in the waters of the Seine is a thing at Bassin de la Villette public pools, even the locals who notoriously leave town in summer are sticking around. City Hall is promising to build five new outdoor swimming pools in time for the Olympics. So even though Paris is now #26 globally for Parks & Outdoors in 2020, it\u2019s a category ranking that should improve.",
                "name": "Paris",
                "tags": {
                    "costs": 1.0,
                    "night_life": 3.0,
                    "sports": 5.0,
                    "nature": 5.0,
                    "culture": 4.0,
                    "infrastructure": 1.0,
                    "food": 4.0
                }
            }
        }
    else:
        body_example = {
        "msg_id": crud_op,
        "city_id": "2596",
        "city": {}
    }

    

    headers = {'Content-type': 'application/json'}
    x = requests.post(url, headers=headers, json=body_example)
    pprint(x.json())


t = time.time()

#case_1_tester()
#get_city_tester(1)
#get_all_tester()

#crud_tester("CREATE")
print(time.time()-t)


def get_coord(city_name):
    coord_rome = [
           {
              "lon":41.88333333,
              "globe":"earth",
              "lat":12.5,
              "primary":""
           }
        ]
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
        return coord_rome
    try:
        for idx, val in response["query"]["pages"].items():
            return val["coordinates"]
    except:
        return coord_rome

print(get_coord("diocane"))
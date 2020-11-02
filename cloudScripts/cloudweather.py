import requests
import pprint
import json


with open("pwds.json",encoding="utf-8") as fp:
    pwds = json.load(fp)

def get_weather(lat, lon):
    return requests.get(f'http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid='+pwds['open_wheater']).json()


def get_country():  
    with open("results2.json",encoding="utf-8") as fp:
        cities = json.load(fp)
    for c in cities:
        lat = c['coordinates'][0]['lat']
        lon = c['coordinates'][0]['lon']
        country = get_weather(lat,lon)['sys']['country']
        c['country'] = country

    with open('luigi2.json', 'w', encoding="utf-8") as fp:
        json.dump(cities, fp, indent=4, ensure_ascii=False)

#pprint.pprint(json.loads(r.content.decode()))

def get_covid_info():
    with open("results3.json",encoding="utf-8") as fp:
        cities = json.load(fp)
        our_covid_info = []
        for c in cities:
            covid_info = requests.get('http://corona-api.com/countries/'+c['country']).json()
            our_covid_info_city = {}
            our_covid_info_city['city'] = c['name']
            our_covid_info_city['date'] = covid_info['data']['updated_at']
            our_covid_info_city['today'] = covid_info['data']['today']
            our_covid_info_city['latest_data'] = covid_info['data']['latest_data']
            our_covid_info.append(our_covid_info_city)

        with open('covid.json', 'w', encoding="utf-8") as fp:
            json.dump(our_covid_info, fp, indent=4, ensure_ascii=False)
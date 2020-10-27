import requests
import json
from bs4 import BeautifulSoup as bs


url = "http://127.0.0.1:5500/best_cities.html"
page = requests.get(url)
soup = bs(page.text, "html.parser")

js = []
for city in soup.find_all(class_="cities-sp-story-1 js-story"):
    city_name = ''.join([i for i in city.find('h1').text if not i.isdigit()]).replace('. ','').replace(' ','_')
    city_brief_desc = city.find(class_="cities-sp-intro-1").text.replace("�",'\'')
    city_desc = city.find('p').text.replace("�",'\'')
    city_brief_desc = city_brief_desc.replace('\n','').replace('\t','')
    c = {}
    c['name'] = city_name
    c['description'] = city_desc
    c['brief_description'] = city_brief_desc
    js.append(c)

with open('luigi.json', 'w', encoding="utf-8") as fp:
    json.dump(js, fp, indent=4, ensure_ascii=False)

    
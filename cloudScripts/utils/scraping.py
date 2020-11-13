import requests
import json
from bs4 import BeautifulSoup as bs

def first_scraping():
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

def image_scraping():
    with open("results3.json",encoding="utf-8") as fp:
        res = json.load(fp)

    for elem in res:
        url = 'https://en.wikipedia.org/wiki/'+elem['name']
        # get contents from url
        content = requests.get(url).content
        # get soup
        soup = bs(content,'lxml')
        link_ok = False # choose lxml parser
        for td in soup.findAll('td'): #find all tables
            if link_ok == False:
                image_tags = td.findAll('a') #find all anchors
                trov = False
                for image_tag in image_tags:
                    if trov == False:
                        if image_tag.get('href') != None and "File" in image_tag.get('href'):
                            trov = True
                            img_url = ("https://en.wikipedia.org"+image_tag.get('href'))
                            
                            content = requests.get(img_url).content
                            # get soup
                            soup_img = bs(content,'lxml') # choose lxml parser
                            imgs = soup_img.findAll(class_="fullImageLink")
                            for im in imgs:
                                aaa = im.find('img')
                                elem['img_url']=aaa.get('src').replace('//','')
                                link_ok = True
                                print(elem['img_url'])
                                break
                    else:
                        break
            else:
                break
            

    with open('luigi.json', 'w', encoding="utf-8") as fp:
        json.dump(res, fp, indent=4, ensure_ascii=False)
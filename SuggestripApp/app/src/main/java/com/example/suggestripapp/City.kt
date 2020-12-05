package com.example.suggestripapp

import java.io.Serializable

data class City(
    var brief_description: String,
    var img_url: String,
    var description: String,
    var ID: Int,
    var country: String, //NAZIONE ES,US..
    var name: String,  //nome citta
    var coordinates: ArrayList<Coordinates>?,
    var tags: Tags?,
    var covid_info: Covid?,
    var weather: Weather?
):Serializable{
    override fun toString(): String {
        return "City(brief_description='$brief_description', img_url='$img_url', description='$description', ID=$ID, country='$country', name='$name', coordinates=$coordinates, tags=$tags)"
    }
}

data class Coordinates(
    var lon: Float,
    var globe: String,
    var lat: Float,
    var primary: String
):Serializable{
    override fun toString(): String {
        return "Coordinates(lon=$lon, globe='$globe', lat=$lat, primary='$primary')"
    }
}

data class Tags(
    var costs: Double,
    var night_life: Double,
    var sports: Double,
    var nature: Double,
    var culture: Double,
    var infrastructure: Double,
    var food: Double
):Serializable{
    override fun toString(): String {
        return "costs=$costs, night_life=$night_life, sports=$sports, nature=$nature, culture=$culture, infrastructure=$infrastructure, food = $food"
    }
}


data class Covid(
        var case_per_million: Float,
        var avg_confirmed: Float,
        var avg_deaths: Float,
        var avg_recovered: Float
):Serializable{
    override fun toString(): String {
        return "cases_one_million=$cases_one_million, cases_weekly_average=$cases_weekly_average, deaths_weekly_average=$deaths_weekly_average, healed_weekly_average=$healed_weekly_average"
    }
}



data class Weather(
        var id: Double,
        var main: String,
        var description: String,
        var icon: String,
        var temperature: Double
):Serializable{
    override fun toString(): String {
        return "Weater(id=$id, main='$main', description='$description', icon='$icon', temperature='$temperature)"
    }
}

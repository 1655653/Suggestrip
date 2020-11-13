package com.example.suggestripapp


data class City(
    var brief_description: String,
    var img_url: String,
    var description: String,
    var ID: Int,
    var country: String, //NAZIONE ES,US..
    var name: String,  //nome citta
    var coordinates: ArrayList<Coordinates>,
    var tags: Tags
){
    override fun toString(): String {
        return "City(brief_description='$brief_description', img_url='$img_url', description='$description', ID=$ID, country='$country', name='$name', coordinates=$coordinates, tags=$tags)"
    }
}
data class Coordinates(
    var lon: Float,
    var globe: String,
    var lat: Float,
    var primary: String
){
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
    var infrastructure: Double
){
    override fun toString(): String {
        return "Tags(costs=$costs, night_life=$night_life, sports=$sports, nature=$nature, culture=$culture, infrastructure=$infrastructure)"
    }
}

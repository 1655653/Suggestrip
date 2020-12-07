package com.example.suggestripapp
import java.io.Serializable

data class RankedCity(
    var img_url: String,
    var ID: Int,
    var country: String, //NAZIONE ES,US..
    var name: String, //nome citta
    var tag_score: Float,
    var distance: Float,
    var distance_score: Float,
    var cost_score: Float,
    var score: Float,
):Serializable{
    override fun toString(): String {
        return "City(tag_score='$tag_score', img_url='$img_url', distance='$distance', ID=$ID, country='$country', name='$name', distance_score=$distance_score, cost_score=$cost_score, score=$score)"
    }
}


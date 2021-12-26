package com.corp.luqman.movielisting.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Video (
    @Json(name = "id")
    var id : String? = "",
    @Json(name = "iso_639_1")
    var language : String? = "",
    @Json(name = "iso_3166_1")
    var country : String? = "",
    @Json(name = "key")
    var key : String? = "",
    @Json(name = "name")
    var name : String? = "",
    @Json(name = "site")
    var site : String? = "",
    @Json(name = "size")
    var size : Int? = 0,
    @Json(name = "type")
    var type : String? = ""

)
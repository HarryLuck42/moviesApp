package com.corp.luqman.movielisting.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Review(
    @Json(name = "author")
    var author : String? = "",
    @Json(name = "content")
    var content : String? = "",
    @Json(name = "id")
    var id : String? = "",
    @Json(name = "url")
    var url : String? = ""
)
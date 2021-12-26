package com.corp.luqman.movielisting.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LanguageValue(
    @Json(name = "iso_639_1")
    var code : String? = "",
    @Json(name = "name")
    var name : String? = ""
)
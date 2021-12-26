package com.corp.luqman.movielisting.data.remote.response

import com.corp.luqman.movielisting.data.models.GenreData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreResponse (
    @Json(name = "genres")
    var genres: MutableList<GenreData>?
)
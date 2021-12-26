package com.corp.luqman.movielisting.data.remote.response

import com.corp.luqman.movielisting.data.models.Video
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoResponse (
    @Json(name = "id")
    var id : Long? = 0,
    @Json(name = "results")
    var results: MutableList<Video>?
)
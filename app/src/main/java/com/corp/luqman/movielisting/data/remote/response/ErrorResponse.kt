package com.corp.luqman.movielisting.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ErrorResponse (
    @Json(name = "error")
    var error: String = "",
    @Json(name = "error_description")
    var errorDesription: String = "",
    @Json(name = "message")
    var message: String = ""
)
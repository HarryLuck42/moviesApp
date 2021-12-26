package com.corp.luqman.movielisting.data.remote.response

import com.corp.luqman.movielisting.data.models.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailResponse (
    @Json(name = "id")
    var id : Long? = 0,
    @Json(name = "adult")
    var adult : Boolean? = false,
    @Json(name = "backdrop_path")
    var backdropPath : String? = "",
    @Json(name = "budget")
    var budget : Int? = 0,
    @Json(name = "genres")
    var genres: MutableList<GenreData>?,
    @Json(name = "homepage")
    var homepage : String? = "",
    @Json(name = "original_language")
    var originalLanguage : String? = "",
    @Json(name = "original_title")
    var originalTitle : String? = "",
    @Json(name = "overview")
    var overview : String? = "",
    @Json(name = "popularity")
    var popularity : Double? = 0.0,
    @Json(name = "poster_path")
    var posterPath : String? = "",
    @Json(name = "production_companies")
    var productionCompanies: MutableList<Companies>?,
    @Json(name = "production_countries")
    var productionCountries: MutableList<Countries>?,
    @Json(name = "release_date")
    var releaseDate : String? = "",
    @Json(name = "revenue")
    var revenue : Int? = 0,
    @Json(name = "runtime")
    var runtime : Int? = 0,
    @Json(name = "spoken_languages")
    var spokenLanguages: MutableList<LanguageValue>?,
    @Json(name = "status")
    var status : String? = "",
    @Json(name = "tagline")
    var tagline : String? = "",
    @Json(name = "title")
    var title : String? = "",
    @Json(name = "video")
    var video : Boolean? = false,
    @Json(name = "vote_average")
    var voteAverage : Double? = 0.0,
    @Json(name = "vote_count")
    var voteCount : Int? = 0
)
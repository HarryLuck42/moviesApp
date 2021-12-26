package com.corp.luqman.movielisting.data.models

import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie (
    @ColumnInfo(name = "id")
    @Json(name = "id")
    var id : Long = 0,
    @ColumnInfo(name = "popularity")
    @Json(name = "popularity")
    var popularity : Double? = 0.0,
    @ColumnInfo(name = "vote_count")
    @Json(name = "vote_count")
    var voteCount : Int? = 0,
    @ColumnInfo(name = "video")
    @Json(name = "video")
    var video : Boolean? = false,
    @ColumnInfo(name = "poster_path")
    @Json(name = "poster_path")
    var posterPath : String? = "",
    @ColumnInfo(name = "adult")
    @Json(name = "adult")
    var adult : Boolean? = false,
    @ColumnInfo(name = "backdrop_path")
    @Json(name = "backdrop_path")
    var backdropPath : String? = "",
    @ColumnInfo(name = "original_language")
    @Json(name = "original_language")
    var originalLanguage : String? = "",
    @ColumnInfo(name = "original_title")
    @Json(name = "original_title")
    var originalTitle : String? = "",
    @ColumnInfo(name = "genre_ids")
    @Json(name = "genre_ids")
    var genreIds : MutableList<Int>? = null,
    @ColumnInfo(name = "title")
    @Json(name = "title")
    var title : String? = "",
    @ColumnInfo(name = "vote_average")
    @Json(name = "vote_average")
    var voteAverage : Double? = 0.0,
    @ColumnInfo(name = "overview")
    @Json(name = "overview")
    var overview : String? = "",
    @ColumnInfo(name = "release_date")
    @Json(name = "release_date")
    var releaseDate : String? = ""
)
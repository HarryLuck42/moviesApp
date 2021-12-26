package com.corp.luqman.movielisting.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "data_genre")
@JsonClass(generateAdapter = true)
data class GenreData (
    @PrimaryKey
    @ColumnInfo(name = "id")
    @Json(name = "id")
    var id : Long = 0,
    @ColumnInfo(name = "name")
    @Json(name = "name")
    var desc : String? = ""
)
package com.corp.luqman.movielisting.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert

import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.corp.luqman.movielisting.data.models.GenreData


@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllGenre(genres: MutableList<GenreData>?)

    @Query("SELECT * FROM data_genre")
    fun getAll(): LiveData<MutableList<GenreData>>?
}
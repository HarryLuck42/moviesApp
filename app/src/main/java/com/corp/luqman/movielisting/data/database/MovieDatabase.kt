package com.corp.luqman.movielisting.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Ignore
import androidx.room.Room
import androidx.room.RoomDatabase
import com.corp.luqman.movielisting.data.models.GenreData


@Database(entities = [GenreData::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase(){

    abstract val movieDao: MovieDao

    companion object{
        @Volatile private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movies_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
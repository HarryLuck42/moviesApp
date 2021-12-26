package com.corp.luqman.movielisting.data.remote

import com.corp.luqman.movielisting.data.remote.response.*
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/{movie}")
    fun getListMovies(
        @Path("movie") movie : String,
        @Query("api_key")api_key: String,
        @Query("page")page:String,
        @Query("language")language:String,
        @Query("with_genres") genres:String?,
        @Query("release_date.gte") start_date:String?,
        @Query("release_date.lte") last_date:String?,
        @Query("vote_average.gte") min_vote:String?,
        @Query("vote_average.lte") max_vote:String?):Deferred<MoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetail(
        @Path("id") id : String,
        @Query("api_key")api_key: String,
        @Query("language")language:String):Deferred<MovieDetailResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReview(
        @Path("id") id : String,
        @Query("api_key")api_key: String,
        @Query("page")page:String,
        @Query("language")language:String):Deferred<ReviewMovieResponse>

    @GET("genre/movie/list")
    fun getDataGenre(
        @Query("api_key")api_key: String,
        @Query("language")language:String):Deferred<GenreResponse>

    @GET("movie/{id}/videos")
    fun getDataVideo(
        @Path("id") id : String,
        @Query("api_key")api_key: String,
        @Query("language")language:String):Deferred<VideoResponse>

    @GET("search/movie")
    fun searchMovieByKeyWord(
        @Query("api_key")api_key: String,
        @Query("page")page:String,
        @Query("language")language:String,
        @Query("query")keyword:String,
        @Query("include_adult")include_adult:String):Deferred<MoviesResponse>
}
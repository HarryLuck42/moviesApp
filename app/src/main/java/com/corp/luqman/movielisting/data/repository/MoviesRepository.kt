package com.corp.luqman.movielisting.data.repository

import androidx.lifecycle.LiveData
import com.corp.luqman.movielisting.data.database.MovieDatabase
import com.corp.luqman.movielisting.data.models.GenreData
import com.corp.luqman.movielisting.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepository(private val apiService: ApiService, private val movieDatabase: MovieDatabase) {

    fun getMoviesData(movie:String, api_key: String, page:String, language:String,
            genres: String?, start_date: String?, last_date: String?, min_vote: String?, max_vote: String?)
            = apiService.getListMovies(movie, api_key, page, language,
                genres, start_date, last_date, min_vote, max_vote)
    fun searchMovie(api_key: String, page:String, language:String, keyword: String, adult_content: String)
            = apiService.searchMovieByKeyWord(api_key, page, language, keyword, adult_content)

    fun getDetailMovieData(id:String, api_key: String, language:String) = apiService.getMovieDetail(id, api_key, language)

    fun getReviewsMovie(id:String, api_key: String, page:String, language:String) = apiService.getMovieReview(id, api_key, page, language)

    fun getDataGenres(api_key: String, language:String) = apiService.getDataGenre(api_key, language)

    fun getVideos(id:String, api_key: String, language:String) = apiService.getDataVideo(id, api_key, language)

    suspend fun replaceAllGenre(values: MutableList<GenreData>){
        withContext(Dispatchers.IO){
            movieDatabase.movieDao.insertAllGenre(values)
        }
    }

    fun getGenreData(): LiveData<MutableList<GenreData>>?{
        val results = movieDatabase.movieDao.getAll()
        return results
    }
}
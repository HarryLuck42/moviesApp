package com.corp.luqman.movielisting.ui.fragment

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.corp.luqman.movielisting.data.models.Movie
import com.corp.luqman.movielisting.data.remote.response.MoviesResponse
import com.corp.luqman.movielisting.data.repository.MoviesRepository
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesUpcomingViewModel @ViewModelInject constructor(val moviesRepository: MoviesRepository,
                                                           @ApplicationContext val context: Context) : ViewModel() {
    var isLoading = false

    fun stopLoading() {
        isLoading = false
    }

    fun startLoading(){
        isLoading = true
    }
    init {
        stopLoading()
    }

    private val scope = CoroutineScope((GlobalScope.coroutineContext))
    val movieState = MutableLiveData<UiState<MoviesResponse>>()
    val searchMovieState = MutableLiveData<UiState<MoviesResponse>>()

    val listMovie : MutableList<Movie> = mutableListOf()

    fun clearListMovie(){
        listMovie.clear()
    }

    val getAllTvSeries = moviesRepository.getGenreData()

    fun getListData(api_key: String, page:String, language:String,
                    genres: String?, start_date: String?, last_date: String?, min_vote: String?, max_vote: String?){
        movieState.value = UiState.Loading()

        scope.launch {
            try {
                val result = moviesRepository.getMoviesData(
                    Const.UPCOMING_PATH, api_key, page, language,
                    genres, start_date, last_date, min_vote, max_vote).await()

                listMovie.addAll(result.results!!)


                movieState.postValue(UiState.Success(result))


            }catch (e: Exception){
                movieState.postValue(UiState.Error(e))
            }
        }
    }

    fun searchMovieByKeyword(api_key: String, page:String, language:String,
                             keyword: String?){
        searchMovieState.value = UiState.Loading()

        scope.launch {
            try {
                val result = moviesRepository.searchMovie(api_key, page, language,
                    keyword!!, "false").await()

                listMovie.addAll(result.results!!)


                searchMovieState.postValue(UiState.Success(result))


            }catch (e: Exception){
                searchMovieState.postValue(UiState.Error(e))
            }
        }
    }



    private val _navigateToDetail = MutableLiveData<Long>()
    val navigateToDetail
        get() = _navigateToDetail

    fun onMovieClicked(id: Long) {
        _navigateToDetail.value = id
    }

    fun onMovieNavigated(){
        _navigateToDetail.value = null
    }

    private val _genres = MutableLiveData<String?>()
    val genres
        get() = _genres

    fun inputGenres(value: String) {
        _genres.value = value
    }

    fun resetGenre(){
        _genres.value = null
    }

    private val _fisrt_date = MutableLiveData<String?>()
    val fisrt_date
        get() = _fisrt_date

    fun inputFirstDate(value: String) {
        _fisrt_date.value = value
    }

    fun resetFirstDate(){
        _fisrt_date.value = null
    }

    private val _last_date = MutableLiveData<String?>()
    val last_date
        get() = _last_date

    fun inputLastDate(value: String) {
        _last_date.value = value
    }

    fun resetLastDate(){
        _last_date.value = null
    }

    private val _min_vote = MutableLiveData<String?>()
    val min_vote
        get() = _min_vote

    fun inputMinVote(value: String) {
        _min_vote.value = value
    }

    fun resetMinVote(){
        _min_vote.value = null
    }

    private val _max_vote = MutableLiveData<String?>()
    val max_vote
        get() = _max_vote

    fun inputMaxVote(value: String) {
        _max_vote.value = value
    }

    fun resetMaxVote(){
        _max_vote.value = null
    }

    private val _keywordValue = MutableLiveData<String?>()
    val keywordValue
        get() = _keywordValue

    fun inputKeyword(value: String) {
        _keywordValue.value = value
    }

    private val _isSearchState = MutableLiveData<Boolean>()
    val isSearchState
        get() = _isSearchState

    fun inputSearchState(value: Boolean) {
        _isSearchState.value = value
    }
}
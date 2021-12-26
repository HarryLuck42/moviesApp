package com.corp.luqman.movielisting.ui.fragment

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.corp.luqman.movielisting.data.models.Video
import com.corp.luqman.movielisting.data.remote.response.MovieDetailResponse
import com.corp.luqman.movielisting.data.remote.response.VideoResponse
import com.corp.luqman.movielisting.data.repository.MoviesRepository
import com.corp.luqman.movielisting.utils.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.lang.Exception

class DetailMovieViewModel @ViewModelInject constructor(val moviesRepository: MoviesRepository,
                                                        @ApplicationContext val context: Context) : ViewModel() {


    private val scope = CoroutineScope((GlobalScope.coroutineContext))
    val detailState = MutableLiveData<UiState<MovieDetailResponse>>()
    val videoState = MutableLiveData<UiState<VideoResponse>>()

    private val _videos = MutableLiveData<MutableList<Video>>()
    val videos : LiveData<MutableList<Video>>
        get() = _videos

    private val _movieDetail = MutableLiveData<MovieDetailResponse>()
    val movieDetail : LiveData<MovieDetailResponse>
        get() = _movieDetail

    fun getDetailMovie(id:String, api_key: String, language:String){
        detailState.value = UiState.Loading()

        scope.launch {
            try {
                val result = moviesRepository.getDetailMovieData(id, api_key, language).await()
                _movieDetail.postValue(result)
                detailState.postValue(UiState.Success(result))

            }catch (e: Exception){
                detailState.postValue(UiState.Error(e))
            }
        }
    }

    fun getDataVideo(id:String, api_key: String, language:String){
        videoState.value = UiState.Loading()

        scope.launch {
            try {
                val result = moviesRepository.getVideos(id, api_key, language).await()
                _videos.postValue(result.results)
                videoState.postValue(UiState.Success(result))

            }catch (e: Exception){
                videoState.postValue(UiState.Error(e))
            }
        }
    }

    private val _navigateToReview = MutableLiveData<Long>()
    val navigateToReview
        get() = _navigateToReview

    fun onMovieClicked(id: Long) {
        _navigateToReview.value = id
    }

    fun onMovieNavigated(){
        _navigateToReview.value = null
    }
}
package com.corp.luqman.movielisting.ui.fragment

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.corp.luqman.movielisting.data.models.Review
import com.corp.luqman.movielisting.data.remote.response.ReviewMovieResponse
import com.corp.luqman.movielisting.data.repository.MoviesRepository
import com.corp.luqman.movielisting.utils.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.lang.Exception

class ReviewMovieViewModel @ViewModelInject constructor(val moviesRepository: MoviesRepository,
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
    val reviewState = MutableLiveData<UiState<ReviewMovieResponse>>()

    val listReview : MutableList<Review> = mutableListOf()

    fun getListReview(id: String, api_key: String, page:String, language:String){
        reviewState.value = UiState.Loading()

        scope.launch {
            try {
                val result = moviesRepository.getReviewsMovie(id, api_key, page, language).await()

                listReview.addAll(result.results!!)


                reviewState.postValue(UiState.Success(result))


            }catch (e: Exception){
                reviewState.postValue(UiState.Error(e))
            }
        }
    }


}
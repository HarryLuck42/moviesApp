package com.corp.luqman.movielisting.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.getCustomView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.Helpers
import com.corp.luqman.movielisting.utils.NetworkHelper
import com.corp.luqman.movielisting.utils.UiState
import com.corp.luqman.movielisting.utils.custom.CustomProgressDialog
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.detail_movie_fragment.view.*
import kotlinx.android.synthetic.main.watch_trailer_dialog.view.*
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private lateinit var progressDialog : CustomProgressDialog

    private lateinit var watchYoutubeDialog : MaterialDialog

    private val viewModel: DetailMovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.detail_movie_fragment, container, false)
        v.layout_detail_movie.visibility = View.GONE
        setHasOptionsMenu(true)
        progressDialog = CustomProgressDialog(v.context, getString(R.string.loading))
        initObserver(v)
        initObserverVideos(v)
        val arguments = DetailMovieFragmentArgs.fromBundle(requireArguments())
        viewModel.getDetailMovie(arguments.idMovie.toString(), Const.apikey, Const.language)
        viewModel.videos.observe(viewLifecycleOwner, Observer {
            val videoNameList : MutableList<String> = mutableListOf()
            for(value in it){
                if(value.type!!.contains("Trailer") && value.site!!.contains("YouTube")){
                    videoNameList.add(value.name!!)
                }
            }
            v.btn_trailer.setOnClickListener { view ->
                val dialogChooseVideo = AlertDialog.Builder(v.context)
                val dataAdapter = ArrayAdapter(v.context, R.layout.support_simple_spinner_dropdown_item, videoNameList)
                dialogChooseVideo.setAdapter(dataAdapter, { dialog, which ->
                    settingDialogWatchTrailer(v, it[which].key!!)
                })
                val dialog = dialogChooseVideo.create()
                dialog.show()
            }
        })
        viewModel.movieDetail.observe(viewLifecycleOwner, Observer {
            it.let {
                v.layout_detail_movie.visibility = View.VISIBLE
                v.tv_title_header.text = it.title
                v.tv_rate.text = it.voteAverage.toString()
                v.tv_release.text = it.releaseDate.toString()
                var genres =""
                for(index in it.genres!!.indices){
                    if (index == 0){
                        genres += it.genres!!.get(index).desc
                    }else{
                        genres += ", " + it.genres!!.get(index).desc
                    }
                }
                v.tv_genre.text = genres
                v.tv_vote.text = it.voteCount.toString()
                v.tv_popularity.text = it.popularity.toString()
                var productions = ""
                for(index in it.productionCompanies!!.indices){
                    if (index == 0){
                        productions += it.productionCompanies!!.get(index).name
                    }else{
                        productions += ", " + it.productionCompanies!!.get(index).name
                    }
                }
                v.tv_production.text = productions
                var countries = ""
                for(index in it.productionCountries!!.indices){
                    if (index == 0){
                        countries += it.productionCountries!!.get(index).name
                    }else{
                        countries += ", " + it.productionCountries!!.get(index).name
                    }
                }
                v.tv_country.text = countries
                val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
                val budget: String = format.format(it.budget)
                v.tv_budget.text = budget
                v.tv_overview.text = it.overview
                v.tv_homepage.text = it.homepage
                Glide.with(v.context).load(Const.imageUrlbase + it.posterPath).apply(
                    RequestOptions().placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken)).into(v.iv_movie_detail)
                val idmovie = it.id
                v.btn_review.setOnClickListener {
                    viewModel.onMovieClicked(idmovie!!)
                }

            }

        })

        viewModel.navigateToReview.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(DetailMovieFragmentDirections.actionDetailMovieFragmentToReviewMovieFragment(it))
                viewModel.onMovieNavigated()
            }
        })
        return v
    }



    private fun settingDialogWatchTrailer(v : View, videoId : String){
        watchYoutubeDialog = Helpers.customViewDialog(v.context, R.layout.watch_trailer_dialog, true)
        val customview = watchYoutubeDialog.getCustomView()
        lifecycle.addObserver(customview.play_trailer)
        customview.play_trailer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })
        watchYoutubeDialog.show {
            cancelable(true)
        }
    }
    private fun initObserver(v : View){
        viewModel.detailState.observe(viewLifecycleOwner, Observer {
            when (it){
                is UiState.Loading -> {
                    progressDialog.show()
                }
                is UiState.Success -> {
                    progressDialog.dismiss()
                    viewModel.getDataVideo(it.data.id.toString(), Const.apikey, Const.language)
                }
                is UiState.Error -> {
                    progressDialog.dismiss()
                    val message = NetworkHelper().getErrorMessage(it.throwable)
                    Helpers.showGeneralOkDialog(
                        v.context,
                        getString(R.string.perhatian),
                        message
                    )
                }
            }
        })
    }

    private fun initObserverVideos(v : View){
        viewModel.videoState.observe(viewLifecycleOwner, Observer {
            when (it){
                is UiState.Loading -> {
                    progressDialog.show()
                }
                is UiState.Success -> {
                    progressDialog.dismiss()
                }
                is UiState.Error -> {
                    progressDialog.dismiss()
                    val message = NetworkHelper().getErrorMessage(it.throwable)
                    Helpers.showGeneralOkDialog(
                        v.context,
                        getString(R.string.perhatian),
                        message
                    )
                }
            }
        })
    }





}
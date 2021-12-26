package com.corp.luqman.movielisting.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.getCustomView
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.ui.adapter.CheckGenreAdapter
import com.corp.luqman.movielisting.ui.adapter.GenreListener
import com.corp.luqman.movielisting.ui.adapter.MovieAdapter
import com.corp.luqman.movielisting.ui.adapter.MovieListener
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.Helpers
import com.corp.luqman.movielisting.utils.NetworkHelper
import com.corp.luqman.movielisting.utils.UiState
import com.corp.luqman.movielisting.utils.custom.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.movie_now_playing_fragment.view.*
import kotlinx.android.synthetic.main.search_movie_dialog.view.*
import kotlinx.android.synthetic.main.sort_movie_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MoviesNowPlayingFragment : Fragment() {
    private lateinit var progressDialog : CustomProgressDialog

    private lateinit var sortMovieDialog : MaterialDialog

    private lateinit var searchMovieDialog : MaterialDialog

    private val nowViewModel: MoviesNowPlayingViewModel by viewModels()

    private lateinit var adapter : MovieAdapter

    private var paging = 1

    private var formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var selectedStartDate : Calendar? = null
    companion object {
        fun newInstance() = MoviesNowPlayingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.movie_now_playing_fragment, container, false)
        setHasOptionsMenu(true)
        progressDialog = CustomProgressDialog(v.context, getString(R.string.loading))
        val layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)
        v.rv_movies_now.layoutManager = layoutManager
        v.rv_movies_now.setHasFixedSize(true)
        v.rv_movies_now.isFocusable = false
        v.rv_movies_now.visibility = View.VISIBLE
        v.tv_not_found_movie_now.visibility = View.GONE
        v.iv_not_found_now.visibility = View.GONE
        adapter = MovieAdapter(
            this.requireContext(),
            nowViewModel.listMovie, MovieListener{ id ->
                nowViewModel.onMovieClicked(id)
            }
        )
        adapter.notifyDataSetChanged()
        v.rv_movies_now.adapter = adapter
        nowViewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(MoviesNowPlayingFragmentDirections.actionMovieNowPlayingFragmentToDetailMovieFragment(it))
                nowViewModel.onMovieNavigated()
            }
        })
        initObserver(v)
        initObserverSearchMovie(v)
        onScrollAdapter(v)
        nowViewModel.inputSearchState(false)
        nowViewModel.getListData(Const.apikey, paging.toString(), Const.language,
            nowViewModel.genres.value, nowViewModel.fisrt_date.value, nowViewModel.last_date.value,
            nowViewModel.min_vote.value, nowViewModel.max_vote.value)
        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_movies->{
                settingDialogSort(this.requireView())
            }
            R.id.search_movies->{
                settingDialogSearch(this.requireView())
            }
            R.id.list_default->{
                RefreshListMovie()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun RefreshListMovie() {
        paging = 1
        nowViewModel.clearListMovie()
        nowViewModel.resetGenre()
        nowViewModel.resetFirstDate()
        nowViewModel.resetLastDate()
        nowViewModel.resetMinVote()
        nowViewModel.resetMaxVote()
        nowViewModel.inputSearchState(false)
        nowViewModel.getListData(
            Const.apikey, paging.toString(), Const.language,
            nowViewModel.genres.value, nowViewModel.fisrt_date.value, nowViewModel.last_date.value,
            nowViewModel.min_vote.value, nowViewModel.max_vote.value
        )
    }

    private fun initObserver(v : View){
        nowViewModel.movieState.observe(viewLifecycleOwner, Observer {
            when (it){
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if(paging > it.data.totalPages!!){
                        nowViewModel.stopLoading()
                    }else{
                        nowViewModel.startLoading()
                    }
                    if(tempPagging==1){
                        if (it.data.results!!.isEmpty()){
                            v.rv_movies_now.visibility = View.GONE
                            v.iv_not_found_now.visibility = View.VISIBLE
                            v.tv_not_found_movie_now.visibility = View.VISIBLE
                        }else{
                            v.rv_movies_now.visibility = View.VISIBLE
                            v.iv_not_found_now.visibility = View.GONE
                            v.tv_not_found_movie_now.visibility = View.GONE
                        }
                    }



                    adapter.notifyDataSetChanged()

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

    private fun initObserverSearchMovie(v : View){
        nowViewModel.searchMovieState.observe(viewLifecycleOwner, Observer {
            when (it){
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if(paging > it.data.totalPages!!){
                        nowViewModel.stopLoading()
                    }else{
                        nowViewModel.startLoading()
                    }
                    if(tempPagging==1){
                        if (it.data.results!!.isEmpty()){
                            v.rv_movies_now.visibility = View.GONE
                            v.iv_not_found_now.visibility = View.VISIBLE
                            v.tv_not_found_movie_now.visibility = View.VISIBLE
                        }else{
                            v.rv_movies_now.visibility = View.VISIBLE
                            v.iv_not_found_now.visibility = View.GONE
                            v.tv_not_found_movie_now.visibility = View.GONE
                        }
                    }

                    adapter.notifyDataSetChanged()

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



    private fun onScrollAdapter(v : View){

        v.rv_movies_now.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){

                    val lastVisibleItem = (v.rv_movies_now.layoutManager!! as GridLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount = (v.rv_movies_now.layoutManager!! as GridLayoutManager).itemCount
                    if (nowViewModel.isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        nowViewModel.stopLoading()
                        if(nowViewModel.isSearchState.value!!){
                            nowViewModel.searchMovieByKeyword(Const.apikey, paging.toString(), Const.language,
                                nowViewModel.keywordValue.value)
                        }else{
                            nowViewModel.getListData(Const.apikey, paging.toString(), Const.language,
                                nowViewModel.genres.value, nowViewModel.fisrt_date.value, nowViewModel.last_date.value,
                                nowViewModel.min_vote.value, nowViewModel.max_vote.value)
                        }
                        progressDialog.show()
                    }

                }
            }
        })
    }

    private fun settingDialogSearch(v : View){
        searchMovieDialog = Helpers.customViewDialog(v.context, R.layout.search_movie_dialog, true)
        val viewCustom = searchMovieDialog.getCustomView()
        var message = ""
        viewCustom.et_keyword_search.requestFocus()
        viewCustom.btn_search.setOnClickListener {
            val keyword = viewCustom.et_keyword_search.text.toString()
            if(keyword.isEmpty()){
                message += "Please input keyword search !"
            }else{
                nowViewModel.inputKeyword(keyword)
            }
            if(message.isEmpty()){
                paging = 1
                nowViewModel.clearListMovie()
                nowViewModel.inputSearchState(true)
                nowViewModel.searchMovieByKeyword(Const.apikey, paging.toString(), Const.language,
                    nowViewModel.keywordValue.value)
                searchMovieDialog.dismiss()
            }else{
                Helpers.showGeneralOkDialog(v.context, "Warning", message)
            }
        }

        searchMovieDialog.show {
            cancelable(true)
        }
    }

    private fun settingDialogSort(v : View){
        sortMovieDialog = Helpers.customViewDialog(v.context, R.layout.sort_movie_dialog, false)
        val customview = sortMovieDialog.getCustomView()
        customview.et_start_date.setOnClickListener {
            showDateDialog(customview, true)
        }
        customview.et_end_date.setOnClickListener {
            showDateDialog(customview, false)
        }
        var minVote = 0
        var maxVote = 10

        customview.seek_max.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                maxVote = progress
                val result = "$minVote - $maxVote"
                customview.tv_range_vote.text = result
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        customview.seek_min.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                minVote = progress
                val result = "$minVote - $maxVote"
                customview.tv_range_vote.text = result
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        val layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)
        customview.rv_list_genre.layoutManager = layoutManager
        customview.rv_list_genre.setHasFixedSize(true)
        customview.rv_list_genre.isFocusable = false
        val listGenreRest : MutableList<String> = mutableListOf()
        nowViewModel.getAllTvSeries!!.observe(viewLifecycleOwner, Observer {

            val adapterGenre = CheckGenreAdapter(
                this.requireContext(),
                it, GenreListener{genre , isAdd ->
                    if(isAdd){
                        listGenreRest.add(genre.id.toString())
                    }else{
                        listGenreRest.remove(genre.id.toString())
                    }
//                    viewModel.onMovieClicked(id)
                }
            )
            adapterGenre.notifyDataSetChanged()
            customview.rv_list_genre.adapter = adapterGenre
        })

        customview.btn_cancel_sort.setOnClickListener {
            sortMovieDialog.dismiss()
        }

        customview.btn_submit_sort.setOnClickListener {
            var message = ""
            var genresResult = ""
            val startDate = customview.et_start_date.text.toString()
            val endDate = customview.et_end_date.text.toString()
            val minVoteResult = minVote.toString()
            val maxVoteResult = maxVote.toString()
            for(index in listGenreRest.indices){
                if(index == 0){
                    genresResult += listGenreRest.get(index)
                }else{
                    genresResult += "," + listGenreRest.get(index)
                }
            }
            if(genresResult.isEmpty()){
                nowViewModel.resetGenre()
            }else{
                nowViewModel.inputGenres(genresResult)
            }

            if(startDate.isEmpty() && endDate.isEmpty()){
                nowViewModel.resetFirstDate()
                nowViewModel.resetLastDate()
            }else if(!startDate.isEmpty() && endDate.isEmpty()){
                message += "Please input Last Date !"
            }else{
                nowViewModel.inputFirstDate(startDate)
                nowViewModel.inputLastDate(endDate)
            }

            if(minVote >= maxVote){
                if(message.isEmpty()){
                    message += "\nMin Vote value cannot more than Max Vote value !"
                }else{
                    message += "Min Vote value cannot more than Max Vote value !"
                }

            }else{
                nowViewModel.inputMinVote(minVoteResult)
                nowViewModel.inputMaxVote(maxVoteResult)
            }

            if (message.isEmpty()){
                paging = 1
                nowViewModel.clearListMovie()
                nowViewModel.inputSearchState(false)
                nowViewModel.getListData(Const.apikey, paging.toString(), Const.language,
                    nowViewModel.genres.value, nowViewModel.fisrt_date.value, nowViewModel.last_date.value,
                    nowViewModel.min_vote.value, nowViewModel.max_vote.value)
                sortMovieDialog.dismiss()
            }else{
                Helpers.showGeneralOkDialog(v.context, "Warning", message)
            }


        }

        sortMovieDialog.show {
            cancelable(false)
        }




    }


    private fun showDateDialog(v : View, isStartDate: Boolean) {
        val calendar = Calendar.getInstance()

        if(isStartDate){
            val datePickerDialog = DatePickerDialog(v.context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                selectedStartDate = Calendar.getInstance()
                selectedStartDate?.set(year,month,dayOfMonth,0,0)
                val valueDate = formatDate.format(selectedStartDate?.time!!)
                v.et_start_date.setText(valueDate)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }else{
            if(selectedStartDate != null){
                val datePickerDialog = DatePickerDialog(v.context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year,month,dayOfMonth,23,59)
                    val valueDate = formatDate.format(selectedDate.time)
                    v.et_end_date.setText(valueDate)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                datePickerDialog.show()
                datePickerDialog.datePicker.minDate = selectedStartDate!!.timeInMillis
            }else{
                Helpers.showGeneralOkDialog(
                    v.context,
                    getString(R.string.perhatian),
                    "Please Selected Start Date"
                )
            }

        }


    }



}
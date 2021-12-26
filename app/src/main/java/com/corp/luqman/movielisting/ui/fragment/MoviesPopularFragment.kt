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
import kotlinx.android.synthetic.main.movies_fragment.view.*
import kotlinx.android.synthetic.main.search_movie_dialog.view.*
import kotlinx.android.synthetic.main.sort_movie_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MoviesPopularFragment : Fragment() {

    private lateinit var progressDialog : CustomProgressDialog

    private lateinit var sortMovieDialog : MaterialDialog

    private lateinit var searchMovieDialog : MaterialDialog

    private val popularPopularViewModel: MoviesPopularViewModel by viewModels()

    private lateinit var adapter : MovieAdapter

    private var paging = 1

    private var formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var selectedStartDate : Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.movies_fragment, container, false)
        setHasOptionsMenu(true)
        progressDialog = CustomProgressDialog(v.context, getString(R.string.loading))
        val layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)
        v.rv_movies.layoutManager = layoutManager
        v.rv_movies.setHasFixedSize(true)
        v.rv_movies.isFocusable = false
        v.rv_movies.visibility = View.VISIBLE
        v.tv_not_found_movie.visibility = View.GONE
        v.iv_not_found.visibility = View.GONE
        adapter = MovieAdapter(
            this.requireContext(),
            popularPopularViewModel.listMovie, MovieListener{ id ->
                popularPopularViewModel.onMovieClicked(id)
            }
        )
        adapter.notifyDataSetChanged()
        v.rv_movies.adapter = adapter
        popularPopularViewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(MoviesPopularFragmentDirections.actionMoviesFragmentToDetailMovieFragment(it))
                popularPopularViewModel.onMovieNavigated()
            }
        })
        initGenreObserver(v)
        initObserver(v)
        initObserverSearchMovie(v)
        onScrollAdapter(v)
        popularPopularViewModel.saveGenreMasterData(Const.apikey, Const.language)
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
        popularPopularViewModel.clearListMovie()
        popularPopularViewModel.resetGenre()
        popularPopularViewModel.resetFirstDate()
        popularPopularViewModel.resetLastDate()
        popularPopularViewModel.resetMinVote()
        popularPopularViewModel.resetMaxVote()
        popularPopularViewModel.inputSearchState(false)
        popularPopularViewModel.getListData(
            Const.apikey, paging.toString(), Const.language,
            popularPopularViewModel.genres.value, popularPopularViewModel.fisrt_date.value, popularPopularViewModel.last_date.value,
            popularPopularViewModel.min_vote.value, popularPopularViewModel.max_vote.value
        )
    }

    private fun initObserver(v : View){
        popularPopularViewModel.movieState.observe(viewLifecycleOwner, Observer {
            when (it){
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if(paging > it.data.totalPages!!){
                        popularPopularViewModel.stopLoading()
                    }else{
                        popularPopularViewModel.startLoading()
                    }
                    if(tempPagging==1){
                        if (it.data.results!!.isEmpty()){
                            v.rv_movies.visibility = View.GONE
                            v.iv_not_found.visibility = View.VISIBLE
                            v.tv_not_found_movie.visibility = View.VISIBLE
                        }else{
                            v.rv_movies.visibility = View.VISIBLE
                            v.iv_not_found.visibility = View.GONE
                            v.tv_not_found_movie.visibility = View.GONE
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
        popularPopularViewModel.searchMovieState.observe(viewLifecycleOwner, Observer {
            when (it){
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if(paging > it.data.totalPages!!){
                        popularPopularViewModel.stopLoading()
                    }else{
                        popularPopularViewModel.startLoading()
                    }
                    if(tempPagging==1){
                        if (it.data.results!!.isEmpty()){
                            v.rv_movies.visibility = View.GONE
                            v.iv_not_found.visibility = View.VISIBLE
                            v.tv_not_found_movie.visibility = View.VISIBLE
                        }else{
                            v.rv_movies.visibility = View.VISIBLE
                            v.iv_not_found.visibility = View.GONE
                            v.tv_not_found_movie.visibility = View.GONE
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

    private fun initGenreObserver(v : View){
        popularPopularViewModel.genreState.observe(viewLifecycleOwner, Observer {
            when (it){
                is UiState.Loading -> {
                    progressDialog.show()
                }
                is UiState.Success -> {
                    progressDialog.dismiss()
                    popularPopularViewModel.inputSearchState(false)
                    popularPopularViewModel.getListData(Const.apikey, paging.toString(), Const.language,
                        null, null, null, null, null)
                    popularPopularViewModel.genreState.postValue(UiState.Stop())

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

        v.rv_movies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){

                    val lastVisibleItem = (v.rv_movies.layoutManager!! as GridLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount = (v.rv_movies.layoutManager!! as GridLayoutManager).itemCount
                    if (popularPopularViewModel.isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        popularPopularViewModel.stopLoading()
                        if(popularPopularViewModel.isSearchState.value!!){
                            popularPopularViewModel.searchMovieByKeyword(Const.apikey, paging.toString(), Const.language,
                            popularPopularViewModel.keywordValue.value)
                        }else{
                            popularPopularViewModel.getListData(Const.apikey, paging.toString(), Const.language,
                                popularPopularViewModel.genres.value, popularPopularViewModel.fisrt_date.value, popularPopularViewModel.last_date.value,
                                popularPopularViewModel.min_vote.value, popularPopularViewModel.max_vote.value)
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
                popularPopularViewModel.inputKeyword(keyword)
            }
            if(message.isEmpty()){
                paging = 1
                popularPopularViewModel.clearListMovie()
                popularPopularViewModel.inputSearchState(true)
                popularPopularViewModel.searchMovieByKeyword(Const.apikey, paging.toString(), Const.language,
                    popularPopularViewModel.keywordValue.value)
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
        popularPopularViewModel.getAllTvSeries!!.observe(viewLifecycleOwner, Observer {

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
                popularPopularViewModel.resetGenre()
            }else{
                popularPopularViewModel.inputGenres(genresResult)
            }

            if(startDate.isEmpty() && endDate.isEmpty()){
                popularPopularViewModel.resetFirstDate()
                popularPopularViewModel.resetLastDate()
            }else if(!startDate.isEmpty() && endDate.isEmpty()){
                message += "Please input Last Date !"
            }else{
                popularPopularViewModel.inputFirstDate(startDate)
                popularPopularViewModel.inputLastDate(endDate)
            }

            if(minVote >= maxVote){
                if(message.isEmpty()){
                    message += "\nMin Vote value cannot more than Max Vote value !"
                }else{
                    message += "Min Vote value cannot more than Max Vote value !"
                }

            }else{
                popularPopularViewModel.inputMinVote(minVoteResult)
                popularPopularViewModel.inputMaxVote(maxVoteResult)
            }

            if (message.isEmpty()){
                paging = 1
                popularPopularViewModel.clearListMovie()
                popularPopularViewModel.inputSearchState(false)
                popularPopularViewModel.getListData(Const.apikey, paging.toString(), Const.language,
                    popularPopularViewModel.genres.value, popularPopularViewModel.fisrt_date.value, popularPopularViewModel.last_date.value,
                    popularPopularViewModel.min_vote.value, popularPopularViewModel.max_vote.value)
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
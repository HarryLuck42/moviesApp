package com.corp.luqman.movielisting.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.ui.adapter.ReviewAdapter
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.Helpers
import com.corp.luqman.movielisting.utils.NetworkHelper
import com.corp.luqman.movielisting.utils.UiState
import com.corp.luqman.movielisting.utils.custom.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.review_movie_fragment.view.*

@AndroidEntryPoint
class ReviewMovieFragment : Fragment() {

    private lateinit var progressDialog : CustomProgressDialog

    private val viewModel: ReviewMovieViewModel by viewModels()

    private var paging = 1

    private lateinit var adapter : ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.review_movie_fragment, container, false)
        progressDialog = CustomProgressDialog(v.context, getString(R.string.loading))
        setHasOptionsMenu(true)
        val layoutManager = LinearLayoutManager(this.requireContext(), GridLayoutManager.VERTICAL, false)
        v.rv_reviews.layoutManager = layoutManager
        v.rv_reviews.setHasFixedSize(true)
        v.rv_reviews.isFocusable = false
        v.rv_reviews.visibility = View.VISIBLE
        v.tv_not_found_review.visibility = View.GONE
        v.iv_not_found_review.visibility = View.GONE
        adapter = ReviewAdapter(
            this.requireContext(), viewModel.listReview)
        adapter.notifyDataSetChanged()
        v.rv_reviews.adapter = adapter
        val arguments = ReviewMovieFragmentArgs.fromBundle(requireArguments())
        viewModel.getListReview(arguments.idReview.toString(), Const.apikey, paging.toString(), Const.language)
        initObserver(v)
        onScrollAdapter(v, arguments.idReview.toString())
        return v
    }

    private fun initObserver(v : View){
        viewModel.reviewState.observe(viewLifecycleOwner, Observer {
            when (it){
                is UiState.Loading -> {

                    progressDialog.show()
                }
                is UiState.Success -> {
                    val tempPagging = paging
                    paging++
                    if(paging > it.data.totalPages!!){
                        viewModel.stopLoading()
                    }else{
                        viewModel.startLoading()
                    }
                    if(tempPagging==1){
                        if (it.data.results!!.isEmpty()){
                            v.rv_reviews.visibility = View.GONE
                            v.iv_not_found_review.visibility = View.VISIBLE
                            v.tv_not_found_review.visibility = View.VISIBLE
                        }else{
                            v.rv_reviews.visibility = View.VISIBLE
                            v.tv_not_found_review.visibility = View.GONE
                            v.iv_not_found_review.visibility = View.GONE
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

    private fun onScrollAdapter(v : View, id: String){
        v.rv_reviews.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){

                    val lastVisibleItem = (v.rv_reviews.layoutManager!! as LinearLayoutManager).findLastVisibleItemPosition()
                    val totalItemCount = (v.rv_reviews.layoutManager!! as LinearLayoutManager).itemCount
                    if (viewModel.isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        viewModel.stopLoading()
                        viewModel.getListReview(id, Const.apikey, paging.toString(), Const.language)
                        progressDialog.show()
                    }

                }
            }
        })
    }

}
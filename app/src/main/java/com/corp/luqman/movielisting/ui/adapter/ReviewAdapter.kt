package com.corp.luqman.movielisting.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.data.models.Review
import kotlinx.android.synthetic.main.item_review_movie.view.*

class ReviewAdapter(val context: Context, val listReview: MutableList<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun setData(review: Review){
            itemView.name_reviewer.text = review.author
            itemView.desc_review.text = review.content
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_review_movie, parent, false)

                return ViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return listReview.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieSelected = listReview[position]
        holder.setData(movieSelected)
    }

}

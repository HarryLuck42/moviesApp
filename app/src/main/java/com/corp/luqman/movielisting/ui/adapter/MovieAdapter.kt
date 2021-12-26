package com.corp.luqman.movielisting.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.data.models.Movie
import com.corp.luqman.movielisting.utils.Const
import com.corp.luqman.movielisting.utils.Helpers
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(val context: Context, val listMovie: MutableList<Movie>, val clickListener: MovieListener) : RecyclerView.Adapter<MovieAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun setData(movie: Movie, clickListener: MovieListener){
            Glide.with(itemView.context).load(Const.imageUrlbase + movie.posterPath)
                .apply(RequestOptions().placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken))
                .into(itemView.iv_movie)
            itemView.title_movie.text = movie.title
            itemView.tv_date_release_movie.text = movie.releaseDate
            itemView.btn_detail_movie.setOnClickListener {
                clickListener.onClick(movie)
            }
            itemView.btn_sinopsis.setOnClickListener {
                Helpers.showGeneralOkDialog(
                    itemView.context,
                    itemView.context.getString(R.string.sinopsis),
                    movie.overview!!
                )
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_movie, parent, false)

                return ViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieSelected = listMovie[position]
        holder.setData(movieSelected, clickListener)
    }
}

class MovieListener(val clickListener: (movieId: Long) -> Unit) {
    fun onClick(movie : Movie) = clickListener(movie.id)
}
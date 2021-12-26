package com.corp.luqman.movielisting.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.data.models.GenreData
import kotlinx.android.synthetic.main.item_genre.view.*

class CheckGenreAdapter (val context: Context, val genres: MutableList<GenreData>, val clickListener: GenreListener) : RecyclerView.Adapter<CheckGenreAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun setData(genre: GenreData, clickListener: GenreListener){
            itemView.cb_genre.text = genre.desc

            itemView.cb_genre.setOnClickListener {
                if(itemView.cb_genre.isChecked){
                    clickListener.onClick(genre, true)
                }else{
                    clickListener.onClick(genre, false)
                }

            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_genre, parent, false)

                return ViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genreSelected = genres[position]
        holder.setData(genreSelected, clickListener)
    }
}

class GenreListener(val clickListener: (genreValue: GenreData, isCheck: Boolean) -> Unit) {
    fun onClick(genre : GenreData, check: Boolean) = clickListener(genre, check)
}
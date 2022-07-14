package com.example.samplenewsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.samplenewsapp.R
import com.example.samplenewsapp.model.Result
import com.squareup.picasso.Picasso

class NowPlayingMovieAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout,parent,false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val result = differ.currentList.get(position)
        Picasso.get().load("https://image.tmdb.org/t/p/original"+result.posterPath).into(holder.movieImage)
        holder.movieOriginalLanguage.text= "Language \n"+result.originalLanguage
        holder.moviePopularity.text = "Popularity \n"+result.popularity.toString()
        holder.movieTitle.text = result.title
        holder.movieRelease.text = "Release Date \n"+result.releaseDate
        holder.movieDescription.text = result.overview
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val movieTitle = itemView.findViewById<TextView>(R.id.movieTitle)
    val movieRelease = itemView.findViewById<TextView>(R.id.movieRelease)
    val movieOriginalLanguage = itemView.findViewById<TextView>(R.id.movieOriginalLanguage)
    val moviePopularity = itemView.findViewById<TextView>(R.id.moviePopularity)
    val movieDescription = itemView.findViewById<TextView>(R.id.movieDescription)
    val movieImage = itemView.findViewById<ImageView>(R.id.movieImage)

}
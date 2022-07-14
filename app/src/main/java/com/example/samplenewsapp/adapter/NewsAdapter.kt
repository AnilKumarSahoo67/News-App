package com.example.samplenewsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.samplenewsapp.R
import com.example.samplenewsapp.model.Article
import com.example.samplenewsapp.model.NewsResponse
import com.squareup.picasso.Picasso

class NewsAdapter : RecyclerView.Adapter<NewsItemViewHolder>() {

//    inner class  NewsItemViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item,parent,false)
        return NewsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val article = differ.currentList.get(position)
        Picasso.get().load(article.urlToImage).into(holder.poster)
        holder.author.text = article.author
        holder.publicationDate.text = article.publishedAt
        holder.desc.text = article.description
        holder.title.text = article.title
        holder.content.text = article.content
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(article) }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener: ((Article)->Unit)? = null

    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener = listener
    }
}
class NewsItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val author = itemView.findViewById<TextView>(R.id.newsAuthor)
    val title = itemView.findViewById<TextView>(R.id.newsTitle)
    val desc = itemView.findViewById<TextView>(R.id.newsDesc)
    val poster = itemView.findViewById<ImageView>(R.id.newsPoster)
    val publicationDate = itemView.findViewById<TextView>(R.id.newsDate)
    val content = itemView.findViewById<TextView>(R.id.newsContent)
}

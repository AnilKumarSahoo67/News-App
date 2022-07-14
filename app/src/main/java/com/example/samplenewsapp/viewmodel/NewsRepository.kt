package com.example.samplenewsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samplenewsapp.api.ApiCallGenerator
import com.example.samplenewsapp.model.Article
import com.example.samplenewsapp.model.MovieResponse
import com.example.samplenewsapp.model.NewsResponse
import com.example.samplenewsapp.room.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    val mutableLiveData = MutableLiveData<NewsResponse>()
    val liveData: LiveData<NewsResponse>
    get() = mutableLiveData
    val mutableLiveDataMovie= MutableLiveData<MovieResponse>()
    val liveDataMovie:LiveData<MovieResponse>
    get() = mutableLiveDataMovie


    suspend fun getBreakingNews(countryCode: String, pagenum: Int) =
        ApiCallGenerator.api.getBreakingNews(countryCode, pagenum)

    suspend fun getSearchNews(query: String, pagenum: Int) =
        ApiCallGenerator.api.searchForNews(query, pagenum)

    suspend fun upsertArticle(article: Article) = db.getArticleDao().upsert(article)

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun getAllArticle() = db.getArticleDao().getAllArticles()

    suspend fun getNowPlayingMovie(pagenum: Int) =
        ApiCallGenerator.movieApi.getNowPlayingMovie(pagenum)

}
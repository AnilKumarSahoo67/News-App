package com.example.samplenewsapp.viewmodel

import androidx.lifecycle.*
import com.example.samplenewsapp.api.Resource
import com.example.samplenewsapp.model.Article
import com.example.samplenewsapp.model.MovieResponse
import com.example.samplenewsapp.model.NewsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(val repository: NewsRepository) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var moviePageNumber = 1
    var breakingNewsResponse: NewsResponse? = null
    var searchNewsResponse: NewsResponse? = null
    var nowPlayingMovieResponse: MovieResponse? = null
    val mutableLiveDataNowPlaying: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()

    init {
        getBreakingNewsData("in")
    }

    fun getBreakingNewsData(countryCode: String) =
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val newsResponse = repository.getBreakingNews(countryCode, breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(newsResponse))
        }

    fun getSearchNews(query: String) =
        viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val searchNewsResponse = repository.getSearchNews(query, searchNewsPage)
            searchNews.postValue(handleSearchNewsResponse(searchNewsResponse))
        }

    fun saveArticle(article: Article) =
        viewModelScope.launch {
            repository.upsertArticle(article)
        }

    fun deleteArticle(article: Article) =
        viewModelScope.launch {
            repository.deleteArticle(article)
        }

    fun getAllArticle() = repository.getAllArticle()

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Failure(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Failure(response.message())
    }

    fun getNowPlayingMovieApi() =
        viewModelScope.launch {
            mutableLiveDataNowPlaying.postValue(Resource.Loading())
            val movieResponse = repository.getNowPlayingMovie(moviePageNumber)
            mutableLiveDataNowPlaying.postValue(handleNowPlayingMovieResponse(movieResponse))
        }

    private fun handleNowPlayingMovieResponse(movieResponse: Response<MovieResponse>): Resource<MovieResponse> {
        if (movieResponse.isSuccessful) {
            movieResponse.body()?.let { movieResponse ->
                moviePageNumber++
                if (nowPlayingMovieResponse == null){
                    nowPlayingMovieResponse = movieResponse
                }else{
                    val oldResult = nowPlayingMovieResponse?.results
                    val newMovieResult = movieResponse.results
                    if (newMovieResult != null) {
                        oldResult?.addAll(newMovieResult)
                    }
                }
                return Resource.Success(nowPlayingMovieResponse?:movieResponse)
            }
        }
        return Resource.Failure(movieResponse.message())
    }
}
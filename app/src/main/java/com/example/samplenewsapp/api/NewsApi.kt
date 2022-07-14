package com.example.samplenewsapp.api

import com.example.samplenewsapp.model.MovieResponse
import com.example.samplenewsapp.model.NewsResponse
import com.example.samplenewsapp.utils.Constants
import com.example.samplenewsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(@Query("country") countryCode : String = "in",@Query("page") page : Int=1 ,@Query("apiKey") apiKey : String=API_KEY) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(@Query("q") searchQuery:String, @Query("page") page : Int=1, @Query("apiKey") apiKey : String=API_KEY) : Response<NewsResponse>

    @GET("movie/top_rated?api_key=${Constants.API_KEY_MOVIE}")
    suspend fun getNowPlayingMovie(@Query("page") page: Int=1) : Response<MovieResponse>

    @GET("movie/top_rated?api_key=${Constants.API_KEY_MOVIE}")
    suspend fun getNowPlayingMovieByPage(@Query("page") page : Int):Response<MovieResponse>
}
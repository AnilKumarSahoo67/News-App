package com.example.samplenewsapp.api

import com.example.samplenewsapp.utils.Constants.Companion.BASE_URL
import com.example.samplenewsapp.utils.Constants.Companion.BASE_URL_MOVIE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiCallGenerator {

    private val retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient().newBuilder().connectTimeout(360,TimeUnit.SECONDS)
            .readTimeout(360,TimeUnit.SECONDS)
            .writeTimeout(360,TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
    private val retrofitForMovie by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient().newBuilder().connectTimeout(360,TimeUnit.SECONDS)
            .readTimeout(360,TimeUnit.SECONDS)
            .writeTimeout(360,TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL_MOVIE)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
     val api by lazy {
         retrofit.create(NewsApi::class.java)
     }
    val movieApi by lazy {
        retrofitForMovie.create(NewsApi::class.java)
    }
}
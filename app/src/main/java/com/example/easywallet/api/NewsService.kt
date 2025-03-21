package com.example.easywallet.api

import com.example.easywallet.api.model.NewsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    // Get all top headlines
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("language") language: String = "en"
    ): Call<NewsData>

    // Get headlines for a specific category
    @GET("v2/top-headlines")
    fun getArticlesByCategory(
        @Query("apiKey") apiKey: String,
        @Query("language") language: String = "en",
        @Query("category") category: String
    ): Call<NewsData>

    // Get headlines from a specific source (e.g., BBC, CNN)
    @GET("v2/top-headlines")
    fun getArticlesBySource(
        @Query("apiKey") apiKey: String,
        @Query("language") language: String = "en",
        @Query("sources") source: String
    ): Call<NewsData>

    // Search news by keyword
    @GET("v2/everything")
    fun searchNews(
        @Query("apiKey") apiKey: String,
        @Query("language") language: String = "en",
        @Query("q") query: String
    ): Call<NewsData>
}
package com.example.easywallet.api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easywallet.api.model.Article
import com.example.easywallet.api.model.NewsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.easywallet.BuildConfig

class NewsManager : ViewModel() {

    private val _newsResponse = MutableStateFlow<List<Article>>(emptyList())
    val newsResponse: StateFlow<List<Article>> get() = _newsResponse

    val apiKey = BuildConfig.NEWS_API_KEY

    // Fetch top headlines
    fun getTopHeadlines() {
        fetchArticles(category = null, source = null, query = null)
    }

    // Fetch business news
    fun getBusinessNews() {
        fetchArticles(category = "business", source = null, query = null)
    }

    // Fetch technology news
    fun getTechnologyNews() {
        fetchArticles(category = "technology", source = null, query = null)
    }

    // Fetch articles by source (e.g., BBC, CNN)
    fun getArticlesBySource(source: String) {
        fetchArticles(category = null, source = source, query = null)
    }

    // Search for news by keyword
    fun searchNews(query: String) {
        fetchArticles(category = null, source = null, query = query)
    }

    // Fetch articles with category, source, or search query (depending on parameters)
    private fun fetchArticles(category: String?, source: String?, query: String?) {
        viewModelScope.launch {
            val call: Call<NewsData> = when {
                !category.isNullOrEmpty() -> {
                    // Fetch by category
                    Api.retrofitService.getArticlesByCategory(apiKey, category = category)
                }
                !source.isNullOrEmpty() -> {
                    // Fetch by source
                    Api.retrofitService.getArticlesBySource(apiKey, source = source)
                }
                !query.isNullOrEmpty() -> {
                    // Search by keyword
                    Api.retrofitService.searchNews(apiKey, query = query)
                }
                else -> {
                    // Fetch all top headlines
                    Api.retrofitService.getTopHeadlines(apiKey)
                }
            }

            // Make the API call asynchronously
            call.enqueue(object : Callback<NewsData> {
                override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                    if (response.isSuccessful) {
                        Log.i("Data", "News articles retrieved")
                        // Update the state with the new articles
                        _newsResponse.value = (response.body()?.articles ?: emptyList()) as List<Article>
                        Log.i("DataStream", _newsResponse.value.toString())
                    }
                }

                override fun onFailure(call: Call<NewsData>, t: Throwable) {
                    // Handle API error (you can log it or show an error message)
                    Log.d("error", "${t.message}")
                }
            })
        }
    }
}

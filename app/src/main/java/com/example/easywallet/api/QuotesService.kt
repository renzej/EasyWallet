package com.example.easywallet.api

import com.example.easywallet.api.model.Quote
import retrofit2.Call
import retrofit2.http.GET

interface QuotesService {
    @GET("today")
    fun getQuote(): Call<List<Quote>>  // ZenQuotes returns a list of quotes
}
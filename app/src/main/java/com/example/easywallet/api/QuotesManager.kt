package com.example.easywallet.api

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.easywallet.api.model.Quote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuotesManager : ViewModel() {

    // Create a MutableStateFlow to hold the Quote
    private val _quoteResponse = MutableStateFlow<Quote?>(null)
    val quoteResponse: StateFlow<Quote?> get() = _quoteResponse

    // Function to fetch a random quote
    fun getRandomQuote() {
        // Make the API call
        val call: Call<List<Quote>> = QuotesApi.retrofitService.getQuote()

        // Enqueue the API call
        call.enqueue(object : Callback<List<Quote>> {
            override fun onResponse(call: Call<List<Quote>>, response: Response<List<Quote>>) {
                if (response.isSuccessful) {
                    // If the response is successful, extract the first quote
                    val quotes = response.body()
                    _quoteResponse.value = quotes?.firstOrNull() // We take the first quote from the list
                    Log.i("QuotesManager", "Quote retrieved: ${quotes?.firstOrNull()?.q}")
                } else {
                    Log.e("QuotesManager", "Failed to retrieve quote: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                // Handle the failure case
                Log.e("QuotesManager", "Error fetching quote: ${t.message}")
            }
        })
    }
}


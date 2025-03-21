package com.example.easywallet.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsData(
    @Json(name = "articles")
    var articles: List<Article?>?,
    @Json(name = "totalResults")
    var totalResults: Int?
)
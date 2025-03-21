package com.example.easywallet.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Article(
    @Json(name = "author")
    var author: String?,
    @Json(name = "content")
    var content: String?,
    @Json(name = "description")
    var description: String?,
    @Json(name = "publishedAt")
    var publishedAt: String?,
    @Json(name = "source")
    var source: Source?,
    @Json(name = "title")
    var title: String?,
    @Json(name = "url")
    var url: String?,
    @Json(name = "urlToImage")
    var urlToImage: String?
)
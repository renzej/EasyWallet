package com.example.easywallet.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Quote(
    @Json(name = "a") var a: String?,
    @Json(name = "q") var q: String?
)
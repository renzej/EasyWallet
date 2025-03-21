package com.example.easywallet.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Source(
    @Json(name = "id")
    var id: String?,
    @Json(name = "name")
    var name: String?
)
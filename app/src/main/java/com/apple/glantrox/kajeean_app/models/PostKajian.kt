package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class PostKajian(
    @SerializedName("address_detail")
    val addressDetail: String,
    @SerializedName("author_id")
    val authorId: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("poster_url")
    val posterUrl: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("livestreaming_link")
    val liveLink: String?
)
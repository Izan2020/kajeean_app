package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class UpdateKajian(
    @SerializedName("address_detail")
    val addressDetail: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("livestreaming_link")
    val livestreamingLink: String,
    @SerializedName("poster_url")
    val posterUrl: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("title")
    val title: String
)
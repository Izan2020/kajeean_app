package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class PostKajianResponseItem(
    @SerializedName("address_detail")
    val addressDetail: String,
    @SerializedName("author_id")
    val authorId: Int,
    @SerializedName("city")
    val city: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("livestreaming_link")
    val livestreamingLink: String?,
    @SerializedName("poster_url")
    val posterUrl: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
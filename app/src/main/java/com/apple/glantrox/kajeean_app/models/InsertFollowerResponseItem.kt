package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class InsertFollowerResponseItem(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("follower_id")
    val followerId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int
)
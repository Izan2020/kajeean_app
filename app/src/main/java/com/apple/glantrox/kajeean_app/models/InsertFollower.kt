package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class InsertFollower(
    @SerializedName("follower_id")
    val followerId: Int,
    @SerializedName("user_id")
    val userId: Int
)
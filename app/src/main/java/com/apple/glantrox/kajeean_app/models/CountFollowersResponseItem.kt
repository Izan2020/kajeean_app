package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class CountFollowersResponseItem(
    @SerializedName("count")
    val count: Int
)
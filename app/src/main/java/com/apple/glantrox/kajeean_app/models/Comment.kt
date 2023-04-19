package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("kajian_id")
    val kajianId: Int,
    @SerializedName("user_id")
    val userId: Int
)
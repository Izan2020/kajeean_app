package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class JoinKajianResponseItem(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("kajian_id")
    val kajianId: Int,
    @SerializedName("user_id")
    val userId: Int
)
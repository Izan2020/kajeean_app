package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class JoinKajian(
    @SerializedName("kajian_id")
    val kajianId: Int?,
    @SerializedName("user_id")
    val userId: Int
)
package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class LoginResponseItem(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("password")
    val password: String
)
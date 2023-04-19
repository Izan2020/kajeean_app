package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName("city")
    val city: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("role")
    val role: String,
)
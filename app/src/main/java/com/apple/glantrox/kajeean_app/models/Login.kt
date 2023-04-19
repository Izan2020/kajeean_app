package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("city")
    val city: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("profile_picture")
    val profilePicture: String,
    @SerializedName("banner_url")
    val bannerPicture: String,
    val id: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("role")
    val role: String
)
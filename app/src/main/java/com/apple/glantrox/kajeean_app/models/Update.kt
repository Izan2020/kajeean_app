package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class Update(
    @SerializedName("city")
    val city: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("profile_picture")
    val profilePicture: String?,
    @SerializedName("about_me")
    val aboutMe: String?
)
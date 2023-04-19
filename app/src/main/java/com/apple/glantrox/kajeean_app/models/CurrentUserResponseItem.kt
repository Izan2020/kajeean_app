package com.apple.glantrox.kajeean_app.models


import com.google.gson.annotations.SerializedName

data class CurrentUserResponseItem(
    @SerializedName("about_me")
    val aboutMe: String?,
    @SerializedName("banner_url")
    val bannerUrl: Any?,
    @SerializedName("city")
    val city: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("profile_picture")
    val profilePicture: Any?,
    @SerializedName("role")
    val role: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
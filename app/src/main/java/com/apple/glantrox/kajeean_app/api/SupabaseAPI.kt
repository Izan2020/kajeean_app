package com.apple.glantrox.kajeean_app.api

import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.models.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SupabaseAPI {



    @POST("users")
    suspend fun registerUser (
        @Body register: Register,
        @Header("apikey") apikey:String = API_KEY,
        @Header("Prefer") prefer: String = "return=representation",
        @Header("Content-Type") contenttype: String = "application/json"
    ): RegisterResponse

    @GET("users")
    suspend fun loginUser (
        @Query("select") select:String = "*",
        @Query("email") email: String,
        @Query("password") password: String,
        @Header("apikey") apikey:String = API_KEY
    ): LoginResponse

    @GET("users")
    suspend fun getCurrentUser (
        @Query("select") select:String = "*",
        @Query("id") id: String,
        @Header("apikey") apikey:String = API_KEY
    ): CurrentUserResponse

    @PATCH("users")
    suspend fun updateUserProfile (
        @Query("id") id:String,
        @Body update:Update,
        @Header("apikey") apikey:String = API_KEY,
        @Header("Prefer") prefer: String = "return=minimal"
    ): Response<UpdateUserResponse>

    @GET("users")
    suspend fun getAllAsatidz (
        @Query("select") select:String = "*",
        @Query("role") role:String = "eq.ustadz",
        @Header("apikey") apikey:String = API_KEY
    ): List<UstadzDataResponseItem>

    @GET("kajian")
    suspend fun countAsatidzKajian(
        @Query("select") select:String = "count",
        @Query("author_id") ustadz:String,
        @Header("apikey") apikey:String = API_KEY
    ): CountKajianResponse

    @POST("kajian")
    suspend fun postKajian(
        @Body kajian: PostKajian,
        @Header("apikey") apikey:String = API_KEY,
        @Header("Prefer") prefer: String = "return=representation",
        @Header("Content-Type") contenttype: String = "application/json"
    ): PostKajianResponse

    @GET("kajian")
    suspend fun getAllKajian(
        @Query("select") select:String = "*",
        @Header("apikey") apikey:String = API_KEY
    ): List<PostKajianResponseItem>

    @GET("kajian")
    suspend fun getKajianDetails(
        @Query("select") select:String = "*",
        @Query("id") id:String ,
        @Header("apikey") apikey:String = API_KEY
    ): PostKajianResponse

    @GET("followers")
    suspend fun countFollowers(
        @Query("select") select:String = "count",
        @Query("user_id") user:String,
        @Header("apikey") apikey:String = API_KEY
    ): CountFollowersResponse

    @GET("comments")
    suspend fun getAllComments(
        @Query("select") select:String = "*",
        @Query("kajian_id") kajianid:String,
        @Header("apikey") apikey:String = API_KEY
    ): List<CommentsResponseItem>

    @POST("comments")
    suspend fun postComment(
        @Body comments: Comment,
        @Header("Prefer") prefer: String = "return=representation",
        @Header("Content-Type") contenttype: String = "application/json",
        @Header("apikey") apikey:String = API_KEY
    ): CommentsResponse

    @GET("users_kajian")
    suspend fun countUsersKajian(
        @Query("select") select: String = "count",
        @Query("kajian_id") kajianid:String ,
        @Header("apikey") apikey:String = API_KEY
    ): CountUsersKajianResponse



    @POST("users_kajian")
    suspend fun joinKajian(
        @Body join:JoinKajian,
        @Header("Prefer") prefer: String = "return=representation",
        @Header("Content-Type") contenttype: String = "application/json",
        @Header("apikey") apikey:String = API_KEY
    ): JoinKajianResponse

    @GET("users_kajian")
    suspend fun getUsersKajian(
        @Query("select") select: String = "*",
        @Query("kajian_id") kajianid: String ,
        @Header("apikey") apikey:String = API_KEY,
    ): UsersInKajianResponse

    @GET("kajian")
    suspend fun getAllKajianUstadz(
        @Query("select") select:String = "*",
        @Query("author_id") ustadzId:String ,
        @Header("apikey") apikey:String = API_KEY
    ): List<PostKajianResponseItem>

    @POST("followers")
    suspend fun insertFollower(
        @Body follower: InsertFollower ,
        @Header("Prefer") prefer: String = "return=representation",
        @Header("Content-Type") contenttype: String = "application/json",
        @Header("apikey") apikey:String = API_KEY

    ): InsertFollowerResponse

    @DELETE("followers")
    suspend fun unfollowUser(
        @Query("user_id") userId: String,
        @Query("follower_id") followerId: String,
        @Header("apikey") apikey:String = API_KEY
    ): Response<InsertFollowerResponseItem>

    @GET("followers")
    suspend fun getAllFollowers(
        @Query("user_id") userid: String ,
        @Query("select") select: String = "*",
        @Header("apikey") apikey:String = API_KEY
    ): InsertFollowerResponse

    @PATCH("kajian")
    suspend fun updateKajian (
        @Query("id") id:String,
        @Body update:UpdateKajian,
        @Header("apikey") apikey:String = API_KEY,
        @Header("Prefer") prefer: String = "return=representation"
    ): Response<UpdateKajianResponse>


    companion object {

        private const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlubWhndHZzeXBreW53aWNlamFrIiwicm9sZSI6ImFub24iLCJpYXQiOjE2Njk4NzkxNjksImV4cCI6MTk4NTQ1NTE2OX0.Hb6-_eA7VicpUa1rjI1bXKg2DZrWg23j8FuTaqRVv3M"




        fun create(): SupabaseAPI {
            return Retrofit.Builder()
                .baseUrl("https://inmhgtvsypkynwicejak.supabase.co/rest/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SupabaseAPI::class.java)
        }
    }
}
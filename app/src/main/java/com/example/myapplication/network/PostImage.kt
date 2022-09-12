package com.example.myapplication.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface PostImage {
    @Multipart
    @PUT("/posts/{id}")
    fun sendImage(@Part("") image: MultipartBody.Part, @Path("id") id: Int)  : Call<ResponseImage>
}

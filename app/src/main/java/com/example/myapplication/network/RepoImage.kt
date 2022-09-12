package com.example.myapplication.network

import android.util.Log
import android.widget.Toast
import com.example.myapplication.MainActivity
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class RepoImage {

    public fun uploadImage(file: File) {
        val request = RequestBody.create(MediaType.parse("multipart"), file)
        val imageFile = MultipartBody.Part.createFormData("image", file.name, request)

        val client = NetworkClient().client().create(PostImage::class.java).sendImage(imageFile, 3)

        client.enqueue(object : retrofit2.Callback<ResponseImage>{

            // handling request saat fail
            override fun onFailure(call: Call<ResponseImage>?, t: Throwable?) {
                Log.e("DATA/IMAGE", call.toString())
            }

            // handling request saat response.
            override fun onResponse(call: Call<ResponseImage>?, response: Response<ResponseImage>?) {
                Log.i("DATA/IMAGE", response.toString())
            }


        })
    }
}
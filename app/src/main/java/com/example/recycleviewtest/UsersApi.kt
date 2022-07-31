package com.example.recycleviewtest

import com.example.recycleviewtest.data.Data
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File

const val BASE_URL = "https://api.github.com/"

interface UsersApi {
    @GET("users")
    fun getUsers(): Call<List<Data>>

    companion object{


        operator fun invoke() : UsersApi{
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UsersApi::class.java)
        }
    }
}
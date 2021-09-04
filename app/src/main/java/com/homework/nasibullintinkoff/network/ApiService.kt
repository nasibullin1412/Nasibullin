package com.homework.nasibullintinkoff.network

import com.homework.nasibullintinkoff.data.PostResponse
import com.homework.nasibullintinkoff.utils.NetworkConstants.BASE_URL
import com.homework.nasibullintinkoff.utils.addJsonConverter
import com.homework.nasibullintinkoff.utils.setClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface ApiService {

    @GET("random")
    suspend fun getRandomPost(): Response<PostResponse>

    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .setClient()
                .addJsonConverter()
                .build()
                .create(ApiService::class.java)
        }
    }
}
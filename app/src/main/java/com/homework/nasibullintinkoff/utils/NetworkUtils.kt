package com.homework.nasibullintinkoff.utils

import com.homework.nasibullintinkoff.BuildConfig
import com.homework.nasibullintinkoff.utils.NetworkConstants.JSON_KEY
import com.homework.nasibullintinkoff.utils.NetworkConstants.JSON_VALUE
import com.homework.nasibullintinkoff.utils.NetworkConstants.APPLICATION_JSON_TYPE
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

fun Retrofit.Builder.setClient() = apply {
    val okHttpClient = OkHttpClient.Builder()
        .addQueryInterceptor()
        .addHttpLoggingInterceptor()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()
    this.client(okHttpClient)
}

private fun OkHttpClient.Builder.addQueryInterceptor() = apply {
    val interceptor = Interceptor { chain ->
        var request = chain.request()
        val url: HttpUrl = request.url.newBuilder().addQueryParameter(JSON_KEY, JSON_VALUE).build()
        request = request.newBuilder().url(url).build()
        chain.proceed(request)
    }
    this.addInterceptor(interceptor)
}

private fun OkHttpClient.Builder.addHttpLoggingInterceptor() = apply {
    if (BuildConfig.DEBUG) {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        this.addNetworkInterceptor(interceptor)
    }
}

@Suppress("EXPERIMENTAL_API_USAGE")
fun Retrofit.Builder.addJsonConverter() = apply {
    val json = Json { ignoreUnknownKeys = true }
    val contentType = APPLICATION_JSON_TYPE.toMediaType()
    this.addConverterFactory(json.asConverterFactory(contentType))
}
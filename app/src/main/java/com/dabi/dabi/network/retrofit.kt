package com.dabi.dabi.network

import com.dabi.dabi.model.MediaTypeAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object Network {
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(MediaTypeAdapter())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor();


    private val client: OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(CredentialInterceptor())
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    var retrofit: Retrofit

    init {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.dabi-api.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }
}
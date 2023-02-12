package com.dabi.dabi.di

import com.dabi.dabi.model.MediaTypeAdapter
import com.dabi.dabi.network.CredentialInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit {
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(MediaTypeAdapter())
            .build()

        val loggingInterceptor = HttpLoggingInterceptor()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(CredentialInterceptor())
            .addNetworkInterceptor(loggingInterceptor)
            .build()

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        return Retrofit.Builder()
            .baseUrl("https://api.dabi-api.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }
}
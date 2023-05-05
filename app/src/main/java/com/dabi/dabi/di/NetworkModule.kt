package com.dabi.dabi.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.dabi.dabi.BuildConfig
import com.dabi.dabi.data.DateAdapter
import com.dabi.dabi.data.MediaTypeAdapter
import com.dabi.dabi.data.PinPositionAdapter
import com.dabi.dabi.data.StyleType
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.MessageDigest
import java.util.*

@Module
class NetworkModule {
    @Provides
    fun provideInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return loggingInterceptor
    }

    @Provides
    fun provideClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(CredentialInterceptor())
            .addNetworkInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(MediaTypeAdapter())
            .add(DateAdapter())
            .add(PinPositionAdapter())
            .add(StyleType::class.java, EnumJsonAdapter.create(StyleType::class.java))
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.dabi-api.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }
}

class CredentialInterceptor : Interceptor {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val signKey = genSignKey()
        val ms = currentUnixTime()
        request = request.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("App-signature", signKey)
            .addHeader("Timestamp", ms.toString())
            .build()
        return chain.proceed(request)
    }

    private fun currentUnixTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, -3)
        return calendar.time.time / 1000
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun genSignKey(): String {
        val ms = currentUnixTime()
        val payload = "${ms}${BuildConfig.SERVER_KEY}".toByteArray(Charsets.UTF_8)
        return hash(payload)

    }

    private fun hash(bytes: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
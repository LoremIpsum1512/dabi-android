package com.dabi.dabi.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.dabi.dabi.BuildConfig
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*

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
        calendar.add(Calendar.MINUTE,-3)
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
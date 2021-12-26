package com.corp.luqman.movielisting.data.remote

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

fun provideOkHttpClient(context: Context): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    httpClient.apply {
        writeTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        callTimeout(60, TimeUnit.SECONDS)
        authenticator(MyAuthenticator())
        addInterceptor(ChuckInterceptor(context))
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(logging)
    }
    return httpClient.build()
}



inline fun <reified T> createWebService(okHttpClient: OkHttpClient, baseUrl: String): T {
    val moshi = Moshi.Builder().build()
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    return retrofit.create(T::class.java)
}

class MyAuthenticator() : Authenticator {
    private var request: Request? = null
    private var response: Response? = null
    override fun authenticate(route: Route?, response: Response): Request? {
        var tokenRefresh = ""


        if (!tokenRefresh.isBlank()) {
            return this.response?.request?.newBuilder()
                ?.header(
                    "Authorization",
                    "Bearer ${tokenRefresh}"
                )
                ?.build()
        } else {
            return null
        }
    }

}
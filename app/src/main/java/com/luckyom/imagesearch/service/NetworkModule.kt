package com.luckyom.imagesearch.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient

    private fun getRetrofit(baseUrl: String): Retrofit {
        if (!::retrofit.isInitialized) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retrofit
    }

    private fun getHttpClient(): OkHttpClient {
        if (!::okHttpClient.isInitialized) {
            okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()
        }
        return okHttpClient
    }

    fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {
        return getRetrofit(baseUrl).create(serviceClass)
    }
}

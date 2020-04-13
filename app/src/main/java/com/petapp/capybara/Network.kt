package com.petapp.capybara

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {

    val api: Api by lazy {
        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor {
                var request: Request = it.request()

                val url = request.url().newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .build()

                request = request.newBuilder()
                    .url(url)
                    .build()

                it.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return@lazy retrofit.create(Api::class.java)
    }
}
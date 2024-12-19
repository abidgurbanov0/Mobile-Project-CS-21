package com.example.muricerr.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DeezerApiInstance {
    private const val BASE_URL = "https://api.deezer.com/"
    val api: DeezerApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeezerApiInterface::class.java)
    }
}

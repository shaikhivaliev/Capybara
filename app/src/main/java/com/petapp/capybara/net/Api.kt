package com.petapp.capybara.net

import com.petapp.capybara.entity.Dataset
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("v1/datasets/{id}")
    suspend fun getDataset(@Path("id") id: Int): Response<Dataset>

}
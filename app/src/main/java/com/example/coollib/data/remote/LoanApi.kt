package com.example.coollib.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface LoanApi {

    @GET("loan")
    suspend fun getAllLoans(): Response<List<LoanDto>>
}
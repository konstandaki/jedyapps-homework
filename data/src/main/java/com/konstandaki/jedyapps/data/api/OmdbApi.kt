package com.konstandaki.jedyapps.data.api

import com.konstandaki.jedyapps.data.BuildConfig
import com.konstandaki.jedyapps.data.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET(".")
    suspend fun search(
        @Query("s") s: String,
        @Query("page") page: Int? = null,
        @Query("apikey") apiKey: String = BuildConfig.OMDB_API_KEY
    ): SearchResponseDto
}
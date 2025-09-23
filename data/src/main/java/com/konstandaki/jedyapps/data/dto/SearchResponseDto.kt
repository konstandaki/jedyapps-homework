package com.konstandaki.jedyapps.data.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class SearchResponseDto(
    @SerialName("Search") val search: List<MovieDto>? = null,
    @SerialName("totalResults") val totalResults: String? = null,
    @SerialName("Response") val response: String,
    @SerialName("Error") val error: String? = null
)
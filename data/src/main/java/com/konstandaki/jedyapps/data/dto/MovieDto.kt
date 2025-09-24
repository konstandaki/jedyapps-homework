package com.konstandaki.jedyapps.data.dto

import android.annotation.SuppressLint
import com.konstandaki.jedyapps.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MovieDto(
    @SerialName("imdbID") val imdbID: String,
    @SerialName("Title")  val title: String,
    @SerialName("Year")   val year: String,
    @SerialName("Type")   val type: String,
    @SerialName("Poster") val poster: String? = null
)

fun MovieDto.toDomain(): Movie = Movie(
    id = imdbID,
    title = title,
    year = year,
    type = type,
    poster = poster ?: ""
)
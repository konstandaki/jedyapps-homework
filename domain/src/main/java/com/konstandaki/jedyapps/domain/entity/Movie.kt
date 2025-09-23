package com.konstandaki.jedyapps.domain.entity

data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val type: String,
    val poster: String?
)
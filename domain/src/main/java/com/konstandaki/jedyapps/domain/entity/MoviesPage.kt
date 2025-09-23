package com.konstandaki.jedyapps.domain.entity

data class MoviesPage(
    val items: List<Movie>,
    val total: Int,
    val page: Int
)
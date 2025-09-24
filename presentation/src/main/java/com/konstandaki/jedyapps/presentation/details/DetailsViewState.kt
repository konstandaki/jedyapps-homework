package com.konstandaki.jedyapps.presentation.details

data class DetailsViewState(
    val id: String = "",
    val title: String = "",
    val year: String = "",
    val type: String = "",
    val poster: String = "",
    val isFavorite: Boolean = false
)
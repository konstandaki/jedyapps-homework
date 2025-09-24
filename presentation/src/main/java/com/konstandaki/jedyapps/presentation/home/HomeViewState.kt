package com.konstandaki.jedyapps.presentation.home

data class HomeViewState(
    val query: String = "",
    val canSearch: Boolean = false
)
package com.konstandaki.jedyapps.domain.repo

import com.konstandaki.jedyapps.domain.entity.MoviesPage

interface MoviesRepository {
    suspend fun search(query: String, page: Int = 1): MoviesPage
}
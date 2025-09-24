package com.konstandaki.jedyapps.domain.repo

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    val favoritesIds: Flow<Set<String>>
    suspend fun add(id: String)
    suspend fun remove(id: String)
    suspend fun toggle(id: String)
}
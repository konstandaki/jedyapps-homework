package com.konstandaki.jedyapps.domain.interactor

import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    val favoritesIds: Flow<Set<String>>
    suspend fun toggle(id: String)
}
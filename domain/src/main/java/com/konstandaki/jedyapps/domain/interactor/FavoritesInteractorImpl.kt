package com.konstandaki.jedyapps.domain.interactor

import com.konstandaki.jedyapps.domain.repo.FavoritesRepository
import javax.inject.Inject

class FavoritesInteractorImpl @Inject constructor(
    private val fav: FavoritesRepository
) : FavoritesInteractor {
    override val favoritesIds = fav.favoritesIds
    override suspend fun toggle(id: String) = fav.toggle(id)
}
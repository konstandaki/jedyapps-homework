package com.konstandaki.jedyapps.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konstandaki.jedyapps.domain.interactor.FavoritesInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val favorites: FavoritesInteractor
) : ViewModel() {

    private val movieId: String = requireNotNull(savedState["id"])
    private val title: String = savedState["title"] ?: ""
    private val year: String  = savedState["year"] ?: ""
    private val type: String  = savedState["type"] ?: ""
    private val poster: String = savedState["poster"] ?: ""

    private val _viewState = MutableStateFlow(
        DetailsViewState(
            id = movieId, title = title, year = year, type = type, poster = poster
        )
    )
    val viewState: StateFlow<DetailsViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            favorites.favoritesIds.collect { favorites ->
                _viewState.update { it.copy(isFavorite = movieId in favorites) }
            }
        }
    }

    fun onToggleFavorite() {
        viewModelScope.launch { favorites.toggle(movieId) }
    }
}
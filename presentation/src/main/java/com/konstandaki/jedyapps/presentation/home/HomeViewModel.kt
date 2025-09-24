package com.konstandaki.jedyapps.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.konstandaki.jedyapps.domain.entity.Movie
import com.konstandaki.jedyapps.domain.interactor.MoviesSearchInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val MIN_QUERY_LEN = 3

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: MoviesSearchInteractor
) : ViewModel() {

    private val query = MutableStateFlow("")

    val movies: Flow<PagingData<Movie>> =
        query
            .debounce(400)
            .map { it.trim() }
            .distinctUntilChanged()
            .flatMapLatest { q ->
                if (q.length < MIN_QUERY_LEN) flowOf(PagingData.empty())
                else interactor.search(q)
            }
            .cachedIn(viewModelScope)

    val viewState: StateFlow<HomeViewState> =
        query
            .map { q ->
                val t = q.trim()
                HomeViewState(
                    query = t,
                    canSearch = t.length >= MIN_QUERY_LEN
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = HomeViewState()
            )

    fun onQueryChange(newQuery: String) {
        query.value = newQuery
    }
}
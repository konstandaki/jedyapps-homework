package com.konstandaki.jedyapps.domain.interactor

import androidx.paging.PagingData
import com.konstandaki.jedyapps.domain.entity.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesSearchInteractor {
    fun search(query: String): Flow<PagingData<Movie>>
}
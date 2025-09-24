package com.konstandaki.jedyapps.domain.interactor

import androidx.paging.PagingData
import com.konstandaki.jedyapps.domain.entity.Movie
import com.konstandaki.jedyapps.domain.repo.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesSearchInteractorImpl @Inject constructor(
    private val repo: MoviesRepository
) : MoviesSearchInteractor {
    override fun search(query: String): Flow<PagingData<Movie>> = repo.search(query)
}
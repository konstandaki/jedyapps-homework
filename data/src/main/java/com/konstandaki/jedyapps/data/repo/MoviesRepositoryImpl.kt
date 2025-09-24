package com.konstandaki.jedyapps.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.konstandaki.jedyapps.data.api.OmdbApi
import com.konstandaki.jedyapps.data.paging.OMDB_PAGE_SIZE
import com.konstandaki.jedyapps.data.paging.OmdbSearchPagingSource
import com.konstandaki.jedyapps.domain.entity.Movie
import com.konstandaki.jedyapps.domain.repo.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: OmdbApi
) : MoviesRepository {

    override fun search(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = OMDB_PAGE_SIZE,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { OmdbSearchPagingSource(api, query) }
        ).flow
    }
}
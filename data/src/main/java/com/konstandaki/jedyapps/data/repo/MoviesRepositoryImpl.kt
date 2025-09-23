package com.konstandaki.jedyapps.data.repo

import com.konstandaki.jedyapps.data.api.OmdbApi
import com.konstandaki.jedyapps.data.dto.toDomain
import com.konstandaki.jedyapps.domain.entity.MoviesPage
import com.konstandaki.jedyapps.domain.repo.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: OmdbApi
) : MoviesRepository {

    override suspend fun search(query: String, page: Int): MoviesPage {
        val dto = api.search(s = query, page = page)

        if (dto.response != "True") {
            throw IllegalStateException(dto.error ?: "OMDb error")
        }

        val items = dto.search.orEmpty().map { it.toDomain() }
        val total = dto.totalResults?.toIntOrNull() ?: items.size
        return MoviesPage(items = items, total = total, page = page)
    }
}
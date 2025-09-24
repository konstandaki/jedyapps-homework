package com.konstandaki.jedyapps.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.konstandaki.jedyapps.data.api.OmdbApi
import com.konstandaki.jedyapps.data.dto.toDomain
import com.konstandaki.jedyapps.domain.entity.Movie
import retrofit2.HttpException
import java.io.IOException

private const val OMDB_FIRST_PAGE = 1
const val OMDB_PAGE_SIZE = 10

class OmdbSearchPagingSource(
    private val api: OmdbApi,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: OMDB_FIRST_PAGE
        return try {
            val res = api.search(s = query, page = page)
            if (res.response != "True") {
                return LoadResult.Error(IllegalStateException(res.error ?: "OMDb error"))
            }
            val items = res.search.orEmpty().map { it.toDomain() }
            val totalResults = res.totalResults?.toIntOrNull() ?: (page * OMDB_PAGE_SIZE)

            val nextKey =
                if (items.isEmpty() || page * OMDB_PAGE_SIZE >= totalResults) null
                else page + 1

            val prevKey = if (page == OMDB_FIRST_PAGE) null else page - 1

            LoadResult.Page(
                data = items,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (io: IOException) {
            LoadResult.Error(io)
        } catch (he: HttpException) {
            LoadResult.Error(he)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}
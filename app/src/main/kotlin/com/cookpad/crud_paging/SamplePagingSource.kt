package com.cookpad.crud_paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class SamplePagingSource(
    private val sampleRepository: SampleRepository
) : PagingSource<Int, SampleEntity>() {

    override fun getRefreshKey(state: PagingState<Int, SampleEntity>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SampleEntity> {
        return try {
            val data = sampleRepository.getNextPage(index = params.key ?: 1)
                .map { SampleEntity(it, "Page number: $it") }
            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (data.isNotEmpty()) {
                    data.last().id + 1
                } else {
                    // return null when page is empty to denote the end of the pagination
                    null
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

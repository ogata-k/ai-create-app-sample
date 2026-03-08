package com.example.ai_sample.data.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ai_sample.data.model.Item
import com.example.ai_sample.domain.usecase.GetItemsUseCase

class ItemPagingSource(
    private val getItemsUseCase: GetItemsUseCase
) : PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val page = params.key ?: 1
        val result = getItemsUseCase(page = page, limit = params.loadSize)

        return result.fold(
            onSuccess = { items ->
                LoadResult.Page(
                    data = items,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (items.isEmpty()) null else page + 1
                )
            },
            onFailure = { exception ->
                LoadResult.Error(exception)
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
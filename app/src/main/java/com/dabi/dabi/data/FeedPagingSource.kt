package com.dabi.dabi.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dabi.dabi.api.FeedService

class FeedPagingSource(
    private val feedService: FeedService,
    private val style: StyleType?
) : PagingSource<Int, Feed>() {

    override fun getRefreshKey(state: PagingState<Int, Feed>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response = feedService.getPagingFeed(nextPageNumber, style = style)
            LoadResult.Page(
                data = response.results,
                prevKey = null, // Only paging forward.
                nextKey = if (response.next == null) null else nextPageNumber + response.results.size
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
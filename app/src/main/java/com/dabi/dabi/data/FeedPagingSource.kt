package com.dabi.dabi.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dabi.dabi.api.FeedService
import kotlinx.coroutines.delay

class FeedPagingSource(
    private val feedService: FeedService,
    private val query: FeedQuery?
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
            val response = query?.let { it ->
                val q = it.asQueryParams()
                val hashtags = q.hashtags?.let {
                    it.joinToString(",")
                }
                feedService.getPagingFeed(
                    nextPageNumber,
                    style = q.style,
                    maxHeight = q.maxHeight,
                    minHeight = q.minHeight,
                    maxWeight = q.maxWeight,
                    minWeight = q.minWeight,
                    hashtags = hashtags
                )
            } ?: feedService.getPagingFeed(nextPageNumber)
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
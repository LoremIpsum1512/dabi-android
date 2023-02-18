package com.dabi.dabi.ui.feed

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dabi.dabi.data.remote.feed.FeedRemoteDataSource
import com.dabi.dabi.model.Feed

class FeedPagingSource(
    private val dataSource: FeedRemoteDataSource,
    private val style:String?,
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
            val response = dataSource.getPaging(nextPageNumber,style)
            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = if (response.next == null) null else nextPageNumber + response.results.size,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}
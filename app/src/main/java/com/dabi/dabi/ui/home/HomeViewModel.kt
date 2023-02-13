package com.dabi.dabi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dabi.dabi.data.remote.feed.FeedRemoteDataSource
import com.dabi.dabi.ui.feed.FeedPagingSource
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val dataSource: FeedRemoteDataSource) :
    ViewModel() {
    val feedFlow = Pager(
        PagingConfig(pageSize = 20)
    ) {
        FeedPagingSource(dataSource)
    }.flow
        .cachedIn(viewModelScope)
}
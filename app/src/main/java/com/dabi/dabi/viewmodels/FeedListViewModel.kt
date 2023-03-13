package com.dabi.dabi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dabi.dabi.data.Feed
import com.dabi.dabi.data.FeedRepository
import com.dabi.dabi.data.StyleType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FeedListViewModel @Inject constructor(private val feedRepository: FeedRepository) :
    ViewModel() {

    private val styleFilter = MutableStateFlow<StyleType?>(null)
    val feedFlow: Flow<PagingData<Feed>>

    init {
        feedFlow = getFeeds().cachedIn(viewModelScope)
    }

    private fun getFeeds(): Flow<PagingData<Feed>> =
        feedRepository.getFeedPagingDataStream()


    fun applyStyle() {
        val style: StyleType = StyleType.values().random()
        println(style)
    }

}
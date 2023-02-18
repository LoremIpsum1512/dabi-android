package com.dabi.dabi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dabi.dabi.model.Feed
import com.dabi.dabi.repository.FeedRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val feedRepository: FeedRepository) :
    ViewModel() {

    private val styleFilter = MutableStateFlow<String>("")
    val feedFlow: Flow<PagingData<Feed>>

    init {
        feedFlow = styleFilter
            .flatMapLatest { style ->
                getFeeds(style)
            }.cachedIn(viewModelScope)
    }

    private fun getFeeds(style: String?): Flow<PagingData<Feed>> =
        feedRepository.getFeedPagingDataStream(style)


    fun applyStyle() {
        val style: String = listOf("lovely", "sexy", "office").random()
        viewModelScope.launch {
            styleFilter.emit(style)
        }
    }

}
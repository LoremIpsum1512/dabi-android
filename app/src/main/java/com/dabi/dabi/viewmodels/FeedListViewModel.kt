package com.dabi.dabi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dabi.dabi.data.Feed
import com.dabi.dabi.data.FeedRepository
import com.dabi.dabi.data.StyleType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedListViewModel @Inject constructor(private val feedRepository: FeedRepository) :
    ViewModel() {

    private val styleFilter = MutableStateFlow<StyleType?>(null)
    private val _feedFlow = MutableStateFlow<PagingData<Feed>>(PagingData.empty())
    val feedFlow: Flow<PagingData<Feed>> = _feedFlow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            styleFilter.flatMapLatest { style ->
                feedRepository.getFeedPagingDataStream(style)
            }.collectLatest { pagingData ->
                _feedFlow.emit(pagingData)
            }
        }

    }

    fun applyStyle() {
        val style: StyleType = StyleType.values().random()
        styleFilter.value = style
    }

    fun refresh() {
        viewModelScope.launch {
            feedRepository.getFeedPagingDataStream(null).collectLatest {
                _feedFlow.emit(it)
            }
        }
    }

}
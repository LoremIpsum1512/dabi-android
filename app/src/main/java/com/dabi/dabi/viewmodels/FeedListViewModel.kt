package com.dabi.dabi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.dabi.dabi.adapters.FeedUIModel
import com.dabi.dabi.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedListViewModel @Inject constructor(private val feedRepository: FeedRepository) :
    ViewModel() {

    val styleQuery = MutableStateFlow<StyleType?>(null)
    val heightQuery = MutableStateFlow<HeightQueryValue?>(null)

    private val _feedQuery: Flow<FeedQuery> = styleQuery.combine(heightQuery) { style, height ->
        FeedQuery(
            style = style,
            height = height
        )
    }.distinctUntilChanged()

    private val _uiModelFlow = MutableStateFlow<PagingData<FeedUIModel>>(PagingData.empty())
    val uiModelFlow: Flow<PagingData<FeedUIModel>> = _uiModelFlow.cachedIn(viewModelScope)

    private fun getPagingUIModel(query: FeedQuery?): Flow<PagingData<FeedUIModel>> {
        return feedRepository.getFeedPagingDataStream(query).cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { FeedUIModel.ImageItem(it) }
            }
    }

    init {
        viewModelScope.launch {
            _feedQuery.flatMapLatest { getPagingUIModel(it) }.collectLatest { pagingData ->
                _uiModelFlow.emit(pagingData)
            }
        }

    }

    fun refresh() {
        viewModelScope.launch {
            getPagingUIModel(null).collectLatest {
                _uiModelFlow.emit(it)
            }
        }
    }

    fun setHeader(uiModel: FeedUIModel) {
        viewModelScope.launch {
            _uiModelFlow.emit(
                _uiModelFlow.value.insertHeaderItem(item = uiModel)
            )
        }
    }

}
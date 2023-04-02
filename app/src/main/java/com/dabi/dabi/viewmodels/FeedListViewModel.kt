package com.dabi.dabi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.dabi.dabi.adapters.FeedUIModel
import com.dabi.dabi.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import javax.inject.Inject

class FeedListViewModel @Inject constructor(private val feedRepository: FeedRepository) :
    ViewModel() {

    private val styleQuery = MutableStateFlow<StyleType?>(null)
    val heightQuery = MutableStateFlow<HeightQueryValue?>(null)
    var header: FeedUIModel? = null

    private val _feedQuery: Flow<FeedQuery> = styleQuery.map { style ->
        FeedQuery(
            style = style,
            height = null
        )
    }.distinctUntilChanged()

    private val _uiModelFlow = MutableStateFlow<PagingData<FeedUIModel>>(PagingData.empty())
    val uiModelFlow: Flow<PagingData<FeedUIModel>> = _uiModelFlow
        .map{pagingData ->
             header?.let { pagingData.insertHeaderItem(item = header!!) } ?: pagingData

    }
        .cachedIn(viewModelScope)

    private fun getPagingUIModel(query: FeedQuery?): Flow<PagingData<FeedUIModel>> {
        return feedRepository.getFeedPagingDataStream(query).cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { FeedUIModel.ImageItem(it) }
            }
    }

    init {
        viewModelScope.launch {
            _feedQuery.flatMapLatest {
                getPagingUIModel(it)
            }.collectLatest { pagingData ->
                _uiModelFlow.emit(pagingData)
            }
        }

        viewModelScope.launch {
            styleQuery.collect {
                it
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

    fun setHeaderUiModel(uiModel: FeedUIModel) {
        header = uiModel

//        viewModelScope.launch {
//            _uiModelFlow.emit(
//                _uiModelFlow.value.insertHeaderItem(item = uiModel)
//            )
//        }
    }

    fun setStyle(styleType: StyleType){
        styleQuery.value = styleType

    }
}
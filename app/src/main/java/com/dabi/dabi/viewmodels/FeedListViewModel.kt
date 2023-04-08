package com.dabi.dabi.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.dabi.dabi.adapters.FeedUIModel
import com.dabi.dabi.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class FeedListViewModel @Inject constructor(private val feedRepository: FeedRepository) :
    ViewModel() {

    private val _styleQuery = MutableStateFlow<StyleType?>(null)
    val styleType = _styleQuery.asStateFlow()

    private val heightQuery = MutableStateFlow<HeightQueryValue?>(null)
    private var header: FeedUIModel? = null
    // generate random UUID every refresh call to force create new PagingData
    private val refreshIdFlow = MutableStateFlow<String>("")
    private val _feedQuery: Flow<FeedQuery?> = _styleQuery.combine(heightQuery) { style, height ->
        FeedQuery(
            style = style,
            height = height
        )
    }.distinctUntilChanged()

    private val _uiModelFlow = refreshIdFlow.combine(_feedQuery) { _, query ->
        query
    }.flatMapLatest { query ->
        getPagingUIModel(query)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = PagingData.empty()
    )


    val uiModelFlow: Flow<PagingData<FeedUIModel>> = _uiModelFlow
        .map { pagingData ->
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


    }

    fun refresh() {
        viewModelScope.launch {
            refreshIdFlow.emit(UUID.randomUUID().toString())
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

    fun setStyle(styleType: StyleType) {
        _styleQuery.value = styleType
    }
}
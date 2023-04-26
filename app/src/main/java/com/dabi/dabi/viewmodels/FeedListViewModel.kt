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

    private val _heightQuery = MutableStateFlow<HeightQueryValue?>(null)
    val heightType = _heightQuery.asStateFlow()

    private val _weightQuery = MutableStateFlow<WeightQueryValue?>(null)
    val weightType = _weightQuery.asStateFlow()

    private val _hashtagsQuery = MutableStateFlow<List<String>?>(null)


    // generate random UUID every refresh call to force create new PagingData
    private val refreshIdFlow = MutableStateFlow<String>("")
    private val _feedQuery: Flow<FeedQuery?> =
        combine(
            _styleQuery,
            _heightQuery,
            _weightQuery,
            _hashtagsQuery.debounce(200)
        ) { style, height, weight, hashtags ->
            FeedQuery(
                style = style,
                height = height,
                weight = weight,
                hashtags = hashtags
            )
        }
            .distinctUntilChanged()

    private val _uiModelFlow = refreshIdFlow.combine(_feedQuery) { _, query ->
        query
    }.flatMapLatest { query ->
        getPagingUIModel(query)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = PagingData.empty()
    )

    val filterCountFlow =
        combine(_styleQuery, _heightQuery, _weightQuery) { style, height, weight ->
            listOf(style != null, height != null, weight != null).fold(0) { acc, curr ->
                if (curr)
                    acc + 1
                else
                    acc
            }
        }

    val uiModelFlow: Flow<PagingData<FeedUIModel>> = _uiModelFlow
        .cachedIn(viewModelScope)

    private fun getPagingUIModel(query: FeedQuery?): Flow<PagingData<FeedUIModel>> {
        return feedRepository.getFeedPagingDataStream(query).cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { FeedUIModel.ImageItem(it) }
            }
    }

    fun refresh() {
        viewModelScope.launch {
            refreshIdFlow.emit(UUID.randomUUID().toString())
        }
    }

    fun setStyle(styleType: StyleType?) {
        _styleQuery.value = styleType
    }

    fun setHeight(heightType: HeightQueryValue?) {
        _heightQuery.value = heightType
    }

    fun setWeight(weightType: WeightQueryValue?) {
        _weightQuery.value = weightType
    }

    fun setHashtags(hashtags: List<String>) {
        if (hashtags.isEmpty()) {
            _hashtagsQuery.value = null
            return
        }
        if (_hashtagsQuery.value != null && _hashtagsQuery.value!!.size == hashtags.size && _hashtagsQuery.value!!.containsAll(
                hashtags
            )
        ) return
        else _hashtagsQuery.value = hashtags
    }
}
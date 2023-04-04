package com.dabi.dabi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dabi.dabi.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor() :
    ViewModel() {

//    private val styleFilter = MutableStateFlow<StyleType?>(null)
//    val feedFlow: Flow<PagingData<Feed>>
//
//    init {
//        feedFlow = getFeeds().cachedIn(viewModelScope)
//    }
//
//    private fun getFeeds(): Flow<PagingData<Feed>> =
//        feedRepository.getFeedPagingDataStream()
//
//
//    fun applyStyle() {
//        val style: StyleType = StyleType.values().random()
//        viewModelScope.launch {
//            styleFilter.emit(style)
//        }
//    }

}
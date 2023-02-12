package com.dabi.dabi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dabi.dabi.data.remote.feed.FeedRemoteDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val dataSource: FeedRemoteDataSource) :
    ViewModel() {

    fun getPagingFeed() {
        viewModelScope.launch {
            val data = dataSource.getPaging()
            println(data)
        }
    }
}
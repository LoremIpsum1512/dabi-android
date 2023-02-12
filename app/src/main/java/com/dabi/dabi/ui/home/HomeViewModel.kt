package com.dabi.dabi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dabi.dabi.data.remote.feed.FeedRemoteDataSource
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val datasource = FeedRemoteDataSource()
    fun getPagingFeed() {
        viewModelScope.launch {
            val data = datasource.getPaging()
            println(data)
        }
    }
}
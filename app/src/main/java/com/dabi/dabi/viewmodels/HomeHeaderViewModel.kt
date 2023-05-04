package com.dabi.dabi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dabi.dabi.data.HandledException
import com.dabi.dabi.data.HashtagRepository
import com.dabi.dabi.data.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeHeaderViewModel @Inject constructor(private val hashtagRepository: HashtagRepository) :
    ViewModel() {
    private val _hashtagsResourceFlow = MutableStateFlow<Resource<List<String>>>(Resource.Loading())
    val hashtagsResource = _hashtagsResourceFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Resource.Loading()
    )

    fun getHashtags() {
        viewModelScope.launch {
            try {
                _hashtagsResourceFlow.value = Resource.Loading()
                val hashtags = hashtagRepository.getAll()
                _hashtagsResourceFlow.value = Resource.Success(hashtags)
            } catch (e: Exception) {
                _hashtagsResourceFlow.value = Resource.Error(
                    HandledException.of(e)
                )
            }

        }
    }
}
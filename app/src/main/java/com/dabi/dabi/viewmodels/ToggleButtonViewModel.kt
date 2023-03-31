package com.dabi.dabi.viewmodels

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dabi.dabi.R
import com.dabi.dabi.databinding.ToogleButtonBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ToggleButtonViewModel() : ViewModel() {
    lateinit var binding: ToogleButtonBinding
    private val _selectedFlow = MutableStateFlow(false)
    val styleFlow: Flow<ToggleButtonStyle> = _selectedFlow.mapLatest { isSelected ->
        if (isSelected) {
            ToggleButtonStyle(
                R.color.black,
                R.color.white
            )
        } else {
            ToggleButtonStyle(
                R.color.component_gray,
                R.color.black
            )
        }
    }

    fun toggle() {
        _selectedFlow.value = !_selectedFlow.value
    }
}

data class ToggleButtonStyle(
    val backgroundColor: Int,
    val textColor: Int
)
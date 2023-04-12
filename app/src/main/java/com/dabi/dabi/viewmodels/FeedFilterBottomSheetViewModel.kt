package com.dabi.dabi.viewmodels

import androidx.lifecycle.ViewModel
import com.dabi.dabi.data.HeightQueryValue
import com.dabi.dabi.data.StyleType
import com.dabi.dabi.data.WeightQueryValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeedFilterBottomSheetViewModel : ViewModel() {
    private val _selectedStyle = MutableStateFlow<StyleType?>(null)
    val selectedStyle = _selectedStyle.asStateFlow()
    private val _selectedHeight = MutableStateFlow<HeightQueryValue?>(null)
    val selectedHeight = _selectedHeight.asStateFlow()

    private val _selectedWeight = MutableStateFlow<WeightQueryValue?>(null)
    val selectedWeight = _selectedWeight.asStateFlow()

    fun setStyle(style: StyleType?) {
        _selectedStyle.value = style
    }

    fun setHeight(height: HeightQueryValue?) {
        _selectedHeight.value = height
    }

    fun setWeight(weight: WeightQueryValue?) {
        _selectedWeight.value = weight
    }
}
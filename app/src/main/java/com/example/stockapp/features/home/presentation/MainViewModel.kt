package com.example.stockapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.features.home.domain.GetMockDataUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMockDataUsecase: GetMockDataUsecase
): ViewModel() {

    private val _state = MutableStateFlow<DataUI?>(null)
    val state = _state

    fun someProccess() {
        viewModelScope.launch {
            val result = getMockDataUsecase.invoke("Params")
            state.value = result
        }
    }
}
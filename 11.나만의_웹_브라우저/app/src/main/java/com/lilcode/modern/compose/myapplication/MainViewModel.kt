package com.lilcode.modern.compose.myapplication

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    companion object {
        const val START_URL = "https://www.google.com"
    }
    private val _addressText = mutableStateOf<String>(START_URL)
    val addressText: State<String> = _addressText

    val url = mutableStateOf(START_URL)

    /*
        한번에 하나의 데이터 발행, 동일한 데이터 발행
        default 값 설정 안해도됨.
         */
    private val _undoSharedFlow = MutableSharedFlow<Boolean>()
    val undoSharedFlow = _undoSharedFlow.asSharedFlow()

    private val _redoSharedFlow = MutableSharedFlow<Boolean>()
    val redoSharedFlow = _redoSharedFlow.asSharedFlow()

    fun undo() {
        viewModelScope.launch {
            _undoSharedFlow.emit(true)
        }
    }

    fun redo() {
        viewModelScope.launch {
            _redoSharedFlow.emit(true)
        }
    }

    fun setAddressText(addressString: String) {
        _addressText.value = addressString
    }
}
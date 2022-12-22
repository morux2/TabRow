package com.example.tabrow

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AViewModel : ViewModel() {

    private val _viewState = mutableStateOf(INITIAL)
    val viewState: State<ViewState> = _viewState

    init {
        Log.d("init ViewModel A", checkNotNull(_viewState.value).message)
    }

    fun updateMessage() {
        _viewState.value = checkNotNull(_viewState.value).copy(
            message = "A updated"
        )
        Log.d("ViewModel A", checkNotNull(_viewState.value).message)
    }

    data class ViewState(
        val message: String
    )

    companion object {
        val INITIAL = ViewState(message = "A")
    }
}
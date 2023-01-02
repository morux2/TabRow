package com.example.tabrow

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BViewModel : ViewModel() {
    private val _viewState = mutableStateOf(INITIAL)
    val viewState: State<ViewState> = _viewState

    init {
        Log.d("init ViewModel B", checkNotNull(_viewState.value).message)
    }

    fun updateMessage() {
        _viewState.value = checkNotNull(_viewState.value).copy(
            message = "B updated"
        )
        Log.d("ViewModel B", checkNotNull(_viewState.value).message)
    }

    data class ViewState(
        val message: String
    )

    companion object {
        val INITIAL = ViewState(message = "B")
    }

    interface bClickEvent {
        fun update()
    }
}
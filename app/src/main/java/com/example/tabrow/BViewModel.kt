package com.example.tabrow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BViewModel : ViewModel() {
    private val _viewState = MutableLiveData(INITIAL)
    val viewState: LiveData<ViewState> = _viewState

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
}
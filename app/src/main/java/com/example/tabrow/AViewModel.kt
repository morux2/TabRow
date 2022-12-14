package com.example.tabrow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AViewModel : ViewModel() {

    private val _viewState = MutableLiveData(INITIAL)
    val viewState: LiveData<ViewState> = _viewState

    data class ViewState(
        val message: String
    )

    companion object {
        val INITIAL = ViewState(message = "A")
    }
}
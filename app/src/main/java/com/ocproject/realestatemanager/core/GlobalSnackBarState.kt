package com.ocproject.realestatemanager.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GlobalSnackBarState(
    val message: String? = null,
    val isVisible: Boolean = false,
    val isSuccess: Boolean = true,
    val timestamp: Long = 0L
)

object GlobalSnackBarManager {
    private val _snackBarState = MutableStateFlow(GlobalSnackBarState())
    val snackBarState = _snackBarState.asStateFlow()

    fun showSnackMsg(message: String, isSuccess: Boolean = true) {
        _snackBarState.value = GlobalSnackBarState(
            message = message,
            isVisible = true,
            isSuccess = isSuccess,
            timestamp = System.currentTimeMillis()
        )
    }

    fun hideToast() {
        _snackBarState.value = GlobalSnackBarState()
    }
}
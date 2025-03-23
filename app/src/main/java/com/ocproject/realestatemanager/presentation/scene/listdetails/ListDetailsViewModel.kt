package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.lifecycle.ViewModel
import com.ocproject.realestatemanager.presentation.scene.funding.FundingEvent
import com.ocproject.realestatemanager.presentation.scene.funding.FundingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListDetailsViewModel() : ViewModel() {
    private val _state = MutableStateFlow(ListDetailsState())
    val state = _state.asStateFlow()

    fun onEvent(event: ListDetailsEvent) {
        when (event) {
            is ListDetailsEvent.OnClickMapMode -> {
                _state.update {
                    it.copy(
                        mapMode = event.value
                    )
                }
            }
        }
    }
}
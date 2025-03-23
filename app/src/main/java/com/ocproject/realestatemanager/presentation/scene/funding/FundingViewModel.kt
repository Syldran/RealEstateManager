package com.ocproject.realestatemanager.presentation.scene.funding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyState
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.IntFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow

class FundingViewModel() : ViewModel() {
    private val _state = MutableStateFlow(FundingState())
    val state = _state.asStateFlow()
    var price: Int by mutableIntStateOf(0)
    fun onEvent(event: FundingEvent) {
        when (event) {
            is FundingEvent.OnPriceInput -> {
                var intFormatter = IntFormatter()
                price = event.value?.let { intFormatter.cleanup(it) }?.toInt() ?: price
            }

            is FundingEvent.OnRateOptionChosen -> {
                _state.update {
                    it.copy(
                        chosenRate = event.value,
                        chosenText = event.text,
                    )
                }
            }
        }
    }

    fun calcMonthlyPayment(amountToBorrow: Double, rate: Double, durationInMonth: Double) : Double{
    var monthlyPayment = (amountToBorrow * rate / 12) / (1- (1+rate/12).pow(-durationInMonth))
        return monthlyPayment
    }

}
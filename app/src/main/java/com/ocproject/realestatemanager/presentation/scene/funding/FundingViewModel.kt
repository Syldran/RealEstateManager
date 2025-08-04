package com.ocproject.realestatemanager.presentation.scene.funding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.pow

class FundingViewModel() : ViewModel() {
    private val _state = MutableStateFlow(FundingState())
    val state = _state.asStateFlow()
    var price: Int by mutableIntStateOf(0)
    fun onEvent(event: FundingEvent) {
        when (event) {
            is FundingEvent.OnPriceInput -> {
                price = event.value?.toIntOrNull() ?: price
//                price = event.value?.let { intFormatter.cleanup(it) }?.toInt() ?: price
            }

            is FundingEvent.OpenRatingSelectionSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isRatingListSheetOpen = true,
                        )
                    }
                }
            }

            is FundingEvent.DismissRatingSelectionSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isRatingListSheetOpen = false,
                        )
                    }
                }
            }

            is FundingEvent.OnRateOptionChosen -> {
                _state.update {
                    it.copy(
                        chosenRate = event.value,
                        chosenText = event.text,
                    )
                }
            }

            is FundingEvent.OnOpenRateList -> {
                _state.update {
                    it.copy(
                        isRatingListSheetOpen = event.value
                    )
                }
            }
        }
    }

    companion object {
        fun calcMonthlyPayment(amountToBorrow: Float, rate: Float, durationInMonth: Float): Float {
            val monthlyPayment =
                (amountToBorrow * rate / 12) / (1 - (1 + rate / 12).pow(-durationInMonth))
            return monthlyPayment
        }

        fun displayPercent(value: Float): String {
            return "${value * 100F} %"
        }

        fun displayTotalCost(monthlyPayment: Float, durationOfChosenRateInMonth: Int): String {
            return String.format(
                Locale.ROOT,
                "%.2f",
                monthlyPayment * durationOfChosenRateInMonth
            )

        }

        fun displayInterest(
            monthlyPayment: Float,
            durationChosenRateInMonths: Int,
            price: Float
        ): String {
            return String.format(
                Locale.ROOT,
                "%.2f",
                monthlyPayment * durationChosenRateInMonths - price
            )
        }
    }
}
package com.ocproject.realestatemanager.presentation.scene.funding

import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent

sealed interface FundingEvent {
    data class OnPriceInput(val value : String?) : FundingEvent
    data class OnRateOptionChosen(val value : FundingRate, val text:String) : FundingEvent
}
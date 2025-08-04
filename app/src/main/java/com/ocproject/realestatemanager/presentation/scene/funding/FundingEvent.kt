package com.ocproject.realestatemanager.presentation.scene.funding

sealed interface FundingEvent {
    data object DismissRatingSelectionSheet : FundingEvent
    data object OpenRatingSelectionSheet : FundingEvent
    data class OnPriceInput(val value : String?) : FundingEvent
    data class OnRateOptionChosen(val value : FundingRate, val text:String) : FundingEvent
    data class OnOpenRateList(val value : Boolean) : FundingEvent
}
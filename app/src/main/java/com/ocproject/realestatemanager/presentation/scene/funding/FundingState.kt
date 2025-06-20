package com.ocproject.realestatemanager.presentation.scene.funding

data class FundingState (
    val chosenRate: FundingRate = FundingRate.SEVEN_YEARS,
    val chosenText: String = "7 years 3.24 %",
    val isRatingSelectionSheetOpen: Boolean = false,
)
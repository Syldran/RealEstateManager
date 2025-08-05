package com.ocproject.realestatemanager.presentation.scene.funding

enum class FundingRate(val ratio: Float, val yearsInMonths: Int) {
    SEVEN_YEARS(0.0324F, 7 * 12),
    TEN_YEARS(0.0338F, 10 * 12),
    FIFTEEN_YEARS(0.0348F, 15 * 12),
    TWENTY_YEARS(0.0358F, 20 * 12),
    TWENTY_FIVE_YEARS(0.0368F, 25 * 12)
}
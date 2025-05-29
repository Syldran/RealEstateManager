package com.ocproject.realestatemanager.presentation.scene.funding

//class FundingRate {
//    companion object {
//        const val ON7YEARS= 0.0324F
//        const val ON10YEARS = 0.0338F
//        const val ON15YEARS = 0.0348F
//        const val ON20YEARS = 0.0358F
//        const val ON25YEARS = 0.0368F
//    }
//}

enum class FundingRate(val ratio: Float, val yearsInMonths: Int) {
    SEVEN_YEARS(0.0324F, 7 * 12),
    TEN_YEARS(0.0338F, 10 * 12),
    FIFTEEN_YEARS(0.0348F, 15 * 12),
    TWENTY_YEARS(0.0358F, 20 * 12),
    TWENTY_FIVE_YEARS(0.0368F, 25 * 12)
}
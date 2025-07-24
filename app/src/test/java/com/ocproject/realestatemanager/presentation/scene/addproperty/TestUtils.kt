package com.ocproject.realestatemanager.presentation.scene.addproperty

import com.ocproject.realestatemanager.core.utils.Utils.convertDollarToEuro
import com.ocproject.realestatemanager.core.utils.Utils.convertEuroToDollars
import com.ocproject.realestatemanager.core.utils.Utils.getTodayDate
import kotlinx.coroutines.test.runTest
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar

class TestUtils {

    @Test
    fun euroEuroDollarConversion() = runTest {
        val dollar: Double = 100.0
        val dollarInEuro: Double = 95.0

        assertEquals(convertDollarToEuro(dollar), dollarInEuro)
        assertEquals(convertEuroToDollars(dollarInEuro), dollar)
    }

    @Test
    fun dateFormat() = runTest {
        val cal = Calendar.getInstance()
        val day: String = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))
        val month = String.format("%02d", cal.get((Calendar.MONTH)) + 1)
        val year: String = cal.get(Calendar.YEAR).toString()
        val expectedFormatDate = "$day/$month/$year"

        assertEquals(expectedFormatDate, getTodayDate())
    }
}
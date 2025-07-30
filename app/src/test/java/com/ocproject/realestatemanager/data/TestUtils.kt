package com.ocproject.realestatemanager.data

import android.content.Context
import com.ocproject.realestatemanager.core.utils.Utils
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.Calendar

class TestUtils {


    @Test
    fun euroEuroDollarConversion() = runTest {
        val dollar: Double = 100.0
        val dollarInEuro: Double = 95.0

        TestCase.assertEquals(Utils.convertDollarToEuro(dollar), dollarInEuro)
        TestCase.assertEquals(Utils.convertEuroToDollars(dollarInEuro), dollar)
    }

    @Test
    fun dateFormat() = runTest {
        val cal = Calendar.getInstance()
        val day: String = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))
        val month = String.format("%02d", cal.get((Calendar.MONTH)) + 1)
        val year: String = cal.get(Calendar.YEAR).toString()
        val expectedFormatDate = "$day/$month/$year"

        TestCase.assertEquals(expectedFormatDate, Utils.getTodayDate())
    }
}
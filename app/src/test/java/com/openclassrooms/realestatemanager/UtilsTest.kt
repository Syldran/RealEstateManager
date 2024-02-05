package com.openclassrooms.realestatemanager

import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Test
import java.util.Calendar


class UtilsTest {

    @Test
    fun getTodayDate() {
        // WHEN
        val date = Utils.getTodayDate()

        // THEN
        val newDate = Calendar.getInstance()

        val stringDate = if (newDate.get(Calendar.DAY_OF_MONTH) < 10) {
            "0$newDate"
        } else {
            newDate.get(Calendar.DAY_OF_MONTH).toString()
        }
        assertThat(date.substring(0, 2)).isEqualTo(stringDate)
        if (newDate.get(Calendar.MONTH) + 1 < 10) {
            assertThat(
                date.substring(3, 5)).isEqualTo("0${newDate.get(Calendar.MONTH) + 1}")

        } else {
           assertThat(date.substring(3, 5)).isEqualTo(newDate.get(Calendar.MONTH) + 1).toString()
        }
        assertThat(date.substring(6, 10)).isEqualTo(newDate.get(Calendar.YEAR).toString())
    }

    @Test
    fun convertDollarToEuro() {
        // WHEN
        val result = Utils.convertDollarToEuro(500.0)

        // THEN
        assertThat(475.0).isEqualTo(result)
    }

    @Test
    fun convertEuroToDollars() {
        // WHEN
        val result = Utils.convertEuroToDollars(500.0)

        // THEN
        assertThat(525.0).isEqualTo(result)
    }

}

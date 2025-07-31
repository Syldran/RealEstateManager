package com.ocproject.realestatemanager.core.utils

import com.ocproject.realestatemanager.domain.models.Property
import java.text.DateFormat
import java.util.Date

class UtilsKotlin {


    companion object {

        val integerChars = '0'..'9'
        fun isNumber(input: String): Boolean {
            var dotOccurred = 0
            return input.all { it in integerChars || it == '.' && dotOccurred++ < 1 }
        }

        fun isInteger(input: String) = input.all { it in integerChars }
        fun datePresentation(property: Property): String {
            val date = Date(property.createdDate!!)
            val formatter: DateFormat =
                DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            return formatter.format(date)
        }
    }
}
package com.ocproject.realestatemanager.core.utils

import com.ocproject.realestatemanager.core.InterestPoint
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


        fun fromListInterestPoint(listInterestPoint: List<InterestPoint>): String {
            var returnString = ""
            var cpt = 0
            val orderedList = mutableListOf<InterestPoint>()
            if (listInterestPoint.contains(InterestPoint.SCHOOL)) {
                orderedList.add(InterestPoint.SCHOOL)
            }
            if (listInterestPoint.contains(InterestPoint.PARK)) {
                orderedList.add(InterestPoint.PARK)
            }
            if (listInterestPoint.contains(InterestPoint.SHOP)) {
                orderedList.add(InterestPoint.SHOP)
            }
            if (listInterestPoint.contains(InterestPoint.TRANSPORT)) {
                orderedList.add(InterestPoint.TRANSPORT)
            }
            if (orderedList.isNotEmpty()) {
                returnString = "%"
            }
            orderedList.forEach {
                returnString += if (cpt == 0) it.name else ",${it.name}"
                cpt++
            }
            if (orderedList.isNotEmpty()) {
                returnString += "%"
            }
            return returnString
        }

        fun toListInterestPoint(stringInterestPoint: String): List<InterestPoint> {
            val returnList = ArrayList<InterestPoint>()
            val split = stringInterestPoint.split(",")
            for (n in split) {
                try {
                    returnList.add(enumValueOf(n))
                } catch (_: Exception) {

                }
            }
            return returnList
        }
    }
}
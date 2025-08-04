package com.ocproject.realestatemanager.data.database

import androidx.room.TypeConverter
import com.ocproject.realestatemanager.core.InterestPoint

class Converters {
    @TypeConverter
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

    @TypeConverter
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
        orderedList.forEach {
            returnString += if (cpt == 0) it.name else ",${it.name}"
            cpt++
        }


        return returnString
    }
}

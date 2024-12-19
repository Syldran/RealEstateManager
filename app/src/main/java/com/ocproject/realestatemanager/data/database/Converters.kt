package com.ocproject.realestatemanager.data.database

import androidx.room.TypeConverter
import com.ocproject.realestatemanager.domain.models.InterestPoint

class Converters {
    @TypeConverter
    fun toListInterestPoint(stringInterestPoint: String): List<InterestPoint> {
        val returnList =  ArrayList<InterestPoint>()
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
    fun fromListInterestPoint(listInterestPoint: List<InterestPoint>?): String {
        var returnString:String = ""
        var cpt = 0
        listInterestPoint?.forEach {
            returnString += if (cpt ==0) it.name else ",${it.name}"
            cpt++
        }


        return returnString
    }
}

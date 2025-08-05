package com.ocproject.realestatemanager.core.utils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.domain.models.Property
import java.text.DateFormat
import java.util.Date
import androidx.core.graphics.createBitmap

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

        fun createMarkerIcon(property: Property, isSelected: Boolean = false): BitmapDescriptor {
            try {
                val firstPhoto = property.photoList.firstOrNull()
                if (firstPhoto?.photoBytes != null && firstPhoto.photoBytes.isNotEmpty()) {
                    val bitmap = BitmapFactory.decodeByteArray(
                        firstPhoto.photoBytes, 0,
                        firstPhoto.photoBytes.size
                    )
                    
                    // Check if bitmap was successfully decoded
                    if (bitmap != null) {
                        val resizedBitmap = bitmap.scale(96, 96, false)
                        
                        // If selected, add a red border
                        if (isSelected) {
                            val borderedBitmap =
                                createBitmap(resizedBitmap.width + 8, resizedBitmap.height + 8)
                            val canvas = android.graphics.Canvas(borderedBitmap)
                            val paint = android.graphics.Paint().apply {
                                color = android.graphics.Color.RED
                                style = android.graphics.Paint.Style.FILL
                            }
                            canvas.drawRect(0f, 0f, borderedBitmap.width.toFloat(), borderedBitmap.height.toFloat(), paint)
                            canvas.drawBitmap(resizedBitmap, 4f, 4f, null)
                            return BitmapDescriptorFactory.fromBitmap(borderedBitmap)
                        }

                        return BitmapDescriptorFactory.fromBitmap(resizedBitmap)
                    }
                }
            } catch (e: Exception) {
                // Log the error but don't crash the app
                println("Error creating marker icon: ${e.message}")
            }

            // Return default marker if no image is available
            return BitmapDescriptorFactory.defaultMarker(
                if (isSelected) BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_AZURE
            )
        }
    }
}
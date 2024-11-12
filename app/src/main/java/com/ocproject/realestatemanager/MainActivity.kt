package com.ocproject.realestatemanager

import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
/*

        val projection = arrayOf( //SELECT FIELD
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val millisYesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.timeInMillis

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC" // ORDER BY
        val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?" // WHERE Condition
        val selectionArgs = arrayOf(
            millisYesterday.toString()
        )

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)

            val images = mutableListOf<Images>()
            while (cursor.moveToNext()){

            }
        }
*/


        setContent {
            RealEstateManagerApp(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false,
            )
        }
    }
}


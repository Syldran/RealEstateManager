package com.openclassrooms.realestatemanager.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Property::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("propertyId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class PictureOfProperty(
    val isMain: Boolean = false,
    val uri: String,
//    val description: String,
    val name: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(index = true)
    val propertyId: Int?,
)

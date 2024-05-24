package com.ocproject.realestatemanager.models

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
data class PhotoProperty(
    val isMain: Boolean = false,
    val name: String = "",
    val photoBytes: ByteArray,
    val propertyId: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    )
package com.ocproject.realestatemanager.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(
        entity = PropertyEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("propertyId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class PhotoPropertyEntity(
    val isMain: Boolean = false,
    val name: String = "",
    val photoBytes: ByteArray,
    @ColumnInfo(index = true)
    val propertyId: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    )
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
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoPropertyEntity

        if (isMain != other.isMain) return false
        if (propertyId != other.propertyId) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (!photoBytes.contentEquals(other.photoBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isMain.hashCode()
        result = 31 * result + propertyId.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + photoBytes.contentHashCode()
        return result
    }
}
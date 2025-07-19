package com.ocproject.realestatemanager.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PhotoProperty(
    val isMain: Boolean = false,
    val name: String = "",
    val photoBytes: ByteArray,
    val id: Long = 0,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoProperty

        if (isMain != other.isMain) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (!photoBytes.contentEquals(other.photoBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isMain.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + photoBytes.contentHashCode()
        return result
    }
}
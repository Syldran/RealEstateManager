package com.ocproject.realestatemanager.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PhotoProperty(
    val isMain: Boolean = false,
    val name: String = "",
    val photoBytes: ByteArray,
    val id: Long = 0,
) : Parcelable
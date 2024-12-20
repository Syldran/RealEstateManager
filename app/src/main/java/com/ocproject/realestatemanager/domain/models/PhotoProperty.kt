package com.ocproject.realestatemanager.domain.models




data class PhotoProperty(
    val isMain: Boolean = false,
    val name: String = "",
    val photoBytes: ByteArray,
    val id: Long = 0,
    )
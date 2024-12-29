package com.ocproject.realestatemanager.core.utils

data class Range<T : Comparable<T>>(
    val lower: T,
    val upper: T
) {
    fun contains(value: T): Boolean = value in lower..upper
}
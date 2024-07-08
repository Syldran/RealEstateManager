package com.ocproject.realestatemanager.utils

class UtilsKotlin {


    companion object {

        val integerChars = '0'..'9'
        fun isNumber(input: String): Boolean {
            var dotOccurred = 0
            return input.all { it in integerChars || it == '.' && dotOccurred++ < 1 }
        }

        fun isInteger(input: String) = input.all { it in integerChars }
    }
}
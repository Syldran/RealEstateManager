package com.ocproject.realestatemanager.presentation.scene.addproperty.utils

import java.text.DecimalFormatSymbols
import kotlin.text.isDigit
import kotlin.text.isNotEmpty
import kotlin.text.matches
import kotlin.text.toRegex

class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {

    private val thousandsSeparator = symbols.groupingSeparator
    private val decimalSeparator = symbols.decimalSeparator

    fun cleanup(input: String): String {

        if (input.matches("\\D".toRegex())) return ""
        if (input.matches("0+".toRegex())) return "0"
        if(input == "") return "0"

        val sb = StringBuilder()

        var hasDecimalSep = false

        for (char in input) {
            if (char.isDigit()) {
                sb.append(char)
                continue
            }
            if (char == decimalSeparator && !hasDecimalSep && sb.isNotEmpty()) {
                sb.append(char)
                hasDecimalSep = true
            }
        }

        return sb.toString()
    }
}


class IntFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {

    private val thousandsSeparator = symbols.groupingSeparator
    private val decimalSeparator = symbols.decimalSeparator

    fun cleanup(input: String): String {

        if (input.matches("\\D".toRegex())) return ""
        if (input.matches("0+".toRegex())) return ""
        if(input == "") return "0"

        val sb = StringBuilder()

        for (char in input) {
            if (char.isDigit()) {
                sb.append(char)
                continue
            }
        }

        return sb.toString()
    }
}
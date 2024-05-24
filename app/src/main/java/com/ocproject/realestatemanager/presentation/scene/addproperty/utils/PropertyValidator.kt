package com.ocproject.realestatemanager.presentation.scene.addproperty.utils

import com.ocproject.realestatemanager.models.Property


object PropertyValidator {
    fun validateProperty(property: Property): ValidationResult {
        var result = ValidationResult()
        if (property.address.isBlank()) {
            result = result.copy(addressError = "Address can't be empty.")
        }

        if (property.town.isBlank()) {
            result = result.copy(townError = "Town can't be empty.")
        }

        if (property.lat.isNaN()) {
            result = result.copy(latError = "Latitude can't be empty.")
        }

        if (property.lng.isNaN()) {
            result = result.copy(lngError = "Longitude can't be empty.")
        }

        if (property.surfaceArea == null || property.surfaceArea == 0) {
            result = result.copy(surfaceAreaError = "Surface area can't be empty.")
        }

        if (property.areaCode == null || property.areaCode == 0) {
            result = result.copy(areaCodeError = "Area code can't be empty.")
        }

        if (property.price == null || property.price == 0) {
            result = result.copy(priceError = "Price can't be empty.")
        }

        if (property.country.isBlank()) {
            result = result.copy(countryError = "Country can't be empty.")
        }

        return result
    }

    data class ValidationResult(
        val addressError: String? = null,
        val townError: String? = null,
        val latError: String? = null,
        val lngError: String? = null,
        val surfaceAreaError: String? = null,
        val areaCodeError: String? = null,
        val priceError: String? = null,
        val countryError: String? = null,

        )
}

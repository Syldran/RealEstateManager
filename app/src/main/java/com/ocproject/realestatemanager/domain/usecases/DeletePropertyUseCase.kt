package com.ocproject.realestatemanager.domain.usecases

import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository

class DeletePropertyUseCase(
    private val propertiesRepository: PropertiesRepository
) {
    suspend operator fun invoke(property: Property) {
        propertiesRepository.deleteProperty(property)
    }
}
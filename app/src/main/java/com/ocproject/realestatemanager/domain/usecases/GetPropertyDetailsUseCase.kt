package com.ocproject.realestatemanager.domain.usecases

import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository

class GetPropertyDetailsUseCase(
    private val propertiesRepository: PropertiesRepository
)  {
    suspend operator fun invoke(propertyId: Long): Property {
        return propertiesRepository.getProperty(propertyId)
    }
}
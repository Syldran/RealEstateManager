package com.ocproject.realestatemanager.domain.usecases

import com.ocproject.realestatemanager.domain.models.PropertyWithPhotos
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository

class GetPropertyDetailsUseCase(
    private val propertiesRepository: PropertiesRepository
)  {
    suspend operator fun invoke(propertyId: Long): PropertyWithPhotos {
        return propertiesRepository.getProperty(propertyId)
    }
}
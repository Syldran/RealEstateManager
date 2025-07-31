package com.ocproject.realestatemanager.domain.repositories

import com.ocproject.realestatemanager.domain.models.Property

interface PropertiesRepository {
    suspend fun upsertProperty(property: Property): Long

    suspend fun deleteProperty(property: Property)

    suspend fun getPropertyList(): List<Property>

    suspend fun getProperty(id: Long): Property
}
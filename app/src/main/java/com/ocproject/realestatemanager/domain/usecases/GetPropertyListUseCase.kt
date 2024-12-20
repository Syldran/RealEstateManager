package com.ocproject.realestatemanager.domain.usecases

import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception


class GetPropertyListUseCase(
    private val propertiesRepository: PropertiesRepository
) {
    operator fun invoke(): Flow<DataState<List<Property>>> {
        return flow {
            emit(DataState.Loading(true))
            try {
                val properties = propertiesRepository.getPropertyList()
                emit(DataState.Success(properties))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(false))
            }
        }
    }
}
package com.ocproject.realestatemanager.ui.scenes.propertylist

import com.ocproject.realestatemanager.repositories.PropertyRepository
import com.ocproject.realestatemanager.ui.DataState
import com.ocproject.realestatemanager.ui.UIComponent
import com.openclassrooms.realestatemanager.models.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

class GetPropertyList(
    private val repo : PropertyRepository
) {

    fun execute(): Flow<DataState<List<Property>>> {
        return flow {
            emit(DataState.Loading(true))
            try {
                val propertyList = repo.getPropertyListOrderedByPrice().single()
//                emit(DataState.Success(propertyList.))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(DataState.Error(UIComponent.Toast(e.message ?: "Unknow Error")))
            } finally {
                emit(DataState.Loading(false))
            }
        }
    }
}

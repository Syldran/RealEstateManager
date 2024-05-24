package com.ocproject.realestatemanager.presentation.scene.detailproperty

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.data.repositories.PropertiesRepository
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class PropertyDetailsViewModel(
    @InjectedParam
    private val propertyId: Long?,
    private val propertyRepository: PropertiesRepository,
) : ViewModel() {


    var selectedProperty: PropertyWithPhotos? by mutableStateOf(null)
        private set

    init {
        getProperty()
    }

    private fun getProperty() {
        viewModelScope.launch {
            if(propertyId != null && propertyId != 0L){
                selectedProperty = propertyRepository.getProperty(propertyId)
            }

        }
    }
}

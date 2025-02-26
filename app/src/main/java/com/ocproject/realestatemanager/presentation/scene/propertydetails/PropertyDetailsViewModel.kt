package com.ocproject.realestatemanager.presentation.scene.propertydetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class PropertyDetailsViewModel(
    private val propertyId: Long?,
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
) : ViewModel() {

    var selectedProperty: Property? by mutableStateOf(null)
        private set

    init {
        getProperty()
    }

    private fun getProperty() {
        viewModelScope.launch {
            if(propertyId != null && propertyId >= 0L){
                selectedProperty = getPropertyDetailsUseCase(propertyId)
            }
        }
    }
}

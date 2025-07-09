package com.ocproject.realestatemanager.presentation.scene.propertydetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class PropertyDetailsViewModel(
    private val propertyId: Long,
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
) : ViewModel() {

  /*  var selectedProperty: Property? by mutableStateOf(null)
        private set

    var currentPropertyId: Long by mutableLongStateOf(propertyId)
        private set

    init {
        getProperty()
    }


    fun updatePropertyId(newPropertyId: Long) {
        if (currentPropertyId != newPropertyId) {
            currentPropertyId = newPropertyId
        }
        getProperty()
    }



    private fun getProperty() {
        viewModelScope.launch {
//            if(propertyId != null && propertyId >= 0L){
            selectedProperty = getPropertyDetailsUseCase(currentPropertyId)
            Timber.tag("TEST3").d("${selectedProperty?.photoList?.size}")
//            }
        }
    }
*/

}

package com.ocproject.realestatemanager.presentation.scene.addproperty

import com.ocproject.realestatemanager.data.repositories.PropertiesRepository
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class AddPropertyViewModelTest {
//goal 80% viewModel
    private lateinit var propertiesRepository: PropertiesRepository
    private lateinit var viewModel: AddPropertyViewModel
    @Before
    fun setUp() {
        propertiesRepository = mock()
        viewModel = AddPropertyViewModel(1, propertiesRepository)
    }

  /*  @Test
    fun onEventGetProperty(){
        // test avec id property null et avec un id de property en bdd
    }
    @Test
    fun onEventOnAddressChanged() {


        // simuler un changement d'état et vérifier qu'il change bien
    }
    @Test
    fun onEventOnPriceChanged() {

        // test avec id property null et avec un id de property en bdd
        // simuler un changement d'état et vérifier qu'il change bien
    }*/

    // etc
}
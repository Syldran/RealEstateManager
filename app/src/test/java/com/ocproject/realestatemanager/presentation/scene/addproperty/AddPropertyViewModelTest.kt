package com.ocproject.realestatemanager.presentation.scene.addproperty

import com.ocproject.realestatemanager.data.repositories.LocalPropertiesRepository

import org.junit.Before
import org.mockito.Mockito.mock

class AddPropertyViewModelTest {
//goal 80% viewModel
    private lateinit var propertiesRepository: LocalPropertiesRepository
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
package com.ocproject.realestatemanager.presentation.scene.propertylist



import com.ocproject.realestatemanager.data.repositories.LocalPropertiesRepository
import org.junit.Before

class PropertyListViewModelTest {

    private lateinit var propertiesRepository: LocalPropertiesRepository
    private lateinit var viewModel: PropertyListViewModel

    @Before
    fun setUp() {
        viewModel = PropertyListViewModel(propertiesRepository) // init viewModel for each test
    }


}
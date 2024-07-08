package com.ocproject.realestatemanager.presentation.scene.propertylist



import com.ocproject.realestatemanager.data.repositories.PropertiesRepository
import org.junit.Before

class PropertyListViewModelTest {

    private lateinit var viewModel: PropertyListViewModel

    @Before
    fun setUp() {
        viewModel = PropertyListViewModel(propertiesRepository = PropertiesRepository()) // init viewModel for each test
    }
}
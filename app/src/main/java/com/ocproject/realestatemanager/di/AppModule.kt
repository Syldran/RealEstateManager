package com.ocproject.realestatemanager.di


import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyViewModel
import com.ocproject.realestatemanager.presentation.scene.funding.FundingViewModel
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsViewModel
import com.ocproject.realestatemanager.presentation.scene.map.MapOfPropertiesViewModel
import com.ocproject.realestatemanager.presentation.scene.propertydetails.PropertyDetailsViewModel
import org.koin.core.module.dsl.*

val appModule = org.koin.dsl.module {


    factory<GetPropertyListUseCase> {
      GetPropertyListUseCase(
            propertiesRepository = get()
        )
    }

    factory<SavePropertyUseCase> {
        SavePropertyUseCase(
            propertiesRepository = get()
        )
    }


    factory<GetPropertyDetailsUseCase> {
        GetPropertyDetailsUseCase(
            propertiesRepository = get()
        )
    }

    factory<DeletePropertyUseCase> {
        DeletePropertyUseCase(
            propertiesRepository = get()
        )
    }



    viewModel {
        AddPropertyViewModel(
            propertyId = get(),
            getPropertyDetailsUseCase = get(),
            savePropertyUseCase = get(),
        )
    }


    viewModel {
        PropertyDetailsViewModel(
            getPropertyDetailsUseCase = get(),
            propertyId = get()
        )
    }

    viewModel {
        ListDetailsViewModel(
            getPropertyListUseCase = get(),
            deletePropertyUseCase = get(),
        )
    }

    viewModel {
        MapOfPropertiesViewModel(
            getPropertyListUseCase = get(),
        )
    }
    viewModel {
        FundingViewModel(
        )
    }
}
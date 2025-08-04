package com.ocproject.realestatemanager.di


import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListFilteredUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyViewModel
import com.ocproject.realestatemanager.presentation.scene.funding.FundingViewModel
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsViewModel
import com.ocproject.realestatemanager.presentation.scene.map.MapOfPropertiesViewModel
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val appModule = module {


    factory<GetPropertyListUseCase> {
      GetPropertyListUseCase(
            propertiesRepository = get()
        )
    }

    factory<GetPropertyListFilteredUseCase> {
        GetPropertyListFilteredUseCase(
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
        ListDetailsViewModel(
            getPropertyListUseCase = get(),
            getPropertyListFilteredUseCase = get(),
            getPropertyDetailsUseCase = get(),
            deletePropertyUseCase = get(),
        )
    }

    viewModel {
        MapOfPropertiesViewModel()
    }
    viewModel {
        FundingViewModel()
    }
}
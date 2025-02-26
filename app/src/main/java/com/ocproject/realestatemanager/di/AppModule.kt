package com.ocproject.realestatemanager.di


import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyViewModel
import com.ocproject.realestatemanager.presentation.scene.propertydetails.PropertyDetailsViewModel
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListViewModel
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

//    factory<SavePhotoProperty> {
//        SavePhotoProperty(
//            propertiesRepository = get()
//        )
//    }

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

//    viewModel { (propertyId: Long) ->
//        PropertyDetailsViewModel(
//            getPropertyDetailsUseCase = get(),
//            propertyId = propertyId
//        )
//    }
    viewModel {
        PropertyDetailsViewModel(
            getPropertyDetailsUseCase = get(),
            propertyId = get()
        )
    }

    viewModel {
        PropertyListViewModel(
            getPropertyListUseCase = get(),
            deletePropertyUseCase = get(),
        )
    }
}
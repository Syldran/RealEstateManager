package com.ocproject.realestatemanager.presentation.scene.map

import androidx.lifecycle.ViewModel
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class MapOfPropertiesViewModel(
    @InjectedParam
    private val getPropertyListUseCase: GetPropertyListUseCase,
) : ViewModel() {

}
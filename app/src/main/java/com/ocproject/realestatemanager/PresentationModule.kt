package com.ocproject.realestatemanager

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.ocproject.realestatemanager.ui")
class ViewModelModule

val presentationModule = listOf(
    ViewModelModule().module
)
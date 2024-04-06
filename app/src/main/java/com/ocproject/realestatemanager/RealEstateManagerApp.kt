package com.ocproject.realestatemanager

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level


class RealEstateManagerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.ERROR)
            androidContext(this@RealEstateManagerApp)
            modules(dataModule)
            modules(presentationModule)
        }
    }
}
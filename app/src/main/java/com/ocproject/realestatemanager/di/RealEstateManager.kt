package com.ocproject.realestatemanager.di

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.ocproject.realestatemanager.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level
import timber.log.Timber

class RealEstateManager: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        GlobalContext.startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@RealEstateManager)
            modules(dataModule, appModule)
//            modules(presentationModule)
        }
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(
                this,
                BuildConfig.PLACES_API_KEY
            )
        }
    }
}
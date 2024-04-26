package com.ocproject.realestatemanager

import androidx.room.Room
import com.ocproject.realestatemanager.data.db.PropertyDatabase
import com.ocproject.realestatemanager.data.repositories.PropertyRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            PropertyDatabase::class.java,
            "property.db"
        ).build() //change back to not allowing queries in main thread
    }

    single {
        get<PropertyDatabase>().dao
    }

    singleOf(::PropertyRepository)
}

package com.ocproject.realestatemanager.di

import androidx.room.Room
import com.ocproject.realestatemanager.data.database.PropertiesDatabase
import com.ocproject.realestatemanager.data.repositories.PropertiesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            PropertiesDatabase::class.java,
            "properties.db"
        ).build()
    }

    single {
        get<PropertiesDatabase>().dao
    }

    singleOf(::PropertiesRepository)

}

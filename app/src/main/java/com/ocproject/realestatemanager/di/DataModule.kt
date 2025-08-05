package com.ocproject.realestatemanager.di

import androidx.room.Room
import com.ocproject.realestatemanager.data.database.PropertiesDatabase
import com.ocproject.realestatemanager.data.repositories.LocalPropertiesRepository
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            PropertiesDatabase::class.java,
            "rem_database.db"
        ).build()
    }

    single {
        get<PropertiesDatabase>().dao
    }

    single<PropertiesRepository> { LocalPropertiesRepository(get()) }
}

val testDatabaseModule = module {
    single {
        Room.inMemoryDatabaseBuilder(
            get(),
            PropertiesDatabase::class.java,
        )
            .allowMainThreadQueries() // Only for testing
            .build()
    }

    single { get<PropertiesDatabase>().dao }

    single<PropertiesRepository> { LocalPropertiesRepository(get()) }
}
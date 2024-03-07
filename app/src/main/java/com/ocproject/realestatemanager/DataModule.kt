package com.ocproject.realestatemanager

import androidx.room.Room
import com.ocproject.realestatemanager.db.PropertyDatabase
import org.koin.android.ext.koin.androidContext
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
}

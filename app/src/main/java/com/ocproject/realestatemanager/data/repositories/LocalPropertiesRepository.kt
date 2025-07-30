package com.ocproject.realestatemanager.data.repositories

import android.app.Application
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.test.core.app.ActivityScenario.launch
import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.data.entities.PhotoPropertyEntity
import com.ocproject.realestatemanager.data.toPhotoPropertyEntity
import com.ocproject.realestatemanager.data.toProperty
import com.ocproject.realestatemanager.data.toPropertyEntity
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import org.koin.compose.getKoin
import timber.log.Timber
import kotlin.coroutines.coroutineContext


class LocalPropertiesRepository(
    private val dao: PropertiesDao,
) : PropertiesRepository {
    override suspend fun upsertProperty(property: Property): Long {
        var propertyToAdd:Long = dao.upsertProperty(property.toPropertyEntity())
        // upsert return -1 for replacing existing data
        if (propertyToAdd < 0L) {
            propertyToAdd = property.id
        }
        dao.deletePicturesOfPropertyByIdProperty(propertyToAdd)
        property.photoList.forEach {
            runBlocking {
                async {
                    dao.upsertPhoto(it.toPhotoPropertyEntity(propertyToAdd))
                }
            }
        }



        return propertyToAdd
    }


    override suspend fun deleteProperty(property: Property) {
        val propertyToDelete = property.toPropertyEntity()
        dao.deleteProperty(propertyToDelete)
    }

    override suspend fun getPropertyList(): List<Property> {
        val properties:List<Property> = dao.getPropertyList().map { it.toProperty() }
        return properties
    }

    override suspend fun getProperty(id: Long): Property {
        val propertyDetails = dao.getPropertyDetail(id)
        return propertyDetails.toProperty()
    }
}
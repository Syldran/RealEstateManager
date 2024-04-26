package com.ocproject.realestatemanager.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.ocproject.realestatemanager.models.PropertyWithPictures
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import com.openclassrooms.realestatemanager.models.Property
import kotlinx.coroutines.flow.Flow


@Dao
interface PropertyDao {

    @Upsert
    suspend fun upsertProperty(property: Property) : Long

    @Upsert
    suspend fun upsertPictureofProperty(pictureOfProperty: PictureOfProperty)

    @Delete
    suspend fun deleteProperty(property: Property)

    @Query("select * from PictureOfProperty WHERE propertyId = :selectedId")
    fun getPicturesOfProperty(selectedId: Int): Flow<List<PictureOfProperty>>

    @Query("SELECT * FROM property ORDER by price ASC")
    fun getPropertiesOrderedByPrice() : Flow<List<PropertyWithPictures>>

    @Query("SELECT * FROM property WHERE id = :selectedId")
    suspend fun getPropertyDetails(selectedId: Int) : PropertyWithPictures
}
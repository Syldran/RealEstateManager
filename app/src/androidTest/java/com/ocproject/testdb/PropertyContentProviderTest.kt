package com.ocproject.testdb

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.data.database.PropertiesDatabase
import com.ocproject.realestatemanager.data.database.PropertyContentProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class PropertyContentProviderTest {

    private lateinit var propertiesDao: PropertiesDao
    private lateinit var db: PropertiesDatabase
    private lateinit var mContentResolver: ContentResolver

    // DATA SET FOR TEST
    private val PROPERTY_ID: Long = 1
    private val PHOTO_ID: Long = 10

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            PropertiesDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        propertiesDao = db.dao
        mContentResolver =
            InstrumentationRegistry.getInstrumentation().targetContext.contentResolver
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getPropertiesWhenNoPropertyInserted() {
        // select all table property
        val cursor = mContentResolver.query(
            ContentUris.withAppendedId(
                PropertyContentProvider.CONTENT_URI_PROPERTIES,
                PROPERTY_ID
            ), null, null, null, null
        )

        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(0))
        cursor.close()
    }

    @Test
    fun insertAndGetProperty() {
        // Insert a property
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }

        val insertedUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(insertedUri, notNullValue())

        // Query the inserted property
        val cursor = mContentResolver.query(insertedUri!!, null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))

        cursor.moveToFirst()
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_DESCRIPTION)),
            `is`("Test Property")
        )
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_ADDRESS)),
            `is`("123 Test Street")
        )
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_TOWN)),
            `is`("Test Town")
        )
        assertThat(
            cursor.getDouble(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_LAT)),
            `is`(40.7128)
        )
        assertThat(
            cursor.getDouble(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_LNG)),
            `is`(-74.0060)
        )
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_COUNTRY)),
            `is`("USA")
        )
        assertThat(
            cursor.getInt(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_AREA_CODE)),
            `is`(10001)
        )
        assertThat(
            cursor.getInt(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_SURFACE_AREA)),
            `is`(150)
        )
        assertThat(
            cursor.getInt(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_PRICE)),
            `is`(500000)
        )

        cursor.close()
    }

    @Test
    fun insertAndGetPhoto() {
        // Property values
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }
        // Insert property values
        val propertyUri: Uri? =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(propertyUri, notNullValue())

        // Photo values
        val photoBytes = "test photo data".toByteArray()
        val photoValues = ContentValues().apply {
            put(PropertyContentProvider.PHOTO_IS_MAIN, true)
            put(PropertyContentProvider.PHOTO_NAME, "test_photo.jpg")
            put(PropertyContentProvider.PHOTO_BYTES, photoBytes)
            put(PropertyContentProvider.PHOTO_PROPERTY_ID, ContentUris.parseId(propertyUri!!))
        }
        // Insert photo values
        val insertedPhotoUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PHOTOS, photoValues)
        assertThat(insertedPhotoUri, notNullValue())

        // Query the inserted photo
        val cursor = mContentResolver.query(insertedPhotoUri!!, null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))

        cursor.moveToFirst()
        assertThat(
            cursor.getInt(cursor.getColumnIndexOrThrow(PropertyContentProvider.PHOTO_IS_MAIN)),
            `is`(1)
        )
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow(PropertyContentProvider.PHOTO_NAME)),
            `is`("test_photo.jpg")
        )
        assertThat(
            cursor.getBlob(cursor.getColumnIndexOrThrow(PropertyContentProvider.PHOTO_BYTES)),
            `is`(photoBytes)
        )
        propertyUri?.let {
            assertThat(
                cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                        PropertyContentProvider.PHOTO_PROPERTY_ID
                    )
                ), `is`(ContentUris.parseId(it))
            )
        }

        cursor.close()
    }

    @Test
    fun updateProperty() {
        // Insert a property
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Original Description")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }
        // Insert values in data properties
        val insertedUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(insertedUri, notNullValue())

        // Value to update
        val updateValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Updated Description")
            put(PropertyContentProvider.PROPERTY_PRICE, 600000)
        }
        // Update values in data properties
        val updatedRows = mContentResolver.update(insertedUri!!, updateValues, null, null)
        assertThat(updatedRows, `is`(1))

        // Query the updated property
        val cursor = mContentResolver.query(insertedUri, null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))

        // First row of data properties
        cursor.moveToFirst()
        // Check updated value are in data properties
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_DESCRIPTION)),
            `is`("Updated Description")
        )
        assertThat(
            cursor.getInt(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_PRICE)),
            `is`(600000)
        )

        cursor.close()
    }

    @Test
    fun updatePhoto() {
        // Database scheme force a photo to have an existing property.

        // Values of a property to insert
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }
        // Insert property values
        val propertyUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(propertyUri, notNullValue())

        // Photo values to insert
        val photoBytes = "original photo data".toByteArray()
        val photoValues = ContentValues().apply {
            put(PropertyContentProvider.PHOTO_IS_MAIN, false)
            put(PropertyContentProvider.PHOTO_NAME, "original_photo.jpg")
            put(PropertyContentProvider.PHOTO_BYTES, photoBytes)
            put(PropertyContentProvider.PHOTO_PROPERTY_ID, ContentUris.parseId(propertyUri!!))
        }
        // Insert Photo values.
        val insertedPhotoUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PHOTOS, photoValues)
        assertThat(insertedPhotoUri, notNullValue())

        // Photo values to update
        val updatePhotoBytes = "updated photo data".toByteArray()
        val updatePhotoValues = ContentValues().apply {
            put(PropertyContentProvider.PHOTO_IS_MAIN, true)
            put(PropertyContentProvider.PHOTO_NAME, "updated_photo.jpg")
            put(PropertyContentProvider.PHOTO_BYTES, updatePhotoBytes)
        }
        // Update data photos
        val updatedRows = mContentResolver.update(insertedPhotoUri!!, updatePhotoValues, null, null)
        assertThat(updatedRows, `is`(1))

        // Query the updated photo
        val cursor = mContentResolver.query(insertedPhotoUri, null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))

        cursor.moveToFirst()
        assertThat(
            cursor.getInt(cursor.getColumnIndexOrThrow(PropertyContentProvider.PHOTO_IS_MAIN)),
            `is`(1)
        )
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow(PropertyContentProvider.PHOTO_NAME)),
            `is`("updated_photo.jpg")
        )
        assertThat(
            cursor.getBlob(cursor.getColumnIndexOrThrow(PropertyContentProvider.PHOTO_BYTES)),
            `is`(updatePhotoBytes)
        )

        cursor.close()
    }

    @Test
    fun deleteProperty() {
        // Property value to insert
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }
        // Insert property
        val insertedUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(insertedUri, notNullValue())

        // Delete property
        val deletedRows = mContentResolver.delete(insertedUri!!, null, null)
        assertThat(deletedRows, `is`(1))

        // Verify property is deleted
        val cursor = mContentResolver.query(insertedUri, null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(0))
        cursor.close()
    }

    @Test
    fun deletePhoto() {
        // Property values
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }
        // Insert property
        val propertyUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(propertyUri, notNullValue())

        // Photo values
        val photoBytes = "test photo data".toByteArray()
        val photoValues = ContentValues().apply {
            put(PropertyContentProvider.PHOTO_IS_MAIN, true)
            put(PropertyContentProvider.PHOTO_NAME, "test_photo.jpg")
            put(PropertyContentProvider.PHOTO_BYTES, photoBytes)
            put(PropertyContentProvider.PHOTO_PROPERTY_ID, ContentUris.parseId(propertyUri!!))
        }
        // Insert photo
        val insertedPhotoUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PHOTOS, photoValues)
        assertThat(insertedPhotoUri, notNullValue())

        // Delete the photo
        val deletedRows = mContentResolver.delete(insertedPhotoUri!!, null, null)
        assertThat(deletedRows, `is`(1))

        // Verify the photo is deleted
        val cursor = mContentResolver.query(insertedPhotoUri, null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(0))
        cursor.close()
    }

    @Test
    fun getTypeForProperties() {
        val propertyType = mContentResolver.getType(PropertyContentProvider.CONTENT_URI_PROPERTIES)
        assertThat(propertyType, `is`("vnd.android.cursor.dir/property"))

        val propertyItemType = mContentResolver.getType(
            ContentUris.withAppendedId(PropertyContentProvider.CONTENT_URI_PROPERTIES, PROPERTY_ID)
        )
        assertThat(propertyItemType, `is`("vnd.android.cursor.item/property"))
    }

    @Test
    fun getTypeForPhotos() {
        val photoType = mContentResolver.getType(PropertyContentProvider.CONTENT_URI_PHOTOS)
        assertThat(photoType, `is`("vnd.android.cursor.dir/photo"))

        val photoItemType = mContentResolver.getType(
            ContentUris.withAppendedId(PropertyContentProvider.CONTENT_URI_PHOTOS, PHOTO_ID)
        )
        assertThat(photoItemType, `is`("vnd.android.cursor.item/photo"))
    }

    @Test
    fun queryAllProperties() {
        // Insert multiple properties
        val property1Values = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Property 1")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Street 1")
            put(PropertyContentProvider.PROPERTY_TOWN, "Town 1")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }

        val property2Values = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Property 2")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "456 Street 2")
            put(PropertyContentProvider.PROPERTY_TOWN, "Town 2")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7589)
            put(PropertyContentProvider.PROPERTY_LNG, -73.9851)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10002)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 200)
            put(PropertyContentProvider.PROPERTY_PRICE, 750000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }

        mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, property1Values)
        mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, property2Values)

        // Query all properties
        val cursor = mContentResolver.query(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            null,
            null,
            null,
            null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(2))

        cursor.close()
    }

    @Test
    fun queryAllPhotos() {
        // First insert a property
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }

        val propertyUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(propertyUri, notNullValue())
        val propertyId = ContentUris.parseId(propertyUri!!)

        // Insert multiple photos
        val photo1Bytes = "photo 1 data".toByteArray()
        val photo1Values = ContentValues().apply {
            put(PropertyContentProvider.PHOTO_IS_MAIN, true)
            put(PropertyContentProvider.PHOTO_NAME, "photo1.jpg")
            put(PropertyContentProvider.PHOTO_BYTES, photo1Bytes)
            put(PropertyContentProvider.PHOTO_PROPERTY_ID, propertyId)
        }

        val photo2Bytes = "photo 2 data".toByteArray()
        val photo2Values = ContentValues().apply {
            put(PropertyContentProvider.PHOTO_IS_MAIN, false)
            put(PropertyContentProvider.PHOTO_NAME, "photo2.jpg")
            put(PropertyContentProvider.PHOTO_BYTES, photo2Bytes)
            put(PropertyContentProvider.PHOTO_PROPERTY_ID, propertyId)
        }

        mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PHOTOS, photo1Values)
        mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PHOTOS, photo2Values)

        // Query all photos
        val cursor = mContentResolver.query(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            null,
            null,
            null,
            null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(2))

        cursor.close()
    }

    @Test
    fun insertPropertyWithDefaultValues() {
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            // areaCode is intentionally not provided, should default to 0
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }

        val insertedUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(insertedUri, notNullValue())

        // Query the property to verify default values are handled correctly
        val cursor = mContentResolver.query(insertedUri!!, null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))

        cursor.moveToFirst()
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_DESCRIPTION)),
            `is`("Test Property")
        )
        assertThat(
            cursor.getInt(cursor.getColumnIndexOrThrow(PropertyContentProvider.PROPERTY_AREA_CODE)),
            `is`(0)
        )

        cursor.close()
    }

    @Test
    fun deleteNonExistentProperty() {
        val nonExistentUri =
            ContentUris.withAppendedId(PropertyContentProvider.CONTENT_URI_PROPERTIES, 999L)
        val deletedRows = mContentResolver.delete(nonExistentUri, null, null)
        assertThat(deletedRows, `is`(0))
    }

    @Test
    fun deleteNonExistentPhoto() {
        val nonExistentUri =
            ContentUris.withAppendedId(PropertyContentProvider.CONTENT_URI_PHOTOS, 999L)
        val deletedRows = mContentResolver.delete(nonExistentUri, null, null)
        assertThat(deletedRows, `is`(0))
    }

    @Test
    fun updateNonExistentProperty() {
        val nonExistentUri =
            ContentUris.withAppendedId(PropertyContentProvider.CONTENT_URI_PROPERTIES, 999L)
        val updateValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Updated Description")
        }

        val updatedRows = mContentResolver.update(nonExistentUri, updateValues, null, null)
        assertThat(updatedRows, `is`(0))
    }

    @Test
    fun updateNonExistentPhoto() {
        val nonExistentUri =
            ContentUris.withAppendedId(PropertyContentProvider.CONTENT_URI_PHOTOS, 999L)
        val updateValues = ContentValues().apply {
            put(PropertyContentProvider.PHOTO_NAME, "updated_photo.jpg")
        }

        val updatedRows = mContentResolver.update(nonExistentUri, updateValues, null, null)
        assertThat(updatedRows, `is`(0))
    }

    @Test
    fun insertPropertyWithNullContentValues() {
        val insertedUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, null)
        assertThat(insertedUri, null)
    }

    @Test
    fun insertPhotoWithNullContentValues() {
        val insertedUri = mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PHOTOS, null)
        assertThat(insertedUri, null)
    }

    @Test
    fun updatePropertyWithNullContentValues() {
        // Insert a property first
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }

        val insertedUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(insertedUri, notNullValue())

        // Try to update with null values
        val updatedRows = mContentResolver.update(insertedUri!!, null, null, null)
        assertThat(updatedRows, `is`(0))
    }

    @Test
    fun updatePhotoWithNullContentValues() {
        // First insert a property
        val propertyValues = ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 40.7128)
            put(PropertyContentProvider.PROPERTY_LNG, -74.0060)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "USA")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 10001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 150)
            put(PropertyContentProvider.PROPERTY_PRICE, 500000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
        }

        val propertyUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PROPERTIES, propertyValues)
        assertThat(propertyUri, notNullValue())

        // Insert a photo
        val photoBytes = "test photo data".toByteArray()
        val photoValues = ContentValues().apply {
            put(PropertyContentProvider.PHOTO_IS_MAIN, true)
            put(PropertyContentProvider.PHOTO_NAME, "test_photo.jpg")
            put(PropertyContentProvider.PHOTO_BYTES, photoBytes)
            put(PropertyContentProvider.PHOTO_PROPERTY_ID, ContentUris.parseId(propertyUri!!))
        }

        val insertedPhotoUri =
            mContentResolver.insert(PropertyContentProvider.CONTENT_URI_PHOTOS, photoValues)
        assertThat(insertedPhotoUri, notNullValue())

        // Try to update with null values
        val updatedRows = mContentResolver.update(insertedPhotoUri!!, null, null, null)
        assertThat(updatedRows, `is`(0))
    }
}
package com.ocproject.testdb

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import androidx.core.net.toUri
import androidx.test.platform.app.InstrumentationRegistry
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.data.database.PropertyContentProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test

class PropertyContentProviderTest {

    private lateinit var mContentResolver: ContentResolver

    // DATA SET FOR TEST
    private var propertyId: Long = 0
    private var photoId: Long = 0

    @Before
    fun setUp() {
        mContentResolver =
            InstrumentationRegistry.getInstrumentation().targetContext.contentResolver
    }

    @After
    fun tearDown() {
        // Clean up any test data
        if (propertyId > 0) {
            val propertyUri = ContentUris.withAppendedId(
                PropertyContentProvider.CONTENT_URI_PROPERTIES,
                propertyId
            )
            mContentResolver.delete(propertyUri, null, null)
        }
        if (photoId > 0) {
            val photoUri = ContentUris.withAppendedId(
                PropertyContentProvider.CONTENT_URI_PHOTOS,
                photoId
            )
            mContentResolver.delete(photoUri, null, null)
        }
    }

    // ===== PROPERTY TESTS =====

    @Test
    fun testGetProperties() {
        // Given: Insert a property into the database via ContentProvider
        val values = createPropertyContentValues()
        val uri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            values
        )
        propertyId = ContentUris.parseId(uri!!)

        // When: Query all properties
        val cursor = mContentResolver.query(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            null,
            null,
            null,
            null
        )

        // Then: Verify cursor is not null and contains data
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(greaterThanOrEqualTo(1)))
        cursor.close()
    }

    @Test
    fun testGetPropertyById() {
        // Given: Insert a property into the database via ContentProvider
        val values = createPropertyContentValues()
        val uri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            values
        )
        propertyId = ContentUris.parseId(uri!!)

        // When: Query property by ID
        val propertyUri = ContentUris.withAppendedId(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            propertyId
        )
        val cursor = mContentResolver.query(propertyUri, null, null, null, null)

        // Then: Verify cursor is not null and contains the property
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        cursor.close()
    }

    @Test
    fun testInsertProperty() {
        // Given: Create ContentValues for a property
        val values = createPropertyContentValues()

        // When: Insert property
        val uri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            values
        )

        // Then: Verify URI is not null and contains a valid ID
        assertThat(uri, notNullValue())
        val insertedId = ContentUris.parseId(uri!!)
        assertThat(insertedId, `is`(notNullValue()))
        assertThat(insertedId, `is`(greaterThan(0L)))
        propertyId = insertedId
    }

    @Test
    fun testUpdateProperty() {
        // Given: Insert a property via ContentProvider
        val values = createPropertyContentValues()
        val uri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            values
        )
        propertyId = ContentUris.parseId(uri!!)

        // Create updated values
        val updatedValues = createPropertyContentValues().apply {
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Updated Description")
            put(PropertyContentProvider.PROPERTY_PRICE, 250000)
        }

        // When: Update the property
        val propertyUri = ContentUris.withAppendedId(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            propertyId
        )
        val updatedRows = mContentResolver.update(propertyUri, updatedValues, null, null)

        // Then: Verify one row was updated
        assertThat(updatedRows, `is`(1))
    }

    @Test
    fun testDeleteProperty() {
        // Given: Insert a property via ContentProvider
        val values = createPropertyContentValues()
        val uri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            values
        )
        propertyId = ContentUris.parseId(uri!!)

        // When: Delete the property
        val propertyUri = ContentUris.withAppendedId(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            propertyId
        )
        val deletedRows = mContentResolver.delete(propertyUri, null, null)

        // Then: Verify one row was deleted
        assertThat(deletedRows, `is`(1))
        propertyId = 0 // Reset since it's deleted
    }

    @Test
    fun testGetPropertyType() {
        // Test MIME type for properties list
        val listType = mContentResolver.getType(PropertyContentProvider.CONTENT_URI_PROPERTIES)
        assertThat(listType, `is`("vnd.android.cursor.dir/property"))

        // Test MIME type for single property
        val uri = ContentUris.withAppendedId(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            1L
        )
        val itemType = mContentResolver.getType(uri)
        assertThat(itemType, `is`("vnd.android.cursor.item/property"))
    }

    // ===== PHOTO TESTS =====

    @Test
    fun testGetPhotos() {
        // Given: Insert a property first, then a photo via ContentProvider
        val propertyValues = createPropertyContentValues()
        val propertyUri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            propertyValues
        )
        propertyId = ContentUris.parseId(propertyUri!!)

        val photoValues = createPhotoContentValues(propertyId)
        val photoUri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            photoValues
        )
        photoId = ContentUris.parseId(photoUri!!)

        // When: Query all photos
        val cursor = mContentResolver.query(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            null,
            null,
            null,
            null
        )

        // Then: Verify cursor is not null and contains data
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(greaterThanOrEqualTo(1)))
        cursor.close()
    }

    @Test
    fun testGetPhotoById() {
        // Given: Insert a property first, then a photo via ContentProvider
        val propertyValues = createPropertyContentValues()
        val propertyUri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            propertyValues
        )
        propertyId = ContentUris.parseId(propertyUri!!)

        val photoValues = createPhotoContentValues(propertyId)
        val photoUri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            photoValues
        )
        photoId = ContentUris.parseId(photoUri!!)

        // When: Query photo by ID
        val queryPhotoUri = ContentUris.withAppendedId(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            photoId
        )
        val cursor = mContentResolver.query(queryPhotoUri, null, null, null, null)

        // Then: Verify cursor is not null and contains the photo
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        cursor.close()
    }

    @Test
    fun testInsertPhoto() {
        // Given: Insert a property first, then create ContentValues for a photo
        val propertyValues = createPropertyContentValues()
        val propertyUri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            propertyValues
        )
        propertyId = ContentUris.parseId(propertyUri!!)
        val values = createPhotoContentValues(propertyId)

        // When: Insert photo
        val uri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            values
        )

        // Then: Verify URI is not null and contains a valid ID
        assertThat(uri, notNullValue())
        val insertedId = ContentUris.parseId(uri!!)
        assertThat(insertedId, `is`(notNullValue()))
        assertThat(insertedId, `is`(greaterThan(0L)))
        photoId = insertedId
    }

    @Test
    fun testUpdatePhoto() {
        // Given: Insert a property first, then a photo via ContentProvider
        val propertyValues = createPropertyContentValues()
        val propertyUri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            propertyValues
        )
        propertyId = ContentUris.parseId(propertyUri!!)

        val photoValues = createPhotoContentValues(propertyId)
        val photoUri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            photoValues
        )
        photoId = ContentUris.parseId(photoUri!!)

        val updatedValues = createPhotoContentValues(propertyId).apply {
            put(PropertyContentProvider.PHOTO_NAME, "Updated Photo")
            put(PropertyContentProvider.PHOTO_IS_MAIN, true)
        }

        // When: Update the photo
        val updatePhotoUri = ContentUris.withAppendedId(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            photoId
        )
        val updatedRows = mContentResolver.update(updatePhotoUri, updatedValues, null, null)

        // Then: Verify one row was updated
        assertThat(updatedRows, `is`(1))
    }

    @Test
    fun testDeletePhoto() {
        // Given: Insert a property first, then a photo via ContentProvider
        val propertyValues = createPropertyContentValues()
        val propertyUri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            propertyValues
        )
        propertyId = ContentUris.parseId(propertyUri!!)

        val photoValues = createPhotoContentValues(propertyId)
        val photoUri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            photoValues
        )
        photoId = ContentUris.parseId(photoUri!!)

        // When: Delete the photo
        val deletePhotoUri = ContentUris.withAppendedId(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            photoId
        )
        val deletedRows = mContentResolver.delete(deletePhotoUri, null, null)

        // Then: Verify one row was deleted
        assertThat(deletedRows, `is`(1))
        photoId = 0 // Reset since it's deleted
    }

    @Test
    fun testGetPhotoType() {
        // Test MIME type for photos list
        val listType = mContentResolver.getType(PropertyContentProvider.CONTENT_URI_PHOTOS)
        assertThat(listType, `is`("vnd.android.cursor.dir/photo"))

        // Test MIME type for single photo
        val uri = ContentUris.withAppendedId(
            PropertyContentProvider.CONTENT_URI_PHOTOS,
            1L
        )
        val itemType = mContentResolver.getType(uri)
        assertThat(itemType, `is`("vnd.android.cursor.item/photo"))
    }

    // ===== ERROR HANDLING TESTS =====

    @Test
    fun testQueryWithInvalidUri() {
        // When: Query with invalid URI
        val invalidUri = "content://invalid.authority/invalid".toUri()
        val cursor = mContentResolver.query(invalidUri, null, null, null, null)

        // Then: Verify cursor is null
        assertThat(cursor, nullValue())
    }

    @Test
    fun testInsertWithNullValues() {
        // When: Insert with null values
        val uri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            null
        )

        // Then: Verify URI is null
        assertThat(uri, nullValue())
    }

    @Test
    fun testUpdateWithNullValues() {
        // Given: Insert a property via ContentProvider
        val values = createPropertyContentValues()
        val uri = mContentResolver.insert(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            values
        )
        propertyId = ContentUris.parseId(uri!!)

        // When: Update with null values
        val propertyUri = ContentUris.withAppendedId(
            PropertyContentProvider.CONTENT_URI_PROPERTIES,
            propertyId
        )
        val updatedRows = mContentResolver.update(propertyUri, null, null, null)

        // Then: Verify no rows were updated
        assertThat(updatedRows, `is`(0))
    }

    // ===== HELPER METHODS =====

    private fun createPropertyContentValues(): ContentValues {
        return ContentValues().apply {
            put(PropertyContentProvider.PROPERTY_INTEREST_POINTS, "SCHOOL,PARK")
            put(PropertyContentProvider.PROPERTY_DESCRIPTION, "Test Property")
            put(PropertyContentProvider.PROPERTY_ADDRESS, "123 Test Street")
            put(PropertyContentProvider.PROPERTY_TOWN, "Test Town")
            put(PropertyContentProvider.PROPERTY_LAT, 48.8566)
            put(PropertyContentProvider.PROPERTY_LNG, 2.3522)
            put(PropertyContentProvider.PROPERTY_COUNTRY, "France")
            put(PropertyContentProvider.PROPERTY_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.PROPERTY_AREA_CODE, 75001)
            put(PropertyContentProvider.PROPERTY_SURFACE_AREA, 100)
            put(PropertyContentProvider.PROPERTY_PRICE, 200000)
            put(PropertyContentProvider.PROPERTY_SOLD_DATE, 0L)
            put(PropertyContentProvider.PROPERTY_TYPE, "House")
            put(PropertyContentProvider.PROPERTY_NBR_ROOM, 3)
            put(PropertyContentProvider.PROPERTY_REAL_ESTATE_AGENT, "Test Agent")
        }
    }

    private fun createPhotoContentValues(propertyId: Long): ContentValues {
        return ContentValues().apply {
            put(PropertyContentProvider.PHOTO_IS_MAIN, true)
            put(PropertyContentProvider.PHOTO_NAME, "Test Photo")
            put(PropertyContentProvider.PHOTO_BYTES, "test photo bytes".toByteArray())
            put(PropertyContentProvider.PHOTO_PROPERTY_ID, propertyId)
        }
    }
}
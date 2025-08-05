package com.ocproject.testdb

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ocproject.realestatemanager.data.database.PropertyContentProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PropertyContentProviderTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        // Clean base before each test
        context = ApplicationProvider.getApplicationContext()
        context.contentResolver.delete(PropertyContentProvider.URI_PHOTO, null, null)
        context.contentResolver.delete(PropertyContentProvider.URI_PROPERTY, null, null)
    }

    @After
    fun cleanup() {
        // Clean base after each test
        context.contentResolver.delete(PropertyContentProvider.URI_PHOTO, null, null)
        context.contentResolver.delete(PropertyContentProvider.URI_PROPERTY, null, null)
    }

    @Test
    fun insert_and_query_propertyEntity_should_succeed() {
        // GIVEN: property values to insert
        val contentValues = ContentValues().apply {
            put("description", "Luminous Apartment")
            put("address", "123 Rue de Paris")
            put("town", "Paris")
            put("lat", 48.8566)
            put("lng", 2.3522)
            put("country", "France")
            put("createdDate", System.currentTimeMillis())
            put("areaCode", 75000)
            put("surfaceArea", 65)
            put("price", 500000)
            put("soldDate", 0L)
            put("type", "Appartement")
            put("nbrRoom", 3)
            put("realEstateAgent", "Martin Dupont")
            put("interestPoints", "SCHOOL,PARK")
        }

        // WHEN: ContentProvider Insert
        val insertedUri: Uri? = context.contentResolver.insert(
            PropertyContentProvider.URI_PROPERTY,
            contentValues
        )
        assertNotNull("URI inserted should not be null", insertedUri)

        // AND: we call for the inserted property
        val cursor: Cursor? = context.contentResolver.query(
            insertedUri!!,
            null,
            null,
            null,
            null
        )

        // THEN: Check data are correct.
        assertNotNull("cursor should not be null", cursor)
        assertTrue("cursor should have one line", cursor!!.moveToFirst())

        val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
        val town = cursor.getString(cursor.getColumnIndexOrThrow("town"))
        val surface = cursor.getInt(cursor.getColumnIndexOrThrow("surfaceArea"))
        val price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
        val interestPoints = cursor.getString(cursor.getColumnIndexOrThrow("interestPoints"))

        assertEquals("Luminous Apartment", description)
        assertEquals("Paris", town)
        assertEquals(65, surface)
        assertEquals(500000, price)
        assertEquals("SCHOOL,PARK", interestPoints)

        cursor.close()
    }

    @Test
    fun insert_and_query_photoEntity_should_succeed() {
        // GIVEN: insert a property before working with photos (database constraint)
        val propertyContentValues = ContentValues().apply {
            put("description", "Apartment for photo")
            put("address", "123 Rue de Paris")
            put("town", "Paris")
            put("lat", 48.8566)
            put("lng", 2.3522)
            put("country", "France")
            put("createdDate", System.currentTimeMillis())
            put("areaCode", 75000)
            put("surfaceArea", 65)
            put("price", 500000)
            put("soldDate", 0L)
            put("type", "Appartement")
            put("nbrRoom", 3)
            put("realEstateAgent", "Martin Dupont")
            put("interestPoints", "SCHOOL,PARK")
        }

        val propertyUri = context.contentResolver.insert(
            PropertyContentProvider.URI_PROPERTY,
            propertyContentValues
        )
        assertNotNull("URI of property should not be null", propertyUri)
        val propertyId = ContentUris.parseId(propertyUri!!)

        // GIVEN: values to insert
        val photoContentValues = ContentValues().apply {
            put("propertyId", propertyId)
            put("isMain", true)
            put("name", "main_photo.jpg")
            put("photoBytes", "test_photo_data".toByteArray())
        }

        // WHEN: ContentProvider insert values.
        val insertedUri: Uri? = context.contentResolver.insert(
            PropertyContentProvider.URI_PHOTO,
            photoContentValues
        )
        assertNotNull("URI inserted should not be null", insertedUri)

        // AND: query data of insertedUri.
        val cursor: Cursor? = context.contentResolver.query(
            insertedUri!!,
            null,
            null,
            null,
            null
        )

        // THEN: check data matches
        assertNotNull("cursor should not be null", cursor)
        assertTrue("cursor should have one line", cursor!!.moveToFirst())

        val photoPropertyId = cursor.getLong(cursor.getColumnIndexOrThrow("propertyId"))
        val isMain = cursor.getInt(cursor.getColumnIndexOrThrow("isMain"))
        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))

        assertEquals(propertyId, photoPropertyId)
        assertEquals(1, isMain) // true = 1 en SQLite
        assertEquals("main_photo.jpg", name)

        cursor.close()
    }

    @Test
    fun update_property_should_succeed() {
        // GIVEN: Insert a Property
        val contentValues = ContentValues().apply {
            put("description", "Original Apartment")
            put("address", "123 Rue de Paris")
            put("town", "Paris")
            put("lat", 48.8566)
            put("lng", 2.3522)
            put("country", "France")
            put("createdDate", System.currentTimeMillis())
            put("areaCode", 75000)
            put("surfaceArea", 65)
            put("price", 500000)
            put("soldDate", 0L)
            put("type", "Appartement")
            put("nbrRoom", 3)
            put("realEstateAgent", "Martin Dupont")
            put("interestPoints", "SCHOOL")
        }

        val insertedUri = context.contentResolver.insert(
            PropertyContentProvider.URI_PROPERTY,
            contentValues
        )
        assertNotNull(insertedUri)

        // WHEN: values to update
        val updateValues = ContentValues().apply {
            put("description", "Modified Apartment")
            put("price", 550000)
        }

        // ContentProvider do update
        val updatedRows = context.contentResolver.update(
            insertedUri!!,
            updateValues,
            null,
            null
        )

        // THEN: check update successful
        assertEquals(1, updatedRows)

        // AND: check query on insertedUri matches data.
        val cursor = context.contentResolver.query(
            insertedUri,
            null,
            null,
            null,
            null
        )
        assertNotNull(cursor)
        assertTrue(cursor!!.moveToFirst())

        val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
        val price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))

        assertEquals("Modified Apartment", description)
        assertEquals(550000, price)

        cursor.close()
    }

    @Test
    fun delete_property_should_succeed() {
        // GIVEN: inserted property
        val contentValues = ContentValues().apply {
            put("description", "Appartement à supprimer")
            put("address", "123 Rue de Paris")
            put("town", "Paris")
            put("lat", 48.8566)
            put("lng", 2.3522)
            put("country", "France")
            put("createdDate", System.currentTimeMillis())
            put("areaCode", 75000)
            put("surfaceArea", 65)
            put("price", 500000)
            put("soldDate", 0L)
            put("type", "Appartement")
            put("nbrRoom", 3)
            put("realEstateAgent", "Martin Dupont")
            put("interestPoints", "SCHOOL")
        }

        val insertedUri = context.contentResolver.insert(
            PropertyContentProvider.URI_PROPERTY,
            contentValues
        )
        assertNotNull(insertedUri)

        // WHEN: Delete a property
        val deletedRows = context.contentResolver.delete(
            insertedUri!!,
            null,
            null
        )

        // THEN: delete successful
        assertEquals(1, deletedRows)

        // AND: property not found on uri.
        val cursor = context.contentResolver.query(
            insertedUri,
            null,
            null,
            null,
            null
        )
        assertNotNull(cursor)
        assertEquals(0, cursor!!.count)

        cursor.close()
    }

    @Test
    fun query_all_properties_should_return_correct_count() {
        // GIVEN: properties inserted
        val property1 = ContentValues().apply {
            put("description", "Propriété 1")
            put("address", "123 Rue de Paris")
            put("town", "Paris")
            put("lat", 48.8566)
            put("lng", 2.3522)
            put("country", "France")
            put("createdDate", System.currentTimeMillis())
            put("areaCode", 75000)
            put("surfaceArea", 65)
            put("price", 500000)
            put("soldDate", 0L)
            put("type", "Appartement")
            put("nbrRoom", 3)
            put("realEstateAgent", "Martin Dupont")
            put("interestPoints", "SCHOOL")
        }

        val property2 = ContentValues().apply {
            put("description", "Propriété 2")
            put("address", "456 Rue de Lyon")
            put("town", "Lyon")
            put("lat", 45.7578)
            put("lng", 4.8320)
            put("country", "France")
            put("createdDate", System.currentTimeMillis())
            put("areaCode", 69000)
            put("surfaceArea", 80)
            put("price", 600000)
            put("soldDate", 0L)
            put("type", "Maison")
            put("nbrRoom", 4)
            put("realEstateAgent", "Jean Martin")
            put("interestPoints", "PARK,SHOP")
        }

        context.contentResolver.insert(PropertyContentProvider.URI_PROPERTY, property1)
        context.contentResolver.insert(PropertyContentProvider.URI_PROPERTY, property2)

        // WHEN: query all properties
        val cursor = context.contentResolver.query(
            PropertyContentProvider.URI_PROPERTY,
            null,
            null,
            null,
            null
        )

        // THEN: got matches number of entries
        assertNotNull(cursor)
        assertEquals(2, cursor!!.count)

        cursor.close()
    }

    @Test
    fun getType_should_return_correct_mime_type() {
        // WHEN: get type for uris
        val propertyType = context.contentResolver.getType(PropertyContentProvider.URI_PROPERTY)
        val photoType = context.contentResolver.getType(PropertyContentProvider.URI_PHOTO)

        // THEN: MIME type correct.
        assertEquals("vnd.android.cursor.dir/com.ocproject.realestatemanager.provider.PropertyEntity", propertyType)
        assertEquals("vnd.android.cursor.dir/com.ocproject.realestatemanager.provider.PhotoPropertyEntity", photoType)
    }

    @Test
    fun insert_with_null_values_should_return_null() {
        // WHEN: Insert with null values
        val insertedUri = context.contentResolver.insert(
            PropertyContentProvider.URI_PROPERTY,
            null
        )

        // THEN: Insert failure.
        assertEquals(null, insertedUri)
    }

    @Test
    fun update_with_null_values_should_return_zero() {
        // GIVEN: Insert property
        val contentValues = ContentValues().apply {
            put("description", "Test Property")
            put("address", "123 Test Street")
            put("town", "Test City")
            put("lat", 0.0)
            put("lng", 0.0)
            put("country", "Test Country")
            put("createdDate", System.currentTimeMillis())
            put("areaCode", 12345)
            put("surfaceArea", 100)
            put("price", 100000)
            put("soldDate", 0L)
            put("type", "Test")
            put("nbrRoom", 2)
            put("realEstateAgent", "Test Agent")
            put("interestPoints", "SCHOOL")
        }

        val insertedUri = context.contentResolver.insert(
            PropertyContentProvider.URI_PROPERTY,
            contentValues
        )
        assertNotNull(insertedUri)

        // WHEN: update with null values
        val updatedRows = context.contentResolver.update(
            insertedUri!!,
            null,
            null,
            null
        )

        // THEN: update failure
        assertEquals(0, updatedRows)
    }

    @Test
    fun query_with_invalid_uri_should_return_null() {
        // WHEN: query invalid Uri.
        val invalidUri = Uri.parse("content://invalid.authority/invalid_table")
        val cursor = context.contentResolver.query(
            invalidUri,
            null,
            null,
            null,
            null
        )

        // THEN: cursor null
        assertEquals(null, cursor)
    }

    @Test
    fun delete_nonexistent_property_should_throw_exception() {
        // WHEN: delete an non existing property
        val nonexistentUri = Uri.parse("content://com.ocproject.realestatemanager.provider/PropertyEntity/999")
        
        // THEN: property doesn't exist so exception thrown
        try {
            context.contentResolver.delete(
                nonexistentUri,
                null,
                null
            )
            fail("Exception should be thrown")
        } catch (e: Exception) {
            // property doesn't exist so exception thrown
            assertTrue("Exception: lead to non existing property",
                      e.message?.contains("empty") == true || e.message?.contains("null") == true)
        }
    }

    @Test
    fun insert_photo_with_invalid_property_id_should_fail() {
        // GIVEN: values for photo with invalid id
        val photoContentValues = ContentValues().apply {
            put("propertyId", 999L) // ID inexistant
            put("isMain", true)
            put("name", "test_photo.jpg")
            put("photoBytes", "test_data".toByteArray())
        }

        // WHEN: insert photo
        val insertedUri = context.contentResolver.insert(
            PropertyContentProvider.URI_PHOTO,
            photoContentValues
        )

        // THEN: failure since database constraint on foreign key prevent it.
        assertNull("Insert should fail with id invalid", insertedUri)
    }
}
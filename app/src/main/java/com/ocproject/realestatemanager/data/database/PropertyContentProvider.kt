package com.ocproject.realestatemanager.data.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import com.ocproject.realestatemanager.data.entities.PropertyEntity
import kotlinx.coroutines.runBlocking

class PropertyContentProvider : ContentProvider() {

    private val dao: PropertiesDao? by lazy {
        context?.let { PropertiesDatabase.getInstance(it).dao }
    }

    companion object {
        // FOR DATA
        const val AUTHORITY = "com.ocproject.realestatemanager.provider"
        const val PROPERTIES_PATH = "properties"
        val URI_PROPERTY = "content://$AUTHORITY/$PROPERTIES_PATH".toUri()
        
        // URI codes
        const val PROPERTIES = 100
        const val PROPERTY_WITH_ID = 101
        
        // Column names
        const val COLUMN_ID = "id"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_TOWN = "town"
        const val COLUMN_LAT = "lat"
        const val COLUMN_LNG = "lng"
        const val COLUMN_COUNTRY = "country"
        const val COLUMN_CREATED_DATE = "createdDate"
        const val COLUMN_AREA_CODE = "areaCode"
        const val COLUMN_SURFACE_AREA = "surfaceArea"
        const val COLUMN_PRICE = "price"
        const val COLUMN_SOLD = "sold"
        const val COLUMN_INTEREST_POINTS = "interestPoints"
        
        const val TABLE_NAME = "PropertyEntity"
        const val DATABASE_NAME = "PropertiesDatabase"
        const val DATABASE_VERSION = 1
        const val CREATE_DB_TABLE =
            (" CREATE TABLE " + TABLE_NAME
                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + " address TEXT NOT NULL, "
                    + " town TEXT NOT NULL, "
                    + " lat REAL NOT NULL, "
                    + " lng REAL NOT NULL, "
                    + " country TEXT NOT NULL, "
                    + " createdDate INTEGER NOT NULL, "
                    + " areaCode INTEGER, "
                    + " surfaceArea INTEGER, "
                    + " price INTEGER, "
                    + " sold INTEGER, "
                    + " interestPoints TEXT);")

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            // For the entire properties table
            addURI(AUTHORITY, PROPERTIES_PATH, PROPERTIES)
            // For a specific property
            addURI(AUTHORITY, "$PROPERTIES_PATH/#", PROPERTY_WITH_ID)
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            PROPERTIES -> {
                dao?.getPropertiesWithCursor()
            }
            PROPERTY_WITH_ID -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    dao?.getPropertyWithCursorById(id)
                } else {
                    null
                }
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            PROPERTIES -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PROPERTIES_PATH"
            PROPERTY_WITH_ID -> "vnd.android.cursor.item/vnd.$AUTHORITY.$PROPERTIES_PATH"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) != PROPERTIES) {
            throw IllegalArgumentException("Invalid URI for insert")
        }
        
        if (values == null) {
            return null
        }
        
        return runBlocking {
            try {
                val property = contentValuesToPropertyEntity(values)
                val id = dao?.upsertProperty(property)
                if (id != null && id > 0) {
                    "content://$AUTHORITY/$PROPERTIES_PATH/$id".toUri()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            PROPERTIES -> {
                // Delete all properties (not recommended for production)
                0
            }
            PROPERTY_WITH_ID -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    runBlocking {
                        try {
                            val property = dao?.getPropertyDetail(id)
                            if (property != null) {
                                dao?.deleteProperty(property.property)
                                1
                            } else {
                                0
                            }
                        } catch (e: Exception) {
                            0
                        }
                    }
                } else {
                    0
                }
            }
            else -> 0
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        if (values == null) {
            return 0
        }
        
        return when (uriMatcher.match(uri)) {
            PROPERTIES -> {
                // Update multiple properties based on selection
                0
            }
            PROPERTY_WITH_ID -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    runBlocking {
                        try {
                            val property = contentValuesToPropertyEntity(values, id)
                            dao?.upsertProperty(property)
                            1
                        } catch (e: Exception) {
                            0
                        }
                    }
                } else {
                    0
                }
            }
            else -> 0
        }
    }
    
    private fun contentValuesToPropertyEntity(values: ContentValues, id: Long = 0L): PropertyEntity {
        return PropertyEntity(
            id = id,
            address = values.getAsString(COLUMN_ADDRESS) ?: "",
            town = values.getAsString(COLUMN_TOWN) ?: "",
            lat = values.getAsDouble(COLUMN_LAT) ?: 0.0,
            lng = values.getAsDouble(COLUMN_LNG) ?: 0.0,
            country = values.getAsString(COLUMN_COUNTRY) ?: "",
            createdDate = values.getAsLong(COLUMN_CREATED_DATE) ?: System.currentTimeMillis(),
            areaCode = values.getAsInteger(COLUMN_AREA_CODE),
            surfaceArea = values.getAsInteger(COLUMN_SURFACE_AREA),
            price = values.getAsInteger(COLUMN_PRICE),
            sold = values.getAsLong(COLUMN_SOLD),
            interestPoints = emptyList() // Convert from string if needed
        )
    }
}
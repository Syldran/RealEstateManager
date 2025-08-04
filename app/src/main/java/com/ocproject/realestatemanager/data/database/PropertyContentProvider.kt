package com.ocproject.realestatemanager.data.database


import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import com.ocproject.realestatemanager.data.entities.PhotoPropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyEntity
import kotlinx.coroutines.runBlocking

class PropertyContentProvider : ContentProvider() {

    private val dao: PropertiesDao? by lazy {
        context?.let { PropertiesDatabase.getInstance(it).dao }
    }

    companion object {
        const val AUTHORITY = "com.ocproject.realestatemanager.provider"
        const val PROPERTIES_PATH = "properties"
        const val PHOTOS_PATH = "photos"

        val CONTENT_URI_PROPERTIES = "content://$AUTHORITY/$PROPERTIES_PATH".toUri()
        val CONTENT_URI_PHOTOS = "content://$AUTHORITY/$PHOTOS_PATH".toUri()

        const val PROPERTY_ID = "id"
        const val PROPERTY_INTEREST_POINTS = "interestPoints"
        const val PROPERTY_DESCRIPTION = "description"
        const val PROPERTY_ADDRESS = "address"
        const val PROPERTY_TOWN = "town"
        const val PROPERTY_LAT = "lat"
        const val PROPERTY_LNG = "lng"
        const val PROPERTY_COUNTRY = "country"
        const val PROPERTY_CREATED_DATE = "createdDate"
        const val PROPERTY_AREA_CODE = "areaCode"
        const val PROPERTY_SURFACE_AREA = "surfaceArea"
        const val PROPERTY_PRICE = "price"
        const val PROPERTY_SOLD_DATE = "soldDate"

        const val PHOTO_ID = "id"
        const val PHOTO_IS_MAIN = "isMain"
        const val PHOTO_NAME = "name"
        const val PHOTO_BYTES = "photoBytes"
        const val PHOTO_PROPERTY_ID = "propertyId"

        private const val PROPERTIES = 1
        private const val PROPERTY_ID_CODE = 2
        private const val PHOTOS = 3
        private const val PHOTO_ID_CODE = 4

        private val uriMatcher =
            android.content.UriMatcher(android.content.UriMatcher.NO_MATCH).apply {
                addURI(AUTHORITY, PROPERTIES_PATH, PROPERTIES)
                addURI(AUTHORITY, "$PROPERTIES_PATH/#", PROPERTY_ID_CODE)
                addURI(AUTHORITY, PHOTOS_PATH, PHOTOS)
                addURI(AUTHORITY, "$PHOTOS_PATH/#", PHOTO_ID_CODE)
            }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String?>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            PROPERTIES -> {
                dao?.getPropertiesWithCursor()
            }

            PROPERTY_ID_CODE -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    dao?.getPropertyWithCursorById(id)
                } else {
                    throw IllegalArgumentException("Failed to query row for uri: $uri")
                }
            }

            PHOTOS -> {
                dao?.getPhotosWithCursor()
            }

            PHOTO_ID_CODE -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    dao?.getPhotoWithCursorById(id)
                } else {
                    throw IllegalArgumentException("Failed to query row for uri: $uri")
                }
            }

            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            PROPERTIES -> "vnd.android.cursor.dir/property"
            PROPERTY_ID_CODE -> "vnd.android.cursor.item/property"
            PHOTOS -> "vnd.android.cursor.dir/photo"
            PHOTO_ID_CODE -> "vnd.android.cursor.item/photo"
            else -> {
                throw IllegalArgumentException("Failed to query row for uri: $uri")
            }
        }
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? {
        if (values == null) return null

        return when (uriMatcher.match(uri)) {
            PROPERTIES -> {
                val property = contentValuesToPropertyEntity(values)
                runBlocking {
                    val id = dao?.upsertProperty(property)
                    id?.let { Uri.withAppendedPath(CONTENT_URI_PROPERTIES, it.toString()) }
                }
            }

            PHOTOS -> {
                val photo = contentValuesToPhotoPropertyEntity(values)
                runBlocking {
                    val id = dao?.upsertPhoto(photo)
                    id?.let { Uri.withAppendedPath(CONTENT_URI_PHOTOS, it.toString()) }
                }
            }

            else -> throw IllegalArgumentException("Failed to insert row into uri: $uri")
        }
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            PROPERTY_ID_CODE -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    runBlocking {
                        val property = dao?.getPropertyDetail(id)
                        property?.let {
                            dao?.deleteProperty(it.property)
                            // 1 Row successfully affected.
                            1
                        } ?: 0
                    }
                } else 0
            }

            PHOTO_ID_CODE -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    runBlocking {
                        dao?.deletePhotoById(id)
                        1
                    }
                } else 0
            }

            else -> 0
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        if (values == null) {
            // 0 Row modified.
            return 0
        }
        return when (uriMatcher.match(uri)) {
            PROPERTY_ID_CODE -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    val property = contentValuesToPropertyEntity(values, id)
                    runBlocking {
                        dao?.upsertProperty(property)
                    
                        1
                    }
                } else 0
            }

            PHOTO_ID_CODE -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    val photo = contentValuesToPhotoPropertyEntity(values, id)
                    runBlocking {
                        dao?.upsertPhoto(photo)
                        1
                    }
                } else 0
            }

            else -> 0
        }
    }

    private fun contentValuesToPropertyEntity(
        values: ContentValues,
        id: Long = 0L
    ): PropertyEntity {
        return PropertyEntity(
            id = id,
            interestPoints = values.getAsString(PROPERTY_INTEREST_POINTS)?.let {
                Converters().toListInterestPoint(it)
            } ?: emptyList(),
            description = values.getAsString(PROPERTY_DESCRIPTION) ?: "",
            address = values.getAsString(PROPERTY_ADDRESS) ?: "",
            town = values.getAsString(PROPERTY_TOWN) ?: "",
            lat = values.getAsDouble(PROPERTY_LAT) ?: 0.0,
            lng = values.getAsDouble(PROPERTY_LNG) ?: 0.0,
            country = values.getAsString(PROPERTY_COUNTRY) ?: "",
            createdDate = values.getAsLong(PROPERTY_CREATED_DATE) ?: System.currentTimeMillis(),
            areaCode = values.getAsInteger(PROPERTY_AREA_CODE) ?: 0,
            surfaceArea = values.getAsInteger(PROPERTY_SURFACE_AREA) ?: 0,
            price = values.getAsInteger(PROPERTY_PRICE) ?: 0,
            soldDate = values.getAsLong(PROPERTY_SOLD_DATE) ?: 0L
        )
    }

    private fun contentValuesToPhotoPropertyEntity(
        values: ContentValues,
        id: Long = 0L
    ): PhotoPropertyEntity {
        return PhotoPropertyEntity(
            id = id,
            isMain = values.getAsBoolean(PHOTO_IS_MAIN) ?: false,
            name = values.getAsString(PHOTO_NAME) ?: "",
            photoBytes = values.getAsByteArray(PHOTO_BYTES) ?: ByteArray(0),
            propertyId = values.getAsLong(PHOTO_PROPERTY_ID) ?: 0L
        )
    }


}
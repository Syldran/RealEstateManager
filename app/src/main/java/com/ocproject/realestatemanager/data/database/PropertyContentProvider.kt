package com.ocproject.realestatemanager.data.database

import android.content.*
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import androidx.room.Room
import com.ocproject.realestatemanager.data.entities.PhotoPropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyEntity
import kotlinx.coroutines.runBlocking

class PropertyContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.ocproject.realestatemanager.provider"
        const val TABLE_PROPERTY = "PropertyEntity"
        const val TABLE_PHOTO = "PhotoPropertyEntity"

        val URI_PROPERTY: Uri = "content://$AUTHORITY/$TABLE_PROPERTY".toUri()
        val URI_PHOTO: Uri = "content://$AUTHORITY/$TABLE_PHOTO".toUri()

        private const val CODE_PROPERTY = 1
        private const val CODE_PROPERTY_ID = 2
        private const val CODE_PHOTO = 11
        private const val CODE_PHOTO_ID = 12


        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_PROPERTY, CODE_PROPERTY)
            addURI(AUTHORITY, "$TABLE_PROPERTY/#", CODE_PROPERTY_ID)
            addURI(AUTHORITY, TABLE_PHOTO, CODE_PHOTO)
            addURI(AUTHORITY, "$TABLE_PHOTO/#", CODE_PHOTO_ID)
        }
    }

    private lateinit var dao: PropertiesDao

    override fun onCreate(): Boolean {
        context?.let {
            val db = Room.databaseBuilder(
                it,
                PropertiesDatabase::class.java,
                "rem_database.db"
            ).build()
            dao = db.dao
            return true
        }
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor = when (uriMatcher.match(uri)) {
            CODE_PROPERTY -> dao.getPropertiesWithCursor()
            CODE_PROPERTY_ID -> {
                val id = ContentUris.parseId(uri)
                dao.getPropertyWithCursorById(id)
            }

            CODE_PHOTO -> dao.getPhotosWithCursor()
            CODE_PHOTO_ID -> {
                val id = ContentUris.parseId(uri)
                dao.getPhotoWithCursorById(id)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            CODE_PROPERTY -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_PROPERTY"
            CODE_PROPERTY_ID -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_PROPERTY"
            CODE_PHOTO -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_PHOTO"
            CODE_PHOTO_ID -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_PHOTO"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        values ?: return null

        return when (uriMatcher.match(uri)) {
            CODE_PROPERTY -> {
                val prop = contentValuesToProperty(values)
                runBlocking {
                    val id = dao.upsertProperty(prop)
                    context?.contentResolver?.notifyChange(URI_PROPERTY, null)
                    ContentUris.withAppendedId(URI_PROPERTY, id)
                }

            }

            CODE_PHOTO -> {
                val photo = contentValuesToPhoto(values)
                runBlocking {
                    try {
                        val id = dao.upsertPhoto(photo)
                        context?.contentResolver?.notifyChange(URI_PHOTO, null)
                        ContentUris.withAppendedId(URI_PHOTO, id)
                    } catch (e: Exception) {
                        // If error on foreign key constraint return null
                        null
                    }
                }
            }

            else -> throw IllegalArgumentException("Invalid insert URI: $uri")
        }
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            CODE_PROPERTY -> {
                runBlocking {
                    dao.deleteAllProperties()
                }
            }

            CODE_PROPERTY_ID -> {
                val id = ContentUris.parseId(uri)
                runBlocking {
                    val property = dao.getPropertyDetail(id)
                    property.let {
                        dao.deleteProperty(it.property)
                        // 1 Row successfully affected.
                        1
                    }
                }
            }

            CODE_PHOTO -> {
                runBlocking {
                    dao.deleteAllPhotos()
                }
            }

            CODE_PHOTO_ID -> {
                val id = ContentUris.parseId(uri)
                runBlocking {
                    dao.deletePhotoById(id)
                    1
                }
            }

            else -> throw IllegalArgumentException("Invalid delete URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        values ?: return 0
        return when (uriMatcher.match(uri)) {
            CODE_PROPERTY_ID -> {
                val id = ContentUris.parseId(uri)
                runBlocking {
                    context?.contentResolver?.notifyChange(uri, null)
                    dao.upsertProperty(contentValuesToProperty(values).copy(id = id))

                    1
                }

            }

            CODE_PHOTO_ID -> {
                val id = ContentUris.parseId(uri)
                runBlocking {
                    context?.contentResolver?.notifyChange(uri, null)
                    dao.upsertPhoto(contentValuesToPhoto(values).copy(id = id))
                }
                1
            }

            else -> throw IllegalArgumentException("Invalid update URI: $uri")
        }
    }

    private fun contentValuesToProperty(values: ContentValues): PropertyEntity = PropertyEntity(
        id = values.getAsLong("id") ?: 0L,
        interestPoints = values.get("interestPoints")?.let {
            Converters().toListInterestPoint(it as String)
        } ?: emptyList(),
        description = values.getAsString("description") ?: "",
        address = values.getAsString("address") ?: "",
        town = values.getAsString("town") ?: "",
        lat = values.getAsDouble("lat") ?: 0.0,
        lng = values.getAsDouble("lng") ?: 0.0,
        country = values.getAsString("country") ?: "",
        createdDate = values.getAsLong("createdDate") ?: 0L,
        areaCode = values.getAsInteger("areaCode") ?: 0,
        surfaceArea = values.getAsInteger("surfaceArea") ?: 0,
        price = values.getAsInteger("price") ?: 0,
        soldDate = values.getAsLong("soldDate") ?: 0L,
        type = values.getAsString("type") ?: "",
        nbrRoom = values.getAsInteger("nbrRoom") ?: 0,
        realEstateAgent = values.getAsString("realEstateAgent") ?: "",
    )

    private fun contentValuesToPhoto(values: ContentValues): PhotoPropertyEntity =
        PhotoPropertyEntity(
            id = values.getAsLong("id") ?: 0L,
            propertyId = values.getAsLong("propertyId") ?: 0L,
            isMain = values.getAsBoolean("isMain") ?: false,
            name = values.getAsString("name") ?: "",
            photoBytes = values.getAsByteArray("photoBytes") ?: ByteArray(0)
        )

}

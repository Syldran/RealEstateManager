package com.ocproject.realestatemanager.data.database

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * Example class showing how to use the PropertyContentProvider
 * This demonstrates the CRUD operations available through the ContentProvider
 */
class PropertyContentProviderExample(private val contentResolver: ContentResolver) {

    companion object {
        private const val AUTHORITY = "com.ocproject.realestatemanager.provider"
        private const val PROPERTIES_PATH = "properties"
        private val BASE_URI = Uri.parse("content://$AUTHORITY/$PROPERTIES_PATH")
    }

    /**
     * Query all properties
     */
    fun getAllProperties(): Cursor? {
        return contentResolver.query(
            BASE_URI,
            null, // projection - null means all columns
            null, // selection
            null, // selectionArgs
            null  // sortOrder
        )
    }

    /**
     * Query a specific property by ID
     */
    fun getPropertyById(id: Long): Cursor? {
        val uri = Uri.withAppendedPath(BASE_URI, id.toString())
        return contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )
    }

    /**
     * Insert a new property
     */
    fun insertProperty(
        address: String,
        town: String,
        lat: Double,
        lng: Double,
        country: String,
        price: Int? = null,
        surfaceArea: Int? = null,
        areaCode: Int? = null
    ): Uri? {
        val values = ContentValues().apply {
            put(PropertyContentProvider.COLUMN_ADDRESS, address)
            put(PropertyContentProvider.COLUMN_TOWN, town)
            put(PropertyContentProvider.COLUMN_LAT, lat)
            put(PropertyContentProvider.COLUMN_LNG, lng)
            put(PropertyContentProvider.COLUMN_COUNTRY, country)
            put(PropertyContentProvider.COLUMN_CREATED_DATE, System.currentTimeMillis())
            put(PropertyContentProvider.COLUMN_PRICE, price)
            put(PropertyContentProvider.COLUMN_SURFACE_AREA, surfaceArea)
            put(PropertyContentProvider.COLUMN_AREA_CODE, areaCode)
        }

        return contentResolver.insert(BASE_URI, values)
    }

    /**
     * Update a property
     */
    fun updateProperty(
        id: Long,
        address: String? = null,
        town: String? = null,
        price: Int? = null,
        surfaceArea: Int? = null
    ): Int {
        val uri = Uri.withAppendedPath(BASE_URI, id.toString())
        val values = ContentValues().apply {
            address?.let { put(PropertyContentProvider.COLUMN_ADDRESS, it) }
            town?.let { put(PropertyContentProvider.COLUMN_TOWN, it) }
            price?.let { put(PropertyContentProvider.COLUMN_PRICE, it) }
            surfaceArea?.let { put(PropertyContentProvider.COLUMN_SURFACE_AREA, it) }
        }

        return contentResolver.update(uri, values, null, null)
    }

    /**
     * Delete a property
     */
    fun deleteProperty(id: Long): Int {
        val uri = Uri.withAppendedPath(BASE_URI, id.toString())
        return contentResolver.delete(uri, null, null)
    }

    /**
     * Mark a property as sold
     */
    fun markPropertyAsSold(id: Long): Int {
        val uri = Uri.withAppendedPath(BASE_URI, id.toString())
        val values = ContentValues().apply {
            put(PropertyContentProvider.COLUMN_SOLD, System.currentTimeMillis())
        }
        return contentResolver.update(uri, values, null, null)
    }

    /**
     * Query properties by price range
     */
    fun getPropertiesByPriceRange(minPrice: Int, maxPrice: Int): Cursor? {
        val selection = "${PropertyContentProvider.COLUMN_PRICE} BETWEEN ? AND ?"
        val selectionArgs = arrayOf(minPrice.toString(), maxPrice.toString())
        
        return contentResolver.query(
            BASE_URI,
            null,
            selection,
            selectionArgs,
            "${PropertyContentProvider.COLUMN_PRICE} ASC"
        )
    }

    /**
     * Query properties by town
     */
    fun getPropertiesByTown(town: String): Cursor? {
        val selection = "${PropertyContentProvider.COLUMN_TOWN} = ?"
        val selectionArgs = arrayOf(town)
        
        return contentResolver.query(
            BASE_URI,
            null,
            selection,
            selectionArgs,
            "${PropertyContentProvider.COLUMN_CREATED_DATE} DESC"
        )
    }
} 
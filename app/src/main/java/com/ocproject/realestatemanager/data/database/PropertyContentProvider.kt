package com.ocproject.realestatemanager.data.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import org.koin.dsl.koinApplication

class PropertyContentProvider(
//    private val dao: PropertiesDao, impossible car pas d'instanciention
) : ContentProvider() {
    val dao: PropertiesDao? = context?.let { PropertiesDatabase.getInstance(it).dao }
    companion object {
        // FOR DATA
        val AUTHORITY = "com.ocproject.realestatemanager.provider"
        val TABLE_NAME = "Property"
        val URI_PROPERTY = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    override fun onCreate(): Boolean {
        return true
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, URI_PROPERTY.toString(), 0)
    }
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        if (uriMatcher.match(uri) == 0) {
            val cursor = dao?.getPropertiesWithCursor()
            return cursor
        }
        return null
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}
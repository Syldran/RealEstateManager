package com.ocproject.realestatemanager.data.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.ocproject.realestatemanager.data.entities.PhotoPropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyWithPhotosEntity

@Dao
interface PropertiesDao {


    @Query("SELECT * FROM PropertyEntity")
    fun getPropertiesWithCursor(): Cursor

    @Query("SELECT * FROM PropertyEntity WHERE id = :id")
    fun getPropertyWithCursorById(id: Long): Cursor

    // Photo cursor methods for ContentProvider
    @Query("SELECT * FROM PhotoPropertyEntity")
    fun getPhotosWithCursor(): Cursor

    @Query("SELECT * FROM PhotoPropertyEntity WHERE id = :id")
    fun getPhotoWithCursorById(id: Long): Cursor

    @Query("SELECT * FROM PhotoPropertyEntity WHERE propertyId = :propertyId")
    fun getPhotosWithCursorByPropertyId(propertyId: Long): Cursor

    @Upsert
    suspend fun upsertProperty(property: PropertyEntity): Long

    @Delete
    suspend fun deleteProperty(property: PropertyEntity)

    @Upsert
    suspend fun upsertPhoto(photoProperty: PhotoPropertyEntity): Long

    @Delete
    suspend fun deletePhoto(photoProperty: PhotoPropertyEntity)

    @Query("DELETE FROM PhotoPropertyEntity WHERE id = :photoId")
    suspend fun deletePhotoById(photoId: Long)

    @Query("DELETE FROM PropertyEntity")
    suspend fun deleteAllProperties(): Int

    @Query("DELETE FROM PhotoPropertyEntity")
    suspend fun deleteAllPhotos(): Int

    @Transaction
    @Query(
        "SELECT * FROM PropertyEntity as prop " +
                "LEFT JOIN PhotoPropertyEntity as photo ON prop.id = photo.propertyId " +
                "WHERE (:areaCode IS NULL OR prop.areaCode = :areaCode) " +
                "AND (:type IS NULL OR prop.type LIKE :type) " +
                "AND (:interestPoints IS NULL OR prop.interestPoints LIKE :interestPoints) " +
                "AND prop.createdDate >= :minAddedDate AND prop.createdDate <= :maxAddedDate "+
                "AND (:sellingStatus IS NULL " +
                "OR ( :sellingStatus = 1 AND prop.soldDate >= :minSoldDate AND prop.soldDate <= :maxSoldDate) "+
                "OR :sellingStatus = -1 AND SIGN(prop.soldDate) = SIGN(:sellingStatus))" +
                "AND prop.price >= :minPrice AND prop.price <= :maxPrice "+
                "AND prop.surfaceArea >= :minSurface AND prop.surfaceArea <= :maxSurface "+
                "GROUP BY prop.id " +
                "HAVING COUNT(photo.id) >= :minPhotos " +
                "ORDER BY prop.price ASC"
    )
    suspend fun getPropertyListPriceASC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity>

    @Query("DELETE FROM PhotoPropertyEntity WHERE propertyId = :propertyId")
    suspend fun deletePicturesOfPropertyByIdProperty(propertyId: Long)

    @Transaction
    @Query(
        "SELECT * FROM PropertyEntity as prop " +
                "LEFT JOIN PhotoPropertyEntity as photo ON prop.id = photo.propertyId " +
                "WHERE (:areaCode IS NULL OR prop.areaCode = :areaCode) " +
                "AND (:type IS NULL OR prop.type LIKE :type) " +
                "AND (:interestPoints IS NULL OR prop.interestPoints LIKE :interestPoints) " +
                "AND prop.createdDate >= :minAddedDate AND prop.createdDate <= :maxAddedDate "+
                "AND (:sellingStatus IS NULL " +
                "OR ( :sellingStatus = 1 AND prop.soldDate >= :minSoldDate AND prop.soldDate <= :maxSoldDate) "+
                "OR :sellingStatus = -1 AND SIGN(prop.soldDate) = SIGN(:sellingStatus))" +
                "AND prop.price >= :minPrice AND prop.price <= :maxPrice "+
                "AND prop.surfaceArea >= :minSurface AND prop.surfaceArea <= :maxSurface "+
                "GROUP BY prop.id " +
                "HAVING COUNT(photo.id) >= :minPhotos " +
                "ORDER BY prop.price DESC"
    )
    suspend fun getPropertyListPriceDESC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity>




    @Transaction
    @Query(
        "SELECT * FROM PropertyEntity as prop " +
                "LEFT JOIN PhotoPropertyEntity as photo ON prop.id = photo.propertyId " +
                "WHERE (:areaCode IS NULL OR prop.areaCode = :areaCode) " +
                "AND (:type IS NULL OR prop.type LIKE :type) " +
                "AND (:interestPoints IS NULL OR prop.interestPoints LIKE :interestPoints) " +
                "AND prop.createdDate >= :minAddedDate AND prop.createdDate <= :maxAddedDate "+
                "AND (:sellingStatus IS NULL " +
                "OR ( :sellingStatus = 1 AND prop.soldDate >= :minSoldDate AND prop.soldDate <= :maxSoldDate) "+
                "OR :sellingStatus = -1 AND SIGN(prop.soldDate) = SIGN(:sellingStatus))" +
                "AND prop.price >= :minPrice AND prop.price <= :maxPrice "+
                "AND prop.surfaceArea >= :minSurface AND prop.surfaceArea <= :maxSurface "+
                "GROUP BY prop.id " +
                "HAVING COUNT(photo.id) >= :minPhotos " +
                "ORDER BY prop.createdDate ASC"
    )
    suspend fun getPropertyListDateASC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity>
    @Transaction
    @Query(
        "SELECT * FROM PropertyEntity as prop " +
                "LEFT JOIN PhotoPropertyEntity as photo ON prop.id = photo.propertyId " +
                "WHERE (:areaCode IS NULL OR prop.areaCode = :areaCode) " +
                "AND (:type IS NULL OR prop.type LIKE :type) " +
                "AND (:interestPoints IS NULL OR prop.interestPoints LIKE :interestPoints) " +
                "AND prop.createdDate >= :minAddedDate AND prop.createdDate <= :maxAddedDate "+
                "AND (:sellingStatus IS NULL " +
                "OR ( :sellingStatus = 1 AND prop.soldDate >= :minSoldDate AND prop.soldDate <= :maxSoldDate) "+
                "OR :sellingStatus = -1 AND SIGN(prop.soldDate) = SIGN(:sellingStatus))" +
                "AND prop.price >= :minPrice AND prop.price <= :maxPrice "+
                "AND prop.surfaceArea >= :minSurface AND prop.surfaceArea <= :maxSurface "+
                "GROUP BY prop.id " +
                "HAVING COUNT(photo.id) >= :minPhotos " +
                "ORDER BY prop.createdDate DESC"
    )
    suspend fun getPropertyListDateDESC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity>



    @Transaction
    @Query(
        "SELECT * FROM PropertyEntity as prop " +
                "LEFT JOIN PhotoPropertyEntity as photo ON prop.id = photo.propertyId " +
                "WHERE (:areaCode IS NULL OR prop.areaCode = :areaCode) " +
                "AND (:type IS NULL OR prop.type LIKE :type) " +
                "AND (:interestPoints IS NULL OR prop.interestPoints LIKE :interestPoints) " +
                "AND prop.createdDate >= :minAddedDate AND prop.createdDate <= :maxAddedDate "+
                "AND (:sellingStatus IS NULL " +
                "OR ( :sellingStatus = 1 AND prop.soldDate >= :minSoldDate AND prop.soldDate <= :maxSoldDate) "+
                "OR :sellingStatus = -1 AND SIGN(prop.soldDate) = SIGN(:sellingStatus))" +
                "AND prop.price >= :minPrice AND prop.price <= :maxPrice "+
                "AND prop.surfaceArea >= :minSurface AND prop.surfaceArea <= :maxSurface "+
                "GROUP BY prop.id " +
                "HAVING COUNT(photo.id) >= :minPhotos " +
                "ORDER BY prop.surfaceArea ASC"
    )
    suspend fun getPropertyListSurfaceASC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity>
    @Transaction
    @Query(
        "SELECT * FROM PropertyEntity as prop " +
                "LEFT JOIN PhotoPropertyEntity as photo ON prop.id = photo.propertyId " +
                "WHERE (:areaCode IS NULL OR prop.areaCode = :areaCode) " +
                "AND (:type IS NULL OR prop.type LIKE :type) " +
                "AND (:interestPoints IS NULL OR prop.interestPoints LIKE :interestPoints) " +
                "AND prop.createdDate >= :minAddedDate AND prop.createdDate <= :maxAddedDate "+
                "AND (:sellingStatus IS NULL " +
                "OR ( :sellingStatus = 1 AND prop.soldDate >= :minSoldDate AND prop.soldDate <= :maxSoldDate) "+
                "OR :sellingStatus = -1 AND SIGN(prop.soldDate) = SIGN(:sellingStatus))" +
                "AND prop.price >= :minPrice AND prop.price <= :maxPrice "+
                "AND prop.surfaceArea >= :minSurface AND prop.surfaceArea <= :maxSurface "+
                "GROUP BY prop.id " +
                "HAVING COUNT(photo.id) >= :minPhotos " +
                "ORDER BY prop.surfaceArea DESC"
    )
    suspend fun getPropertyListSurfaceDESC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity>


    @Transaction
    @Query("SELECT * FROM PropertyEntity ORDER BY price ASC")
    suspend fun getPropertyList(): List<PropertyWithPhotosEntity>



    @Transaction
    @Query("SELECT * FROM PropertyEntity WHERE id = :selectedId")
    suspend fun getPropertyDetail(selectedId: Long): PropertyWithPhotosEntity

}
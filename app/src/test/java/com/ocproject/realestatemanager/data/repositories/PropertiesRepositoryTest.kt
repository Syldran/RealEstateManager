package com.ocproject.realestatemanager.data.repositories

import com.ocproject.realestatemanager.models.Property
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

//import org.mockito.Mockito.mock

//GOAL 100% COVERAGE

class PropertiesRepositoryTest {
    val property = Property(
        emptyList(),
        false,
        false,
        false,
        false,
        "quelque part",
        "NowhereCity",
        120.5,
        50.30,
        "Faraway",
        null,
        18290,
        150,
        150000,
        sold = false,
    )
//    private val propertiesDao: PropertiesDao = mock()

    //    @Before
//    fun setUp() {
//        //mocker dao
//
//    }
    @Test
    fun insert_Property() = runTest {
        val repo = LocalPropertiesRepository(FakePropertiesDao())
        val property = Property(
            emptyList(),
            false,
            false,
            false,
            false,
            "quelque part",
            "NowhereCity",
            120.5,
            50.30,
            "Faraway",
            null,
            18290,
            150,
            150000,
            sold = false,
        )
        val propertyWithPhotos = PropertyWithPhotos(property, null)
        repo.upsertProperty(property)
        assertEquals(propertyWithPhotos, repo.getPropertyList().first().first())
    }

    @Test
    fun delete_Property() = runTest {
        val repo = LocalPropertiesRepository(FakePropertiesDao())
        val property = Property(
            emptyList(),
            false,
            false,
            false,
            false,
            "quelque part",
            "NowhereCity",
            120.5,
            50.30,
            "Faraway",
            null,
            18290,
            150,
            150000,
            sold = false,
        )
        val propertyWithPhotos = PropertyWithPhotos(property, null)
        repo.upsertProperty(property)
        assertEquals(propertyWithPhotos, repo.getPropertyList().first().first())
        repo.deleteProperty(property)
        assertEquals(emptyList<PropertyWithPhotos>(), repo.getPropertyList().last())


        //given
        //when
        //then
        // appel méthode repo et check méthode dao called once
//        verify(repository, times(1)).someMethod()
    }

    //
//    @Test
//    fun data_storeUser_returnUserDetailsStored() {
//        runBlocking {
//
//            `when`(cacheRepository.retrieveUserDetails())
//            ).thenReturn(Result.GetUserDetails)
//
//            val result = dataRepository.changeCustomDataBasedOnAction(
//                MyAction.StoreUser(
//                    "username", "40"
//                )
//            )
//
//            Assert.assertEquals(result, ListenableWorker.Result.UserDetailsStored)
//        }
//    }
//
//    @Test
//    fun data_storeUser_returnUserDetailsNotStored() {
//        runBlocking {
//
//            `when`(cacheRepository.retrieveUserDetails())
//            ).thenReturn(Result.UserDetailsNotStored)
//
//            val result = dataRepository.changeCustomDataBasedOnAction(
//                MyAction.StoreUser(
//                    "username", "40"
//                )
//            )
//
//            Assert.assertEquals(result, ListenableWorker.Result.UserDetailsNotStored)
//        }
//    }
    @Test
    fun insertPhotoProperty() {
    }

    @Test
    fun deletePicturesOfPropertyById() {
    }

    @Test
    fun getPropertyList() {
    }

    @Test
    fun getProperty() {
    }
}
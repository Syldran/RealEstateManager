package com.testdb

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.data.database.PropertiesDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedConstruction

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var propertyDao: PropertiesDao
    private lateinit var db: PropertiesDatabase
// GOAL 100% COVERAGE (IN MEMORY)
//    @Before
//    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<MockedConstruction.Context>()
//        db = Room.inMemoryDatabaseBuilder(
//            context, PropertiesDatabase::class.java).build()
//        propertyDao = db.dao
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun closeDb() {
//        db.close()
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun writeUserAndReadInList() {
//        val user: User = TestUtil.createUser(3).apply {
//            setName("george")
//        }
//        userDao.insert(user)
//        val byName = userDao.findUsersByName("george")
//        assertThat(byName.get(0), equalTo(user))
//    }
}

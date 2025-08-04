package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ocproject.realestatemanager.MainCoroutineRule
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class AddPropertyViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    val testDispatcher = StandardTestDispatcher()

    private val getPropertyDetails = mockk<GetPropertyDetailsUseCase>(relaxed = true)
    private val saveProperty = mockk<SavePropertyUseCase>(relaxed = true)
    private lateinit var viewModel: AddPropertyViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)

    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get Property, editing case`() = runTest {
        //Editing case so property id as existing value
        val property = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Description of a Somewhere",
            address = "Somewhere",
            town = "NowhereCity",
            lat = 120.5,
            lng = 50.30,
            country = "Faraway",
            createdDate = 500,
            areaCode = 18290,
            surfaceArea = 150,
            price = 150000,
            sold = -1,
            id = 1L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Test Agent",
        )
        coEvery { getPropertyDetails.invoke(any()) } returns property
        viewModel.getProperty()
        advanceUntilIdle()
        coVerify { getPropertyDetails.invoke(any()) }
        assert(viewModel.state.value.newProperty.id == 1L)
    }

    @Test
    fun `get Property, new entry case`() = runTest {
        //New property, so id is set to 0 and will be auto incremented in bdd.
        val property1 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Description of a Somewhere",
            address = "Somewhere",
            town = "NowhereCity",
            lat = 120.5,
            lng = 50.30,
            country = "Faraway",
            createdDate = 500,
            areaCode = 18290,
            surfaceArea = 150,
            price = 150000,
            sold = -1,
            id = 0L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Test Agent",
        )
        val viewModelTest = AddPropertyViewModel(0L, getPropertyDetails, saveProperty)
        advanceUntilIdle()
        viewModelTest.getProperty()
        advanceUntilIdle()
        coVerify { getPropertyDetails.invoke(any()) }
        assert(viewModel.state.value.newProperty.id == 0L)
    }

    @Test
    fun `save property test`() = runTest {
        val property1 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Description of a Somewhere",
            address = "Somewhere",
            town = "Paris",
            lat = 120.5,
            lng = 50.30,
            country = "France",
            createdDate = 10000,
            areaCode = 18290,
            surfaceArea = 150,
            price = 300000,
            sold = -1,
            id = 2L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Test Agent",
        )
        val property2 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Description of a Somewhere",
            address = "Somewhere",
            town = "Paris",
            lat = 120.5,
            lng = 50.30,
            country = "France",
            createdDate = null,
            areaCode = 18290,
            surfaceArea = 150,
            price = 300000,
            sold = -1,
            id = 5L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Test Agent",
        )
        viewModel.saveProperty(
            property1,
            "Property saved successfully!",
            "Failed to save property. Please try again."
        )
        advanceUntilIdle()
        coVerify { saveProperty(property1) }
        viewModel.saveProperty(
            property2,
            "Property saved successfully!",
            "Failed to save property. Please try again."
        )
        advanceUntilIdle()
        coVerify {
            saveProperty(any())
        }
    }

    @Test
    fun `update form test`() = runTest {
        coEvery { getPropertyDetails(1L) } returns Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "",
            address = "",
            town = "",
            lat = 0.0,
            lng = 0.0,
            country = "",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 0,
            surfaceArea = 0,
            price = 0,
            id = 1L,
            sold = -1,
            type = "",
            nbrRoom = 0,
            realEstateAgent = "",
        )
        val viewModelTest = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)
        advanceUntilIdle()
        viewModelTest.updateForm(AddPropertyEvent.UpdateForm(town = "Paris"))
        advanceUntilIdle()
        val town = viewModelTest.state.value.newProperty.town
        assertEquals("Paris", town)
    }


    @Test
    fun `update tags test`() = runTest {

        coEvery { getPropertyDetails(1L) } returns Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "",
            address = "",
            town = "",
            lat = 0.0,
            lng = 0.0,
            country = "",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 0,
            surfaceArea = 0,
            price = 0,
            id = 1L,
            sold = -1,
            type = "",
            nbrRoom = 0,
            realEstateAgent = "",
        )
        val viewModelTest = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)
        advanceUntilIdle()

        viewModelTest.updateTags(
            AddPropertyEvent.UpdateTags(
                school = true,
                shop = false,
                park = false,
                transport = false,
            )
        )
        advanceUntilIdle()
        val interestPoint = viewModelTest.state.value.newProperty.interestPoints
        assert(interestPoint.contains(InterestPoint.SCHOOL))
    }

    @Test
    fun `remove photo test`() = runTest {
        val photo1 = PhotoProperty(isMain = true, name="Photo 1", photoBytes = byteArrayOf())
        val photo2 = PhotoProperty(isMain = false, name="Photo 2", photoBytes = byteArrayOf())
        coEvery { getPropertyDetails(1L) } returns Property(
            photoList = listOf(photo1, photo2),
            interestPoints = emptyList(),
            description = "",
            address = "",
            town = "",
            lat = 0.0,
            lng = 0.0,
            country = "",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 0,
            surfaceArea = 0,
            price = 0,
            id = 1L,
            sold = -1,
            type = "",
            nbrRoom = 0,
            realEstateAgent = "",
        )
        val viewModelTest = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)
        advanceUntilIdle()
        viewModelTest.removePhoto(photo1)
        advanceUntilIdle()
        assert(viewModelTest.state.value.photoList.size == 1)
    }

    @Test
    fun `add photo from camera test`() = runTest {
        val viewModelTest = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)
        advanceUntilIdle()
        // add a camera taken photo
        viewModelTest.addPhotoFromCamera(byteArrayOf())
        advanceUntilIdle()
        // check previously empty photo list has now 1
        assert(viewModelTest.state.value.photoList.size == 1)
    }

    @Test
    fun `update photo & photo name change test`() = runTest {
        val photo1 = PhotoProperty(isMain = true, name = "Photo 1", photoBytes = byteArrayOf())
        val viewModelTest = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)
        advanceUntilIdle()
        // OnPhotoNameChanged apply on photo list so we populate it with one.
        viewModelTest.onEvent(AddPropertyEvent.UpdatePhotos(listOf(photo1)))
        advanceUntilIdle()
        // Check photo list has now a photo
        assert(viewModelTest.state.value.photoList.size == 1)
        // Call function with new photo name
        viewModelTest.onEvent(AddPropertyEvent.OnPhotoNameChanged(photo1, "Photo Test"))
        advanceUntilIdle()
        // Check that photo list has the photo with new name
        assert(viewModelTest.state.value.photoList[0].name == "Photo Test")
    }

    @Test
    fun `update photo from picker`() = runTest {
        val photoByteFromPicker = byteArrayOf()
        val viewModelTest = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)
        advanceUntilIdle()
        viewModelTest.onEvent(AddPropertyEvent.OnPhotoPicked(listOf(photoByteFromPicker)))
        advanceUntilIdle()
        // Check that photo list has a photo oh ByteArray corresponding
        assert(viewModelTest.state.value.photoList[0].photoBytes.contentEquals(photoByteFromPicker))
    }
}
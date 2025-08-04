package com.ocproject.realestatemanager.data.repositories

import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.domain.models.Property
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PropertiesRepositoryTest {

    private lateinit var fakeRepository: FakePropertiesRepository

    @Before
    fun setup() {
        fakeRepository = FakePropertiesRepository()
    }

    @Test
    fun `upsertProperty should add new property and return correct id`() = runTest {
        // Given
        val property = createTestProperty(id = -1L)

        // When
        val returnedId = fakeRepository.upsertProperty(property)

        // Then
        assertEquals(1L, returnedId)
        assertEquals(1, fakeRepository.getPropertiesCount())
        val savedProperty = fakeRepository.getProperty(returnedId)
        assertEquals(returnedId, savedProperty.id)
        assertEquals(property.description, savedProperty.description)
    }

    @Test
    fun `upsertProperty should update existing property`() = runTest {
        // Given
        val property = createTestProperty(id = 1L)
        fakeRepository.addProperty(property)
        
        val updatedProperty = property.copy(description = "Updated description")

        // When
        val returnedId = fakeRepository.upsertProperty(updatedProperty)

        // Then
        assertEquals(1L, returnedId)
        assertEquals(1, fakeRepository.getPropertiesCount())
        val savedProperty = fakeRepository.getProperty(returnedId)
        assertEquals("Updated description", savedProperty.description)
    }

    @Test
    fun `deleteProperty should remove property from repository`() = runTest {
        // Given
        val property = createTestProperty(id = 1L)
        fakeRepository.addProperty(property)
        assertEquals(1, fakeRepository.getPropertiesCount())

        // When
        fakeRepository.deleteProperty(property)

        // Then
        assertEquals(0, fakeRepository.getPropertiesCount())
        val deletedProperty = fakeRepository.getProperty(1L)
        assertEquals(-1L, deletedProperty.id) // Default empty property
    }

    @Test
    fun `getPropertyList should return all properties`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)

        // When
        val properties = fakeRepository.getPropertyList()

        // Then
        assertEquals(5, properties.size)
        assertTrue(properties.any { it.town == "Paris" })
        assertTrue(properties.any { it.town == "Lyon" })
        assertTrue(properties.any { it.town == "Marseille" })
    }

    @Test
    fun `getProperty should return correct property by id`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)

        // When
        val property = fakeRepository.getProperty(1L)

        // Then
        assertEquals(1L, property.id)
        assertEquals("Paris", property.town)
        assertEquals("Belle maison avec jardin", property.description)
    }

    @Test
    fun `getProperty should return empty property when id not found`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)

        // When
        val property = fakeRepository.getProperty(999L)

        // Then
        assertEquals(-1L, property.id)
        assertEquals("", property.description)
    }

    @Test
    fun `getPropertyListFiltered should filter by selling status PURCHASABLE`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(sellingStatus = SellingStatus.PURCHASABLE)

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        properties.forEach { property ->
            assertEquals(-1L, property.sold) // All should be purchasable
        }
    }

    @Test
    fun `getPropertyListFiltered should filter by selling status SOLD`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(sellingStatus = SellingStatus.SOLD)

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        properties.forEach { property ->
            assertEquals(1L, property.sold) // All should be sold
        }
    }

    @Test
    fun `getPropertyListFiltered should filter by area code`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(areaCodeFilter = 75001)

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        properties.forEach { property ->
            assertEquals(75001, property.areaCode)
        }
    }

    @Test
    fun `getPropertyListFiltered should filter by type housing`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(typeHousing = "House")

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        properties.forEach { property ->
            assertEquals("House", property.type)
        }
    }

    @Test
    fun `getPropertyListFiltered should filter by interest points`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(tagSchool = true)

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        properties.forEach { property ->
            assertTrue(property.interestPoints.contains(InterestPoint.SCHOOL))
        }
    }

    @Test
    fun `getPropertyListFiltered should filter by price range`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(
            priceRange = Range(200000, 400000)
        )

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        properties.forEach { property ->
            assertTrue(property.price in 200000..400000)
        }
    }

    @Test
    fun `getPropertyListFiltered should sort by price ASC`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(
            sortType = SortType.PRICE,
            priceOrder = Order.ASC
        )

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        for (i in 0 until properties.size - 1) {
            assertTrue(properties[i].price <= properties[i + 1].price)
        }
    }

    @Test
    fun `getPropertyListFiltered should sort by price DESC`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(
            sortType = SortType.PRICE,
            priceOrder = Order.DESC
        )

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        for (i in 0 until properties.size - 1) {
            assertTrue(properties[i].price >= properties[i + 1].price)
        }
    }

    @Test
    fun `getPropertyListFiltered should sort by date ASC`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(
            sortType = SortType.DATE,
            dateOrder = Order.ASC
        )

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        for (i in 0 until properties.size - 1) {
            assertTrue(properties[i].createdDate!! <= properties[i + 1].createdDate!!)
        }
    }

    @Test
    fun `getPropertyListFiltered should sort by surface ASC`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(
            sortType = SortType.AREA,
            surfaceOrder = Order.ASC
        )

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        for (i in 0 until properties.size - 1) {
            assertTrue(properties[i].surfaceArea <= properties[i + 1].surfaceArea)
        }
    }

    @Test
    fun `getPropertyListFiltered should filter by minimum photos`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(minNbrPhotos = 1)

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        // All properties in test data have empty photo lists, so should return empty
        assertTrue(properties.isEmpty())
    }

    @Test
    fun `getPropertyListFiltered should filter by surface range`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(
            surfaceRange = Range(100, 200)
        )

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        properties.forEach { property ->
            assertTrue(property.surfaceArea in 100..200)
        }
    }

    @Test
    fun `getPropertyListFiltered should filter by date range`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(
            dateRange = Range(1640995200000L, 1643673600000L) // Jan-Feb 2022
        )

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        assertTrue(properties.isNotEmpty())
        properties.forEach { property ->
            assertTrue(property.createdDate in 1640995200000L..1643673600000L)
        }
    }

    @Test
    fun `getPropertyListFiltered should handle multiple filters simultaneously`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(
            sellingStatus = SellingStatus.PURCHASABLE,
            areaCodeFilter = 75001,
            tagSchool = true,
            priceRange = Range(400000, 500000)
        )

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        properties.forEach { property ->
            assertEquals(-1L, property.sold) // Purchasable
            assertEquals(75001, property.areaCode) // Paris area
            assertTrue(property.interestPoints.contains(InterestPoint.SCHOOL)) // Has school
            assertTrue(property.price in 400000..500000) // Price range
        }
    }

    @Test
    fun `getPropertyListFiltered should handle typeHousing with other filters`() = runTest {
        // Given
        fakeRepository.shouldHaveFilledList(true)
        val filter = createTestFilter(
            sellingStatus = SellingStatus.PURCHASABLE,
            typeHousing = "House",
            priceRange = Range(300000, 600000)
        )

        // When
        val properties = fakeRepository.getPropertyListFiltered(filter)

        // Then
        properties.forEach { property ->
            assertEquals(-1L, property.sold) // Purchasable
            assertEquals("House", property.type) // House type
            assertTrue(property.price in 300000..600000) // Price range
        }
    }

    // Helper methods
    private fun createTestProperty(
        id: Long = 1L,
        description: String = "Test Property",
        price: Int = 200000,
        sold: Long = -1L
    ): Property {
        return Property(
            id = id,
            photoList = emptyList(),
            interestPoints = listOf(InterestPoint.SCHOOL),
            description = description,
            address = "123 Test Street",
            town = "Test City",
            lat = 48.8566,
            lng = 2.3522,
            country = "France",
            createdDate = 1640995200000L,
            areaCode = 75001,
            surfaceArea = 100,
            price = price,
            sold = sold,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Test Agent"
        )
    }

    private fun createTestFilter(
        sortType: SortType = SortType.PRICE,
        priceOrder: Order = Order.ASC,
        dateOrder: Order = Order.ASC,
        surfaceOrder: Order = Order.ASC,
        priceRange: Range<Int> = Range(0, Int.MAX_VALUE),
        dateRange: Range<Long> = Range(0L, Long.MAX_VALUE),
        soldDateRange: Range<Long> = Range(0L, Long.MAX_VALUE),
        surfaceRange: Range<Int> = Range(0, Int.MAX_VALUE),
        sellingStatus: SellingStatus = SellingStatus.ALL,
        tagSchool: Boolean = false,
        tagTransport: Boolean = false,
        tagShop: Boolean = false,
        tagPark: Boolean = false,
        areaCodeFilter: Int? = null,
        typeHousing: String? = null,
        minNbrPhotos: Int = 0
    ): Filter {
        return Filter(
            sortType = sortType,
            priceOrder = priceOrder,
            dateOrder = dateOrder,
            surfaceOrder = surfaceOrder,
            priceRange = priceRange,
            dateRange = dateRange,
            soldDateRange = soldDateRange,
            surfaceRange = surfaceRange,
            sellingStatus = sellingStatus,
            tagSchool = tagSchool,
            tagTransport = tagTransport,
            tagShop = tagShop,
            tagPark = tagPark,
            areaCodeFilter = areaCodeFilter,
            typeHousing = typeHousing,
            minNbrPhotos = minNbrPhotos
        )
    }
}
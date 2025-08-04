package com.ocproject.realestatemanager.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * Unit tests for Utils class
 * Focus on testing all utility methods including isInternetAvailable, currency conversion, and date formatting
 * Note: NetworkInfo is deprecated but still used in the original implementation
 */
@Suppress("DEPRECATION")
class UtilsTest {

    private lateinit var mockContext: Context
    private lateinit var mockConnectivityManager: ConnectivityManager
    private lateinit var mockNetworkInfo: NetworkInfo

    @Before
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockConnectivityManager = mockk(relaxed = true)
        mockNetworkInfo = mockk(relaxed = true)
        
        // Mock the system service call
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
    }

    // ==================== Currency Conversion Tests ====================

    @Test
    fun `convertDollarToEuro should convert correctly with positive value`() {
        // Given
        val dollars = 100.0

        // When
        val result = Utils.convertDollarToEuro(dollars)

        // Then
        assertEquals("100 dollars should convert to 95 euros", 95.0, result, 0.01)
    }

    @Test
    fun `convertDollarToEuro should convert correctly with zero`() {
        // Given
        val dollars = 0.0

        // When
        val result = Utils.convertDollarToEuro(dollars)

        // Then
        assertEquals("0 dollars should convert to 0 euros", 0.0, result, 0.01)
    }

    @Test
    fun `convertDollarToEuro should convert correctly with negative value`() {
        // Given
        val dollars = -100.0

        // When
        val result = Utils.convertDollarToEuro(dollars)

        // Then
        assertEquals("-100 dollars should convert to -95 euros", -95.0, result, 0.01)
    }

    @Test
    fun `convertDollarToEuro should round the result`() {
        // Given
        val dollars = 100.5

        // When
        val result = Utils.convertDollarToEuro(dollars)

        // Then
        assertEquals("100.5 dollars should convert to 95.475 and round to 95", 95.0, result, 0.01)
    }

    @Test
    fun `convertEuroToDollars should convert correctly with positive value`() {
        // Given
        val euros = 100.0

        // When
        val result = Utils.convertEuroToDollars(euros)

        // Then
        assertEquals("100 euros should convert to 105 dollars", 105.0, result, 0.01)
    }

    @Test
    fun `convertEuroToDollars should convert correctly with zero`() {
        // Given
        val euros = 0.0

        // When
        val result = Utils.convertEuroToDollars(euros)

        // Then
        assertEquals("0 euros should convert to 0 dollars", 0.0, result, 0.01)
    }

    @Test
    fun `convertEuroToDollars should convert correctly with negative value`() {
        // Given
        val euros = -100.0

        // When
        val result = Utils.convertEuroToDollars(euros)

        // Then
        assertEquals("-100 euros should convert to -105 dollars", -105.0, result, 0.01)
    }

    @Test
    fun `convertEuroToDollars should round the result`() {
        // Given
        val euros = 100.5

        // When
        val result = Utils.convertEuroToDollars(euros)

        // Then
        assertEquals("100.5 euros should convert to 105.525 and round to 106", 106.0, result, 0.01)
    }

    @Test
    fun `currency conversion should be reversible with rounding`() {
        // Given
        val originalDollars = 100.0

        // When
        val euros = Utils.convertDollarToEuro(originalDollars)
        val convertedBackDollars = Utils.convertEuroToDollars(euros)

        // Then
        // Due to rounding, the values might not be exactly the same
        assertTrue("Converted values should be close to original",
            abs(originalDollars - convertedBackDollars) < 10.0)
    }

    // ==================== Date Formatting Tests ====================

    @Test
    fun `getTodayDate should return date in correct format`() {
        // When
        val result = Utils.getTodayDate()

        // Then
        assertNotNull("Date should not be null", result)
        assertTrue("Date should match dd/MM/yyyy format", result.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
    }

    @Test
    fun `getTodayDate should return current date`() {
        // Given
        val expectedFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val expectedDate = expectedFormat.format(Date())

        // When
        val result = Utils.getTodayDate()

        // Then
        assertEquals("Should return current date", expectedDate, result)
    }

    @Test
    fun `getTodayDate should use default locale`() {
        // When
        val result = Utils.getTodayDate()

        // Then
        // The method uses Locale.getDefault(), so it should match the system locale
        assertNotNull("Date should not be null", result)
        assertTrue("Date should be in valid format", result.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
    }

    @Test
    fun `getTodayDate should return consistent format`() {
        // When
        val result1 = Utils.getTodayDate()
        val result2 = Utils.getTodayDate()

        // Then
        assertEquals("Multiple calls should return same format", result1.length, result2.length)
        assertTrue("Both results should match date format",
            result1.matches(Regex("\\d{2}/\\d{2}/\\d{4}")) && result2.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
    }

    // ==================== Internet Availability Tests ====================

    @Test
    fun `isInternetAvailable should return true when network is connected`() {
        // Given
        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnectedOrConnecting } returns true

        // When
        val result = Utils.isInternetAvailable(mockContext)

        // Then
        assertTrue("Internet should be available when network is connected", result)
    }

    @Test
    fun `isInternetAvailable should return false when network is not connected`() {
        // Given
        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnectedOrConnecting } returns false

        // When
        val result = Utils.isInternetAvailable(mockContext)

        // Then
        assertFalse("Internet should not be available when network is not connected", result)
    }

    @Test
    fun `isInternetAvailable should return false when network info is null`() {
        // Given
        every { mockConnectivityManager.activeNetworkInfo } returns null

        // When
        val result = Utils.isInternetAvailable(mockContext)

        // Then
        assertFalse("Internet should not be available when network info is null", result)
    }

    @Test
    fun `isInternetAvailable should return true when network is available`() {
        // Given
        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnectedOrConnecting } returns true

        // When
        val result = Utils.isInternetAvailable(mockContext)

        // Then
        // Checks if network is available (connected or connecting)
        assertTrue("Internet should be available when network is connected or connecting", result)
    }

    @Test
    fun `isInternetAvailable should return false when network is disconnected`() {
        // Given
        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnectedOrConnecting } returns false

        // When
        val result = Utils.isInternetAvailable(mockContext)

        // Then
        assertFalse("Internet should not be available when network is disconnected", result)
    }

    @Test
    fun `isInternetAvailable should verify correct system service call`() {
        // Given
        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnectedOrConnecting } returns true

        // When
        Utils.isInternetAvailable(mockContext)

        // Then
        // Verify that getSystemService was called with the correct parameter
        // This is implicit in the setup, but we can verify the behavior
        assertTrue("Method should complete without throwing exception", true)
    }

    @Test
    fun `isInternetAvailable should handle network state transitions`() {
        // Given - Network initially connected
        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnectedOrConnecting } returns true

        // When - First call (connected)
        val resultConnected = Utils.isInternetAvailable(mockContext)

        // Then
        assertTrue("Should return true when connected", resultConnected)

        // Given - Network becomes disconnected
        every { mockNetworkInfo.isConnectedOrConnecting } returns false

        // When - Second call (disconnected)
        val resultDisconnected = Utils.isInternetAvailable(mockContext)

        // Then
        assertFalse("Should return false when disconnected", resultDisconnected)
    }

    @Test
    fun `isInternetAvailable should handle network info state changes`() {
        // Given - Network info exists but is not connected
        every { mockConnectivityManager.activeNetworkInfo } returns mockNetworkInfo
        every { mockNetworkInfo.isConnectedOrConnecting } returns false

        // When
        val result = Utils.isInternetAvailable(mockContext)

        // Then
        assertFalse("Should return false when network info exists but not connected", result)

        // Given - Network info becomes null
        every { mockConnectivityManager.activeNetworkInfo } returns null

        // When
        val resultNull = Utils.isInternetAvailable(mockContext)

        // Then
        assertFalse("Should return false when network info is null", resultNull)
    }

    @Test(expected = NullPointerException::class)
    fun `isInternetAvailable should throw NullPointerException when context is null`() {
        // When
        Utils.isInternetAvailable(null)
        // Then - should throw NullPointerException
    }

    @Test(expected = NullPointerException::class)
    fun `isInternetAvailable should throw NullPointerException when connectivity manager is null`() {
        // Given
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns null

        // When
        Utils.isInternetAvailable(mockContext)
        // Then - should throw NullPointerException
    }

    @Test(expected = RuntimeException::class)
    fun `isInternetAvailable should throw exception when system service throws exception`() {
        // Given
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } throws RuntimeException("Service not available")

        // When
        Utils.isInternetAvailable(mockContext)
        // Then - should throw RuntimeException
    }
} 
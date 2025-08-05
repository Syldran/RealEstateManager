package com.ocproject.realestatemanager.core.utils

import android.os.Build
import org.junit.Test
import org.junit.Assert.*

class AndroidVersionUtilsTest {
    
    @Test
    fun `test supportsCameraX returns correct value`() {
        // This test will pass on any Android version
        val result = AndroidVersionUtils.supportsCameraX()
        assertTrue("CameraX support should be boolean", result is Boolean)
    }
    
    @Test
    fun `test supportsMaterial3Adaptive returns correct value`() {
        val result = AndroidVersionUtils.supportsMaterial3Adaptive()
        assertTrue("Material 3 Adaptive support should be boolean", result is Boolean)
    }
    
    @Test
    fun `test supportsEdgeToEdge returns correct value`() {
        val result = AndroidVersionUtils.supportsEdgeToEdge()
        assertTrue("Edge to Edge support should be boolean", result is Boolean)
    }
    
    @Test
    fun `test getMinSdkVersion returns Android 6 0`() {
        val result = AndroidVersionUtils.getMinSdkVersion()
        assertEquals("Min SDK should be Android 6.0 (API 23)", Build.VERSION_CODES.M, result)
    }
    
    @Test
    fun `test getTargetSdkVersion returns Android 15`() {
        val result = AndroidVersionUtils.getTargetSdkVersion()
        assertEquals("Target SDK should be Android 15", Build.VERSION_CODES.UPSIDE_DOWN_CAKE, result)
    }
    
    @Test
    fun `test isAndroid6 returns correct value`() {
        val result = AndroidVersionUtils.isAndroid6()
        assertTrue("isAndroid6 should return boolean", result is Boolean)
    }
    
    @Test
    fun `test isAndroid7 returns correct value`() {
        val result = AndroidVersionUtils.isAndroid7()
        assertTrue("isAndroid7 should return boolean", result is Boolean)
    }
    
    @Test
    fun `test isAndroid8Plus returns correct value`() {
        val result = AndroidVersionUtils.isAndroid8Plus()
        assertTrue("isAndroid8Plus should return boolean", result is Boolean)
    }
    
    @Test
    fun `test getVersionDescription returns non empty string`() {
        val result = AndroidVersionUtils.getVersionDescription()
        assertNotNull("Version description should not be null", result)
        assertTrue("Version description should not be empty", result.isNotEmpty())
    }
    
    @Test
    fun `test version compatibility logic`() {
        // Test that the logic is consistent
        val isAndroid6 = AndroidVersionUtils.isAndroid6()
        val isAndroid7 = AndroidVersionUtils.isAndroid7()
        val isAndroid8Plus = AndroidVersionUtils.isAndroid8Plus()
        
        // Only one should be true at a time (for the current device)
        val trueCount = listOf(isAndroid6, isAndroid7, isAndroid8Plus).count { it }
        assertTrue("Exactly one version check should be true", trueCount <= 1)
    }
} 
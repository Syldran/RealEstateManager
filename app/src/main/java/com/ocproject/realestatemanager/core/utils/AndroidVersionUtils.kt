package com.ocproject.realestatemanager.core.utils

import android.os.Build

/**
 * Utility class to handle Android version-specific functionality
 */
object AndroidVersionUtils {
    
    /**
     * Check if the device supports CameraX (Android 8.0+)
     */
    fun supportsCameraX(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
    
    /**
     * Check if the device supports Material 3 Adaptive (Android 8.0+)
     */
    fun supportsMaterial3Adaptive(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
    
    /**
     * Check if the device supports enableEdgeToEdge (Android 8.0+)
     */
    fun supportsEdgeToEdge(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
    
    /**
     * Get the minimum supported Android version
     */
    fun getMinSdkVersion(): Int {
        return Build.VERSION_CODES.M // Android 6.0
    }
    
    /**
     * Get the target Android version
     */
    fun getTargetSdkVersion(): Int {
        return Build.VERSION_CODES.UPSIDE_DOWN_CAKE // Android 15
    }
    
    /**
     * Check if the device is running Android 6.0 (API 23)
     */
    fun isAndroid6(): Boolean {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.M
    }
    
    /**
     * Check if the device is running Android 7.0-7.1 (API 24-25)
     */
    fun isAndroid7(): Boolean {
        return Build.VERSION.SDK_INT in Build.VERSION_CODES.N..Build.VERSION_CODES.N_MR1
    }
    
    /**
     * Check if the device is running Android 8.0+ (API 26+)
     */
    fun isAndroid8Plus(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
    
    /**
     * Get a user-friendly version description
     */
    fun getVersionDescription(): String {
        return when {
            isAndroid6() -> "Android 6.0 (Marshmallow)"
            isAndroid7() -> "Android 7.0-7.1 (Nougat)"
            isAndroid8Plus() -> "Android 8.0+ (Oreo+)"
            else -> "Unknown Android version"
        }
    }
} 
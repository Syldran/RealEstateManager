package com.ocproject.realestatemanager.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Utility class for adaptive dimensions based on screen size
 */
object AdaptiveDimensions {
    
    @Composable
    fun getSpacingSmall(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 16.dp
            configuration.screenWidthDp >= 600 -> 12.dp
            else -> 8.dp
        }
    }
    
    @Composable
    fun getSpacingMedium(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 32.dp
            configuration.screenWidthDp >= 600 -> 24.dp
            else -> 16.dp
        }
    }
    
    @Composable
    fun getSpacingLarge(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 48.dp
            configuration.screenWidthDp >= 600 -> 32.dp
            else -> 24.dp
        }
    }
    
    @Composable
    fun getSpacingXLarge(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 64.dp
            configuration.screenWidthDp >= 600 -> 48.dp
            else -> 32.dp
        }
    }
    
    @Composable
    fun getTextSmall(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 16.dp
            configuration.screenWidthDp >= 600 -> 14.dp
            else -> 12.dp
        }
    }
    
    @Composable
    fun getTextMedium(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 18.dp
            configuration.screenWidthDp >= 600 -> 16.dp
            else -> 14.dp
        }
    }
    
    @Composable
    fun getTextLarge(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 20.dp
            configuration.screenWidthDp >= 600 -> 18.dp
            else -> 16.dp
        }
    }
    
    @Composable
    fun getTextXLarge(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 28.dp
            configuration.screenWidthDp >= 600 -> 24.dp
            else -> 20.dp
        }
    }
    
    @Composable
    fun getTextHeadline(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 32.dp
            configuration.screenWidthDp >= 600 -> 28.dp
            else -> 24.dp
        }
    }
    
    @Composable
    fun getButtonHeight(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 64.dp
            configuration.screenWidthDp >= 600 -> 56.dp
            else -> 48.dp
        }
    }
    
    @Composable
    fun getIconSize(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 40.dp
            configuration.screenWidthDp >= 600 -> 32.dp
            else -> 24.dp
        }
    }
    
    @Composable
    fun getCardCornerRadius(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 16.dp
            configuration.screenWidthDp >= 600 -> 12.dp
            else -> 8.dp
        }
    }
    
    @Composable
    fun getCardElevation(): Dp {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp >= 720 -> 6.dp
            configuration.screenWidthDp >= 600 -> 4.dp
            else -> 2.dp
        }
    }
    
    @Composable
    fun isTablet(): Boolean {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp >= 600
    }
    
    @Composable
    fun isLargeTablet(): Boolean {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp >= 720
    }
} 
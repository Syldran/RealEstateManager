package com.ocproject.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.core.ui.theme.RealEstateManagerTheme
import com.ocproject.realestatemanager.core.utils.Globals.checkConnectivityForTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // use createAndroidComposeRule<YourActivity>() if you need access to an activity
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ocproject.realestatemanager", appContext.packageName)
    }

    @Test
    fun testInternetOnAutocomplete() {
        // Start app
        composeTestRule.setContent {
            RealEstateManagerTheme {
                RealEstateManagerApp(
                    currentLocation = LatLng(0.0, 0.0),
                    darkTheme = false,
                    dynamicColor = false
                )
            }
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Act
        composeTestRule.onNodeWithTag("topBarAdd").performClick()
        composeTestRule.onNodeWithText("Select Location").performClick()

        // Assert
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {

            assertEquals(checkConnectivityForTest, true)
        } else {
            assertEquals(checkConnectivityForTest, false)
            composeTestRule.onNodeWithText("Error connexion").assertIsDisplayed()
        }
    }
}
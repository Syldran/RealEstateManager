package com.ocproject.realestatemanager

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ocproject.realestatemanager.core.ui.theme.RealestatemanagerTheme
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyScreen
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.compose.koinViewModel

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
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
    fun myTest() {
        // Start the app
        composeTestRule.setContent {
            RealestatemanagerTheme {
//                MainScreen(uiState = fakeUiState /*...*/)
//                AddPropertyScreen()
            }
        }

        composeTestRule.onNodeWithText("Select Location").performClick()

        composeTestRule.onNodeWithText("erreur co").assertIsDisplayed()
    }
}


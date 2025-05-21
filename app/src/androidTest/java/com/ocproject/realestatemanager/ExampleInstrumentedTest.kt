package com.ocproject.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.core.ui.theme.RealestatemanagerTheme
import com.ocproject.realestatemanager.presentation.navigation.Screen
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyScreen
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetails
import okhttp3.internal.wait
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
                RealEstateManagerApp(
                    currentLocation = LatLng(0.0, 0.0),
                    darkTheme = false,
                    dynamicColor = false
                )
            }
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Act
        composeTestRule.onNodeWithTag("topBarMenuIcon").performClick()
        composeTestRule.onNodeWithTag("menuItemAdd").performClick()
        composeTestRule.onNodeWithText("Select Location").performClick()

        // Assert
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            // Tester avec boolean.


        } else {
            // Vérifier que le message d'erreur s'affiche
            composeTestRule.onNodeWithText("Error connexion").assertIsDisplayed()
        }
    }
    fun myTest2() {
        // Start the app
        composeTestRule.setContent {
            RealestatemanagerTheme {
                RealEstateManagerApp(
                    currentLocation = LatLng(0.0, 0.0),
                    darkTheme = false,
                    dynamicColor = false
                )
            }
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Act
        composeTestRule.onNodeWithTag("topBarMenuIcon").performClick()
        composeTestRule.onNodeWithTag("menuItemAdd").performClick()
        composeTestRule.onNodeWithText("Select Location").performClick()

        // Assert
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            // Tester avec boolean.

//            adb shell svc wifi enable
//            adb shell svc data enable
//            composeTestRule.onNodeWithText("Search location").assertExists()
//            composeTestRule.waitUntil(timeoutMillis = 5_000) {
//                composeTestRule
//                    .onAllNodesWithText("Chargement terminé")
//                    .fetchSemanticsNodes().isNotEmpty()
//            }
//            composeTestRule.onNodeWithText("Search location").performTextInput("Eiffel Tower")
//            composeTestRule.onNodeWithText("Eiffel Tower").performClick()

        } else {
            // Vérifier que le message d'erreur s'affiche
            composeTestRule.onNodeWithText("Error connexion").assertIsDisplayed()
        }
    }
}

/*
interface NetworkChecker {
    fun isConnected(): Boolean
}

class RealNetworkChecker(private val context: Context) : NetworkChecker {
    override fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

class FakeNetworkChecker(private val connected: Boolean) : NetworkChecker {
    override fun isConnected(): Boolean = connected
}

//test
@RunWith(AndroidJUnit4::class)
class SimulatedNetworkTest {

    @Test
    fun testWithNetworkAvailable() {
        val checker = FakeNetworkChecker(true)
        assertTrue("Should simulate internet connection", checker.isConnected())
    }

    @Test
    fun testWithNetworkUnavailable() {
        val checker = FakeNetworkChecker(false)
        assertTrue("Should simulate no internet connection", !checker.isConnected())
    }
}

package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.ocproject.realestatemanager.MainActivity
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module



class AddPropertyScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        // Initialisation de Koin pour les tests
        startKoin {
            modules(
                module {
                    viewModel { AddPropertyViewModel(null, get(), get()) }
                }
            )
        }

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setCurrentDestination(Screen.AddPropertyScreen.route)
    }

    @Test
    fun testAddPropertyScreen_AllFieldsFilled_Success() {
        // Arrange
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = {},
            )
        }

        // Act
        // Remplir le formulaire
        composeTestRule.onNodeWithText("Address").performTextInput("123 Main St")
        composeTestRule.onNodeWithText("Town").performTextInput("Paris")
        composeTestRule.onNodeWithText("Area Code").performTextInput("75001")
        composeTestRule.onNodeWithText("Country").performTextInput("France")
        composeTestRule.onNodeWithText("Price").performTextInput("500000")
        composeTestRule.onNodeWithText("Surface Area").performTextInput("100")
        composeTestRule.onNodeWithText("Latitude").performTextInput("48.8566")
        composeTestRule.onNodeWithText("Longitude").performTextInput("2.3522")

        // Vérifier que les champs sont remplis correctement
        composeTestRule.onNodeWithText("123 Main St").assertExists()
        composeTestRule.onNodeWithText("Paris").assertExists()
        composeTestRule.onNodeWithText("75001").assertExists()
        composeTestRule.onNodeWithText("France").assertExists()
        composeTestRule.onNodeWithText("500000").assertExists()
        composeTestRule.onNodeWithText("100").assertExists()
        composeTestRule.onNodeWithText("48.8566").assertExists()
        composeTestRule.onNodeWithText("2.3522").assertExists()
    }

    @Test
    fun testAddPropertyScreen_RequiredFieldsEmpty_ShowErrors() {
        // Arrange
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = {},
            )
        }

        // Act
        // Cliquer sur le bouton de sauvegarde sans remplir les champs
        composeTestRule.onNodeWithText("Save").performClick()

        // Assert
        // Vérifier que les messages d'erreur sont affichés
        composeTestRule.onNodeWithText("Address is required").assertExists()
        composeTestRule.onNodeWithText("Town is required").assertExists()
        composeTestRule.onNodeWithText("Area code is required").assertExists()
        composeTestRule.onNodeWithText("Country is required").assertExists()
        composeTestRule.onNodeWithText("Price is required").assertExists()
        composeTestRule.onNodeWithText("Surface area is required").assertExists()
    }

    @Test
    fun testAddPropertyScreen_InvalidNumericValues_ShowErrors() {
        // Arrange
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = {},
            )
        }

        // Act
        // Remplir les champs numériques avec des valeurs invalides
        composeTestRule.onNodeWithText("Price").performTextInput("abc")
        composeTestRule.onNodeWithText("Surface Area").performTextInput("-100")
        composeTestRule.onNodeWithText("Area Code").performTextInput("12345")

        // Assert
        // Vérifier que les messages d'erreur appropriés sont affichés
        composeTestRule.onNodeWithText("Price must be a valid number").assertExists()
        composeTestRule.onNodeWithText("Surface area must be positive").assertExists()
        composeTestRule.onNodeWithText("Area code must be valid").assertExists()
    }

    @Test
    fun testAddPropertyScreen_LocationSearch_Works() {
        // Arrange
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = {},
            )
        }

        // Act
        // Utiliser la recherche de localisation
        composeTestRule.onNodeWithText("Search location").performTextInput("Eiffel Tower")

        // Assert
        // Vérifier que les suggestions apparaissent
        composeTestRule.onNodeWithText("Eiffel Tower").assertExists()
    }

    @Test
    fun testAddPropertyScreen_PhotoUpload_Works() {
        // Arrange
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = {},
            )
        }

        // Act
        // Cliquer sur le bouton d'ajout de photo
        composeTestRule.onNodeWithContentDescription("Add photo").performClick()

        // Assert
        // Vérifier que le sélecteur de photos s'ouvre
        composeTestRule.onNodeWithText("Choose photos").assertExists()
    }

    @Test
    fun testAddPropertyScreen_Navigation_Works() {
        // Arrange
        var navigationCalled = false
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = { navigationCalled = true },
            )
        }

        // Act
        // Remplir le formulaire avec des données valides
        composeTestRule.onNodeWithText("Address").performTextInput("123 Main St")
        composeTestRule.onNodeWithText("Town").performTextInput("Paris")
        composeTestRule.onNodeWithText("Area Code").performTextInput("75001")
        composeTestRule.onNodeWithText("Country").performTextInput("France")
        composeTestRule.onNodeWithText("Price").performTextInput("500000")
        composeTestRule.onNodeWithText("Surface Area").performTextInput("100")
        composeTestRule.onNodeWithText("Latitude").performTextInput("48.8566")
        composeTestRule.onNodeWithText("Longitude").performTextInput("2.3522")

        // Cliquer sur le bouton de sauvegarde
        composeTestRule.onNodeWithText("Save").performClick()

        // Assert
        // Vérifier que la navigation a été déclenchée
        assert(navigationCalled)
    }
}

//******************

package com.ocproject.realestatemanager.presentation.scene.addproperty

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.ocproject.realestatemanager.MainActivity
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class AddPropertyScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        // Initialisation de Koin pour les tests
        startKoin {
            modules(
                module {
                    viewModel { AddPropertyViewModel(null, get(), get()) }
                }
            )
        }

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setCurrentDestination(Screen.AddPropertyScreen.route)
    }

    @Test
    fun testNetworkConnection_Available() {
        // Arrange
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        // Assert
        assert(capabilities != null)
        assert(capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true)
    }

    @Test
    fun testAddPropertyScreen_NetworkRequiredFeatures_WorkWithConnection() {
        // Arrange
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        // Vérifier que la connexion est disponible avant de tester les fonctionnalités réseau
        if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            composeTestRule.setContent {
                AddPropertyScreen(
                    onNavigateToListDetails = {},
                )
            }

            // Act & Assert
            // Tester la recherche de localisation (nécessite une connexion)
            composeTestRule.onNodeWithText("Search location").performTextInput("Eiffel Tower")
            composeTestRule.onNodeWithText("Eiffel Tower").assertExists()

            // Tester l'ajout de photos (peut nécessiter une connexion pour le téléchargement)
            composeTestRule.onNodeWithContentDescription("Add photo").performClick()
            composeTestRule.onNodeWithText("Choose photos").assertExists()
        } else {
            // Si pas de connexion, on ne peut pas tester ces fonctionnalités
            assert(false) { "Network connection required for this test" }
        }
    }

    @Test
    fun testAddPropertyScreen_OfflineFeatures_WorkWithoutConnection() {
        // Arrange
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = {},
            )
        }

        // Act & Assert
        // Tester les fonctionnalités qui ne nécessitent pas de connexion
        composeTestRule.onNodeWithText("Address").performTextInput("123 Main St")
        composeTestRule.onNodeWithText("Town").performTextInput("Paris")
        composeTestRule.onNodeWithText("Area Code").performTextInput("75001")
        composeTestRule.onNodeWithText("Country").performTextInput("France")
        composeTestRule.onNodeWithText("Price").performTextInput("500000")
        composeTestRule.onNodeWithText("Surface Area").performTextInput("100")

        // Vérifier que les champs sont remplis correctement
        composeTestRule.onNodeWithText("123 Main St").assertExists()
        composeTestRule.onNodeWithText("Paris").assertExists()
        composeTestRule.onNodeWithText("75001").assertExists()
        composeTestRule.onNodeWithText("France").assertExists()
        composeTestRule.onNodeWithText("500000").assertExists()
        composeTestRule.onNodeWithText("100").assertExists()
    }

    @Test
    fun testAddPropertyScreen_AllFieldsFilled_Success() {
        // Arrange
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = {},
            )
        }

        // Act
        // Remplir le formulaire
        composeTestRule.onNodeWithText("Address").performTextInput("123 Main St")
        composeTestRule.onNodeWithText("Town").performTextInput("Paris")
        composeTestRule.onNodeWithText("Area Code").performTextInput("75001")
        composeTestRule.onNodeWithText("Country").performTextInput("France")
        composeTestRule.onNodeWithText("Price").performTextInput("500000")
        composeTestRule.onNodeWithText("Surface Area").performTextInput("100")
        composeTestRule.onNodeWithText("Latitude").performTextInput("48.8566")
        composeTestRule.onNodeWithText("Longitude").performTextInput("2.3522")

        // Vérifier que les champs sont remplis correctement
        composeTestRule.onNodeWithText("123 Main St").assertExists()
        composeTestRule.onNodeWithText("Paris").assertExists()
        composeTestRule.onNodeWithText("75001").assertExists()
        composeTestRule.onNodeWithText("France").assertExists()
        composeTestRule.onNodeWithText("500000").assertExists()
        composeTestRule.onNodeWithText("100").assertExists()
        composeTestRule.onNodeWithText("48.8566").assertExists()
        composeTestRule.onNodeWithText("2.3522").assertExists()
    }

    @Test
    fun testAddPropertyScreen_RequiredFieldsEmpty_ShowErrors() {
        // Arrange
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = {},
            )
        }

        // Act
        // Cliquer sur le bouton de sauvegarde sans remplir les champs
        composeTestRule.onNodeWithText("Save").performClick()

        // Assert
        // Vérifier que les messages d'erreur sont affichés
        composeTestRule.onNodeWithText("Address is required").assertExists()
        composeTestRule.onNodeWithText("Town is required").assertExists()
        composeTestRule.onNodeWithText("Area code is required").assertExists()
        composeTestRule.onNodeWithText("Country is required").assertExists()
        composeTestRule.onNodeWithText("Price is required").assertExists()
        composeTestRule.onNodeWithText("Surface area is required").assertExists()
    }

    @Test
    fun testAddPropertyScreen_InvalidNumericValues_ShowErrors() {
        // Arrange
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = {},
            )
        }

        // Act
        // Remplir les champs numériques avec des valeurs invalides
        composeTestRule.onNodeWithText("Price").performTextInput("abc")
        composeTestRule.onNodeWithText("Surface Area").performTextInput("-100")
        composeTestRule.onNodeWithText("Area Code").performTextInput("12345")

        // Assert
        // Vérifier que les messages d'erreur appropriés sont affichés
        composeTestRule.onNodeWithText("Price must be a valid number").assertExists()
        composeTestRule.onNodeWithText("Surface area must be positive").assertExists()
        composeTestRule.onNodeWithText("Area code must be valid").assertExists()
    }

    @Test
    fun testAddPropertyScreen_Navigation_Works() {
        // Arrange
        var navigationCalled = false
        composeTestRule.setContent {
            AddPropertyScreen(
                onNavigateToListDetails = { navigationCalled = true },
            )
        }

        // Act
        // Remplir le formulaire avec des données valides
        composeTestRule.onNodeWithText("Address").performTextInput("123 Main St")
        composeTestRule.onNodeWithText("Town").performTextInput("Paris")
        composeTestRule.onNodeWithText("Area Code").performTextInput("75001")
        composeTestRule.onNodeWithText("Country").performTextInput("France")
        composeTestRule.onNodeWithText("Price").performTextInput("500000")
        composeTestRule.onNodeWithText("Surface Area").performTextInput("100")
        composeTestRule.onNodeWithText("Latitude").performTextInput("48.8566")
        composeTestRule.onNodeWithText("Longitude").performTextInput("2.3522")

        // Cliquer sur le bouton de sauvegarde
        composeTestRule.onNodeWithText("Save").performClick()

        // Assert
        // Vérifier que la navigation a été déclenchée
        assert(navigationCalled)
    }
}


//select location
@Test
fun testAutocompleteSearch_ButtonClick_NetworkStateChanges() {
    // Arrange
    composeTestRule.setContent {
        AutocompleteSearch(viewModel)
    }

    // Act
    composeTestRule.onNodeWithText("Select Location").performClick()

    // Assert
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)

    // Vérifier le comportement en fonction de l'état de la connexion
    if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
        // Vérifier que l'interface de recherche s'ouvre
        composeTestRule.onNodeWithText("Search location").assertExists()
    } else {
        // Vérifier que le message d'erreur s'affiche
        composeTestRule.onNodeWithText("erreur co").assertExists()
    }
}


 */
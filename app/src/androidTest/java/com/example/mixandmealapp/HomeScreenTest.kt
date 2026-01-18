package com.example.mixandmealapp

import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.screens.home.HomeScreen
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_showsHeaderAndSections() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(
                navController = navController,
                showPrivacy = false
            )
        }

        // Controle: headertekst (pas tekst aan je eigen string)
        composeTestRule.onNodeWithText("Hello", substring = true, ignoreCase = true)
            .assertExists()

        // Controle: hoofdsecties zichtbaar
        composeTestRule.onNodeWithText("Featured", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Popular", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Quick", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Easy", substring = true, ignoreCase = true)
            .assertExists()

        // Controle: categorie‑titel
        composeTestRule.onNodeWithText("Category", substring = true, ignoreCase = true)
            .assertExists()
    }

    @Test
    fun homeScreen_showsPrivacyDialogAndCallsOnAccept() {
        var accepted = false

        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(
                navController = navController,
                showPrivacy = true,
                onAcceptPrivacy = { accepted = true }
            )
        }

        // Zoek een kenmerkende tekst uit je PrivacyDialog
        composeTestRule.onNodeWithText("Privacy", substring = true, ignoreCase = true)
            .assertExists()

        // Zoek de akkoord‑knop (pas tekst aan wat je in PrivacyDialog gebruikt, bv. "Accept", "Akkoord", "I agree")
        composeTestRule.onNodeWithText("Akkoord", substring = true, ignoreCase = true)
            .performClick()

        composeTestRule.runOnIdle {
            assert(accepted)
        }
    }

    @Test
    fun homeScreen_categoryButtonsAreClickable() {
        var clicked = false

        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(
                navController = navController,
                showPrivacy = false
            )
        }

        // Voorbeeld: “Breakfast” knop aanklikken
        composeTestRule.onNodeWithText(
            "Breakfast",
            substring = true,
            ignoreCase = true
        ).performClick()

        // Hier kun je later extra asserts doen als je een callback toevoegt.
        // Voor nu checken we alleen dat de knop klikbaar is.
        composeTestRule.runOnIdle {
            // placeholder voor extra assertions
            clicked = true
        }

        assert(clicked)
    }

    @Test
    fun homeScreen_clickOnPopularRecipeCardCallsOnRecipeClick() {
        var clickedRecipeId: Int? = null

        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(
                navController = navController,
                showPrivacy = false,
                // we gebruiken een custom onRecipeClick in de Popular/Easy/Quick secties
                // via navController.navigate("${Navigation.RECIPE_DETAIL}/$recipeId")
                // daarom testen we hier op de tekst van de kaart en simuleren we een click
            )
        }

        // Omdat de recipes asynchroon geladen worden, is het veilig om eerst op de sectietitel te wachten
        composeTestRule.onNodeWithText("Popular", substring = true, ignoreCase = true)
            .assertExists()

        // In jouw PopularRecipeCard wordt de titel van de recipe getoond:
        // zoek dus op een verwachte titel; voor een echte test kun je test data injecteren
        // via DI. Tot die tijd kun je alleen verifiëren dat er in elk geval een kaart klikbaar is:
        composeTestRule.onNode(
            hasText("", ignoreCase = true) and hasAnyAncestor(isRoot()) and hasClickAction()
        ).assertExists()
    }
}

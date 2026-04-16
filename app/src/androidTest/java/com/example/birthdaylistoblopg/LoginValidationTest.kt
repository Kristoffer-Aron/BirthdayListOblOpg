package com.example.birthdaylistoblopg

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.birthdaylistoblopg.screens.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginValidationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun validationTest() {
        composeTestRule.setContent {
            LoginScreen(
                signIn = { email, password -> },
                register = { email, password -> },
                onNavigateToListPage = {}
            )
        }
        Thread.sleep(5000)
        composeTestRule.onNodeWithText("testpass@mail.dk").performTextClearance()
        composeTestRule.onNodeWithText("Sign In").performClick()
        composeTestRule.onNodeWithText("Invalid email").assertExists()

        Thread.sleep(3000)
        composeTestRule.onNodeWithText("Email").performTextInput("testpass@mail.dk")
        composeTestRule.onNodeWithText("Password").performTextClearance()
        composeTestRule.onNodeWithText("Sign In").performClick()
        composeTestRule.onNodeWithText("Invalid password").assertExists()
        Thread.sleep(3000)
    }
}

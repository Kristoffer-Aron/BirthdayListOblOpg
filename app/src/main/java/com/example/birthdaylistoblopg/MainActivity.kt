package com.example.birthdaylistoblopg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.birthdaylistoblopg.screens.AddScreen
import com.example.birthdaylistoblopg.screens.ListScreen
import com.example.birthdaylistoblopg.screens.LoginScreen
import com.example.birthdaylistoblopg.screens.PersonScreen
import com.example.birthdaylistoblopg.ui.theme.BirthdayListOblOpgTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BirthdayListOblOpgTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            // TODO don't pass navController to Home
            LoginScreen(
                onNavigateToListPage = {navController.navigate(NavRoutes.List.route)}
            )
        }
        composable(NavRoutes.List.route)
        {
            ListScreen(
                onNavigateToAddPage = {navController.navigate(NavRoutes.Add.route)},
                onNavigateToEditPage = {navController.navigate(NavRoutes.Edit.route)}
            )
        }
        composable(NavRoutes.Add.route)
        {
            AddScreen(
                onNavigateToListPage = {navController.navigate(NavRoutes.List.route)},
                navigateBack = {navController.popBackStack()}
            )
        }
        composable(NavRoutes.Edit.route)
        {
            PersonScreen(
                onNavigateToListPage = {navController.navigate(NavRoutes.List.route)},
                navigateBack = {navController.popBackStack()}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdayPreview() {
    BirthdayListOblOpgTheme {
        MainScreen()
    }
}
package com.example.birthdaylistoblopg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.birthdaylistoblopg.screens.AddScreen
import com.example.birthdaylistoblopg.screens.ListScreen
import com.example.birthdaylistoblopg.screens.LoginScreen
import com.example.birthdaylistoblopg.screens.PersonScreen
import com.example.birthdaylistoblopg.ui.theme.BirthdayListOblOpgTheme
import com.example.birthdaylistoblopg.viewmodel.AuthViewModel
import com.example.birthdaylistoblopg.viewmodel.PersonViewModel
import org.koin.androidx.compose.koinViewModel

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
fun MainScreen(
    authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val personViewModel: PersonViewModel = koinViewModel()
    val personUIState: PersonUIState by personViewModel.personUIState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            // TODO don't pass navController to Home
            LoginScreen(
                user = authViewModel.user,
                message = authViewModel.message,
                signIn = { email, password -> authViewModel.signIn(email, password) },
                register = authViewModel::register,
                onNavigateToListPage = {navController.navigate(NavRoutes.List.route)}
            )
        }
        composable(NavRoutes.List.route)
        {
            val user = authViewModel.user
            LaunchedEffect(user) {
                if (user != null) {
                    personViewModel.getPersons(user.email ?: "")
                } else {
                    personViewModel.clearPersons()
                }
            }
            ListScreen(
                user = user,
                onSignOut = { authViewModel.signOut() },
                personUIState = personUIState,
                onNavigateToLoginPage = {navController.navigate(NavRoutes.Home.route)},
                onNavigateToAddPage = {navController.navigate(NavRoutes.Add.route)},
                onNavigateToEditPage = { person ->
                    personViewModel.selectedPerson = person
                    navController.navigate(NavRoutes.Edit.route)
                }
            )
        }
        composable(NavRoutes.Add.route)
        {
            AddScreen(
                user = authViewModel.user,
                onSignOut = { authViewModel.signOut() },
                onNavigateToLoginPage = {navController.navigate(NavRoutes.Home.route)},
                onNavigateToListPage = {navController.navigate(NavRoutes.List.route)},
                navigateBack = {navController.popBackStack()},
                addPerson = {person -> personViewModel.addPerson(person)}
            )
        }
        composable(NavRoutes.Edit.route)
        {
            PersonScreen(
                person = personViewModel.selectedPerson,
                user = authViewModel.user,
                onSignOut = { authViewModel.signOut() },
                onNavigateToLoginPage = {navController.navigate(NavRoutes.Home.route)},
                onNavigateToListPage = {navController.navigate(NavRoutes.List.route)},
                navigateBack = {navController.popBackStack()},
                deletePerson = { id -> personViewModel.deletePerson(id) },
                updatePerson = { id, person -> personViewModel.updatePerson(id, person) }
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
package com.example.birthdaylistoblopg

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("home")
    data object List : NavRoutes("list")
    data object Add : NavRoutes("add")
    data object Edit : NavRoutes("edit/{id}")

}
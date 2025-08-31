package com.tibolatte.milbadge.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Badges", Icons.Rounded.Star)
    object Profile : Screen("profile", "Profil", Icons.Rounded.Person)
    object Settings : Screen("settings", "Paramètres", Icons.Rounded.Settings)
}

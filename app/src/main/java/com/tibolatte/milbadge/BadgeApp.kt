package com.tibolatte.milbadge

import BadgeDetailScreen
import android.app.Application
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibolatte.milbadge.data.BadgeEngine
import com.tibolatte.milbadge.navigation.ProfileScreen
import com.tibolatte.milbadge.navigation.Screen
import com.tibolatte.milbadge.navigation.SettingsScreen
import com.tibolatte.milbadge.screens.HomeScreen
import com.tibolatte.milbadge.screens.HomeViewModel
import com.tibolatte.milbadge.screens.HomeViewModelFactory

@Composable
fun BadgeApp() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext as Application
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))

    val badgeEngine = remember { BadgeEngine(homeViewModel) }




    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController, homeViewModel)
            }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable("badgeDetail/{badgeId}") { backStackEntry ->
                val badgeId = backStackEntry.arguments?.getString("badgeId")?.toInt() ?: 0
                BadgeDetailScreen(
                    badgeId = badgeId,
                    homeViewModel = homeViewModel,
                    onClose = { navController.popBackStack() }
                )
            }

        }
    }
}
@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(Screen.Home, Screen.Profile, Screen.Settings)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            val isSelected = currentRoute == screen.route
            val scale = animateFloatAsState(if (isSelected) 1.2f else 1f)
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label, modifier = Modifier.scale(scale.value)) },
                label = { Text(screen.label) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
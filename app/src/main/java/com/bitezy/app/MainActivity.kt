package com.bitezy.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bitezy.app.ui.dashboard.GoalsScreen
import com.bitezy.app.ui.dashboard.LogScreen
import com.bitezy.app.ui.dashboard.MonthScreen
import com.bitezy.app.ui.dashboard.TodayScreen
import com.bitezy.app.ui.onboarding.GetStartedScreen
import com.bitezy.app.ui.onboarding.OnboardingScreen
import com.bitezy.app.ui.theme.BitezyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "get_started") {
                composable("get_started") {
                    BitezyTheme(darkTheme = true) {
                        GetStartedScreen(onGetStartedClick = {
                            navController.navigate("onboarding") {
                                popUpTo("get_started") { inclusive = true }
                            }
                        })
                    }
                }
                composable("onboarding") {
                    OnboardingScreen(onFinish = {
                        navController.navigate("main") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    })
                }
                composable("main") {
                    BitezyTheme {
                        BitezyApp()
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Log : Screen("log", "Log", Icons.Filled.AddCircle)
    object Today : Screen("today", "Today", Icons.Filled.List)
    object Month : Screen("month", "Month", Icons.Filled.DateRange)
    object Goals : Screen("goals", "Goals", Icons.Filled.Person)
}

@Composable
fun BitezyApp() {
    val navController = rememberNavController()
    val items = listOf(Screen.Log, Screen.Today, Screen.Month, Screen.Goals)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Today.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Log.route) { 
                LogScreen(onBack = { 
                    navController.navigate(Screen.Today.route) {
                        popUpTo(Screen.Today.route) { inclusive = true }
                    }
                }) 
            }
            composable(Screen.Today.route) { TodayScreen() }
            composable(Screen.Month.route) { MonthScreen() }
            composable(Screen.Goals.route) { GoalsScreen() }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }
}

package com.example.coollib.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.coollib.ui.navigation.screens

@Composable
fun BottomBar(navController: NavController) {

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    NavigationBar {

        screens.forEach { screen ->

            NavigationBarItem(

                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                },

                label = {
                    Text(stringResource(screen.labelResourceId))
                },

                selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route == screen.route } == true,

                onClick = {

                    navController.navigate(screen.route) {

                        popUpTo(navController.graph.findStartDestination().id)

                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
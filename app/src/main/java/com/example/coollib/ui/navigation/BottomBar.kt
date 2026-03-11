package com.example.coollib.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(
    navController: NavController
) {

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {

        screens.forEach { screen ->

            NavigationBarItem(

                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null
                    )
                },

                label = {
                    Text(stringResource(screen.labelResourceId))
                },

                selected = currentRoute == screen.route,

                onClick = {

                    navController.navigate(screen.route) {

                        // 避免重复创建页面
                        popUpTo(navController.graph.startDestinationId) {
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
package com.bluechat.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bluechat.R

@Composable
fun AppNavigation(
    navController: NavController,
    onThemeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val bottomBarScreens = Screen.bottomBarScreens
    val showBottomBar = bottomBarScreens.any { it.route == currentRoute?.substringBefore('/') }
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                    ),
                    tonalElevation = 8.dp
                ) {
                    bottomBarScreens.forEach { screen ->
                        val selected = currentRoute == screen.route
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon!!,
                                    contentDescription = stringResource(id = R.string.cd_bottom_nav_item, screen.title)
                                )
                            },
                            label = { Text(screen.title) },
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == Screen.Chats.route) {
                FloatingActionButton(
                    onClick = { 
                        navController.navigate(Screen.Contacts.route) 
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.cd_new_chat)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraph(
                navController = navController,
                onThemeChanged = onThemeChanged
            )
        }
    }
}

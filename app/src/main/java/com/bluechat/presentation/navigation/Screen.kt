package com.bluechat.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null,
    val showInBottomBar: Boolean = true
) {
    object Chats : Screen("chats", "Chats", Icons.Default.Chat)
    
    object Chat : Screen(
        route = "chat/{chatId}",
        title = "Chat",
        showInBottomBar = false
    ) {
        fun createRoute(chatId: String) = "chat/$chatId"
        
        val arguments = listOf(
            navArgument("chatId") {
                type = NavType.StringType
            }
        )
    }
    
    object Contacts : Screen("contacts", "Contacts", Icons.Default.Contacts)
    
    object Profile : Screen("profile", "Profile", Icons.Default.Person, showInBottomBar = false)
    
    object Settings : Screen("settings", "Settings", Icons.Default.Settings, showInBottomBar = false)
    
    companion object {
        val bottomBarScreens = listOf(Chats, Contacts)
    }
}

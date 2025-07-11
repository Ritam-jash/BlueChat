package com.bluechat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bluechat.presentation.ui.chat.ChatScreen
import com.bluechat.presentation.ui.chats.ChatsScreen
import com.bluechat.presentation.ui.contacts.ContactsScreen
import com.bluechat.presentation.ui.profile.ProfileScreen
import com.bluechat.presentation.ui.settings.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    onThemeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Chats.route,
        modifier = modifier
    ) {
        composable(Screen.Chats.route) {
            ChatsScreen(
                onChatClick = { chatId ->
                    navController.navigate(Screen.Chat.createRoute(chatId))
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(
            route = Screen.Chat.route,
            arguments = Screen.Chat.arguments
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
            ChatScreen(
                chatId = chatId,
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(Screen.Contacts.route) {
            ContactsScreen(
                onBackClick = { navController.navigateUp() },
                onContactClick = { contactId ->
                    // Handle contact click (e.g., start new chat)
                }
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.navigateUp() },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.navigateUp() },
                onThemeChanged = onThemeChanged
            )
        }
    }
}

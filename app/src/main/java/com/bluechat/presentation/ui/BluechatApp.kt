package com.bluechat.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.bluechat.presentation.navigation.AppNavigation
import com.bluechat.presentation.theme.BluechatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluechatApp() {
    val navController = rememberNavController()
    var isDarkTheme by remember { mutableStateOf(false) }
    
    BluechatTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold { paddingValues ->
                AppNavigation(
                    navController = navController,
                    onThemeChanged = { isDarkTheme = it },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BluechatAppPreview() {
    BluechatTheme {
        BluechatApp()
    }
}

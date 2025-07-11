package com.bluechat.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.bluechat.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Singleton
class AppPreferences @Inject constructor(
    private val context: Context
) {
    private object PreferencesKeys {
        val CURRENT_USER_ID = stringPreferencesKey("current_user_id")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
        val APP_FIRST_LAUNCH = booleanPreferencesKey("app_first_launch")
    }

    val currentUserId: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.CURRENT_USER_ID]
        }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] ?: false
        }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
        }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.APP_FIRST_LAUNCH] ?: true
        }

    suspend fun setCurrentUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_USER_ID] = user.id
            preferences[PreferencesKeys.APP_FIRST_LAUNCH] = false
        }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] = enabled
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun updateLastSyncTime() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_SYNC_TIME] = System.currentTimeMillis()
        }
    }

    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.CURRENT_USER_ID)
            // Keep theme preference
            val theme = preferences[PreferencesKeys.IS_DARK_THEME] ?: false
            preferences.clear()
            preferences[PreferencesKeys.IS_DARK_THEME] = theme
        }
    }
}

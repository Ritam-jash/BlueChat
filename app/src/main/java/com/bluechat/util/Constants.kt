package com.bluechat.util

object Constants {
    // App info
    const val APP_NAME = "BlueChat"
    const val DATABASE_NAME = "bluechat_database"
    
    // Bluetooth
    const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    const val SERVICE_NAME = "BlueChatService"
    const val MESSAGE_DELIMITER = "\n"
    
    // Encryption
    const val ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding"
    const val KEY_LENGTH = 256
    const val IV_LENGTH = 12
    const val TAG_LENGTH = 128
    
    // Network
    const val CONNECTION_TIMEOUT = 30L // seconds
    const val READ_TIMEOUT = 30L // seconds
    const val WRITE_TIMEOUT = 30L // seconds
    
    // Pagination
    const val PAGE_SIZE = 20
    const val INITIAL_LOAD_SIZE = 30
    const val PREFETCH_DISTANCE = 10
    
    // Date Formats
    const val DATE_FORMAT_SHORT = "MMM d, yyyy"
    const val TIME_FORMAT = "h:mm a"
    const val DATE_TIME_FORMAT = "MMM d, yyyy h:mm a"
    
    // Shared Preferences
    const val PREF_NAME = "bluechat_prefs"
    const val PREF_USER_ID = "user_id"
    const val PREF_USER_NAME = "user_name"
    const val PREF_USER_EMAIL = "user_email"
    const val PREF_IS_LOGGED_IN = "is_logged_in"
    const val PREF_DARK_THEME = "dark_theme"
    
    // Request Codes
    const val REQUEST_ENABLE_BT = 1001
    const val REQUEST_DISCOVERABLE = 1002
    const val REQUEST_LOCATION_PERMISSION = 1003
    const val REQUEST_BLUETOOTH_PERMISSION = 1004
    
    // Notification
    const val NOTIFICATION_CHANNEL_ID = "bluechat_messages"
    const val NOTIFICATION_CHANNEL_NAME = "Messages"
    const val NOTIFICATION_ID = 1
    
    // File Upload
    const val MAX_IMAGE_SIZE = 5 * 1024 * 1024 // 5MB
    const val MAX_VIDEO_SIZE = 50 * 1024 * 1024 // 50MB
    const val MAX_FILE_SIZE = 20 * 1024 * 1024 // 20MB
    
    // Validation
    const val MIN_USERNAME_LENGTH = 3
    const val MAX_USERNAME_LENGTH = 20
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_MESSAGE_LENGTH = 2000
    
    // Cache
    const val CACHE_SIZE = 10L * 1024 * 1024 // 10MB
    const val CACHE_MAX_AGE = 60 * 60 // 1 hour in seconds
    
    // WebSocket
    const val WS_RECONNECT_DELAY = 5000L // 5 seconds
    const val WS_MAX_RECONNECT_ATTEMPTS = 5
    
    companion object {
        // MIME Types
        const val MIME_TYPE_IMAGE = "image/*"
        const val MIME_TYPE_VIDEO = "video/*"
        const val MIME_TYPE_AUDIO = "audio/*"
        const val MIME_TYPE_PDF = "application/pdf"
        const val MIME_TYPE_DOC = "application/msword"
        const val MIME_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        
        // File extensions
        val IMAGE_EXTENSIONS = listOf("jpg", "jpeg", "png", "gif", "webp")
        val VIDEO_EXTENSIONS = listOf("mp4", "3gp", "mkv", "webm")
        val AUDIO_EXTENSIONS = listOf("mp3", "wav", "ogg", "m4a")
        val DOCUMENT_EXTENSIONS = listOf("pdf", "doc", "docx", "txt")
    }
}

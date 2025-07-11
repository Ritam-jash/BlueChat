package com.bluechat.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bluechat.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object UiUtils {
    @Composable
    fun Dp.toPx(): Float {
        return with(LocalDensity.current) { this@toPx.toPx() }
    }

    @Composable
    fun Int.pxToDp(): Dp {
        return with(LocalDensity.current) { this@pxToDp.toDp() }
    }

    suspend fun compressImage(
        context: Context,
        imageUri: Uri,
        maxFileSize: Long = Constants.MAX_IMAGE_SIZE,
        quality: Int = 80
    ): ByteArray? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri) ?: return@withContext null
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()

            var scale = 1
            while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale.toDouble(), 2.0)) > 2000000) {
                scale++
            }

            val newOptions = BitmapFactory.Options().apply {
                inSampleSize = scale
                inPreferredConfig = Bitmap.Config.RGB_565
            }

            val newInputStream = context.contentResolver.openInputStream(imageUri) ?: return@withContext null
            val bitmap = BitmapFactory.decodeStream(newInputStream, null, newOptions)
            newInputStream.close()

            var outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

            var outputBytes = outputStream.toByteArray()
            var currentQuality = quality

            while (outputBytes.size > maxFileSize && currentQuality > 20) {
                outputStream = ByteArrayOutputStream()
                currentQuality -= 10
                bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, outputStream)
                outputBytes = outputStream.toByteArray()
            }

            outputStream.close()
            outputBytes
        } catch (e: Exception) {
            Log.e("UiUtils", "Error compressing image", e)
            null
        }
    }

    suspend fun saveImageToCache(context: Context, bitmap: Bitmap, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val cacheDir = context.cacheDir
                val file = File(cacheDir, fileName)
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.flush()
                outputStream.close()
                file.absolutePath
            } catch (e: Exception) {
                Log.e("UiUtils", "Error saving image to cache", e)
                null
            }
        }
    }

    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val now = Date()
        val diff = now.time - timestamp

        return when {
            diff < 60 * 1000 -> "Just now"
            diff < 60 * 60 * 1000 -> "${(diff / (60 * 1000))}m ago"
            diff < 24 * 60 * 60 * 1000 -> SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
            diff < 7 * 24 * 60 * 60 * 1000 -> SimpleDateFormat("EEE h:mm a", Locale.getDefault()).format(date)
            else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
        }
    }

    fun getFileSizeInMB(sizeInBytes: Long): String {
        return String.format("%.1f MB", sizeInBytes / (1024.0 * 1024.0))
    }

    fun getMimeType(url: String): String {
        return when (url.substringAfterLast('.').lowercase()) {
            in Constants.IMAGE_EXTENSIONS -> Constants.MIME_TYPE_IMAGE
            in Constants.VIDEO_EXTENSIONS -> Constants.MIME_TYPE_VIDEO
            in Constants.AUDIO_EXTENSIONS -> Constants.MIME_TYPE_AUDIO
            "pdf" -> Constants.MIME_TYPE_PDF
            "doc", "docx" -> Constants.MIME_TYPE_DOC
            else -> "application/octet-stream"
        }
    }

    fun getFileIcon(mimeType: String): Int {
        return when {
            mimeType.startsWith("image/") -> R.drawable.ic_image
            mimeType.startsWith("video/") -> R.drawable.ic_video
            mimeType.startsWith("audio/") -> R.drawable.ic_audio
            mimeType == Constants.MIME_TYPE_PDF -> R.drawable.ic_pdf
            mimeType == Constants.MIME_TYPE_DOC || mimeType == Constants.MIME_TYPE_DOCX -> R.drawable.ic_document
            else -> R.drawable.ic_file
        }
    }

    @Composable
    fun getDisplayDensity(): Float {
        return LocalDensity.current.density
    }

    fun getCacheSize(context: Context): String {
        val cacheDir = context.cacheDir
        return getFileSizeInMB(cacheDir.walk().fold(0L) { acc, file -> acc + file.length() })
    }

    fun clearCache(context: Context) {
        val cacheDir = context.cacheDir
        cacheDir.deleteRecursively()
    }
}
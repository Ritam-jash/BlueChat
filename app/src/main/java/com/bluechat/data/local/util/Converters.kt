package com.bluechat.data.local.util

import androidx.room.TypeConverter
import com.bluechat.data.local.entity.MessageEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromMessageStatus(status: MessageEntity.MessageStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun toMessageStatus(value: String?): MessageEntity.MessageStatus? {
        return value?.let { enumValueOf<MessageEntity.MessageStatus>(it) }
    }

    @TypeConverter
    fun fromMessageType(type: MessageEntity.MessageType?): String? {
        return type?.name
    }

    @TypeConverter
    fun toMessageType(value: String?): MessageEntity.MessageType? {
        return value?.let { enumValueOf<MessageEntity.MessageType>(it) }
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) return null
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromMap(map: Map<String, String>?): String? {
        return map?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toMap(value: String?): Map<String, String>? {
        if (value == null) return null
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, mapType)
    }
}

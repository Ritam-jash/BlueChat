package com.bluechat.data.local.converters

import androidx.room.TypeConverter
import com.bluechat.data.local.entity.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {
    private val gson = Gson()

    // Date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    // List<String>
    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        if (value == null) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String {
        return gson.toJson(list ?: emptyList())
    }

    // MessageType
    @TypeConverter
    fun toMessageType(value: String?): MessageType? = value?.let { enumValueOf<MessageType>(it) }

    @TypeConverter
    fun fromMessageType(value: MessageType?): String? = value?.name

    // DeliveryStatus
    @TypeConverter
    fun toDeliveryStatus(value: String?): DeliveryStatus? = value?.let { enumValueOf<DeliveryStatus>(it) }

    @TypeConverter
    fun fromDeliveryStatus(value: DeliveryStatus?): String? = value?.name

    // ConversationType
    @TypeConverter
    fun toConversationType(value: String?): ConversationType? = value?.let { enumValueOf<ConversationType>(it) }

    @TypeConverter
    fun fromConversationType(value: ConversationType?): String? = value?.name

    // List<DeviceCapability>
    @TypeConverter
    fun toCapabilityList(value: String?): List<DeviceCapability> {
        if (value.isNullOrEmpty()) return emptyList()
        return value.split(",").mapNotNull { 
            try {
                enumValueOf<DeviceCapability>(it.trim())
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    @TypeConverter
    fun fromCapabilityList(list: List<DeviceCapability>?): String {
        return list?.joinToString(",") { it.name } ?: ""
    }
}

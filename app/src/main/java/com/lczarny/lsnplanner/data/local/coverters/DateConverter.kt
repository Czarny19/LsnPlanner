package com.lczarny.lsnplanner.data.local.coverters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? = date?.toString()
}
package com.dabi.dabi.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class StringListConverter {
    @TypeConverter
    fun fromString(stringListString: String?): List<String>? {
        return stringListString?.split(",")
    }

    @TypeConverter
    fun toString(stringList: List<String>?): String? {

        return stringList?.joinToString(separator = ",")
    }
}

class DateStringConverter {
    private val formatter = SimpleDateFormat(  "yyyy-mm-dd HH:MM")


    @TypeConverter
    fun toDate(str: String?): Date? {
        return str?.let {
            return formatter.parse(it)
        }
    }

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let {
            formatter.format(date)
        }
    }


}
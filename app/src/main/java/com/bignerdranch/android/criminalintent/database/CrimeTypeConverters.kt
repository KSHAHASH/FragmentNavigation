package com.bignerdranch.android.criminalintent.database

import androidx.room.TypeConverter
import java.util.Date

class CrimeTypeConverters {

    //convert to database representation
    @TypeConverter
    fun fromDate(date: Date): Long{
        return date.time
    }

    //convert from database representation
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date{
        return Date(millisSinceEpoch)
    }
}
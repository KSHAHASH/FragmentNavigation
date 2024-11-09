package com.bignerdranch.android.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.criminalintent.Crime

//version defines the version number of database schema, it is used by Room to manage schema changes over time
@Database(entities = [Crime ::class], version=2)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase(){
    //when the database is created, Room will generate concrete implementation of the DAO that you can access
    abstract fun crimeDao(): CrimeDao

}





//migrating from one version to another when there is the change in the database
//as we created a suspect column to implement the implicit intent
val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
//        )
    }
}


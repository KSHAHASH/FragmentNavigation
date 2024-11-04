package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.flow.Flow
import java.util.UUID

//accessing the database using the Repository Pattern


//private constructor cannot be called outside of the class
//it's a singleton pattern, can tell because of private constructor, private variable, companion object which is static
//companion object --> methods and properties that belongs to class itself rather than instance of it

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {


    //Setting up repository properties
    //Room.databaseBuilder--> creates concrete implementation of abstract CrimeDatabase
    private val database: CrimeDatabase = Room
        .databaseBuilder(
            context.applicationContext, // passing application instance which we created in "CriminalIntentApplication"
            CrimeDatabase::class.java, // specifies class of database you are creating, CrimeDatabse which is abstract class is the subclass of RoomDatabase
            DATABASE_NAME // string constant that represent name of database file
        )
        .createFromAsset(DATABASE_NAME)
        .build() // creates the instance of the database and returns object of type CrimeDatabase

    //after setting up repository, fill out CrimeRepository so components can perform operations they need on database
    //add functions for each function in DAO(CrimeDao)
    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()
    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)




    //companion object is singleton that belongs to the class itself
    companion object {
        //holds the single instance of CrimeRepository
        private var INSTANCE: CrimeRepository? = null

        //methods to make CrimeRepository singleton(two methods)
        fun initialize(context: Context) {
            //if INSTANCE is null then there is no instance of CrimeRepository
            //so create a new instance of CrimeRepository and assign it to INSTANCE
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        //accesses the repository
        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")

        }
    }
}
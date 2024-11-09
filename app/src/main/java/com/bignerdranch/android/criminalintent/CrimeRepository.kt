package com.bignerdranch.android.criminalintent

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import com.bignerdranch.android.criminalintent.database.migration_1_2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID

//accessing the database using the Repository Pattern


//private constructor cannot be called outside of the class
//it's a singleton pattern, can tell because of private constructor, private variable, companion object which is static
//companion object --> methods and properties that belongs to class itself rather than instance of it

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {


    //Setting up repository properties
    //Room.databaseBuilder--> creates concrete implementation of abstract CrimeDatabase
    private val database: CrimeDatabase = Room
        .databaseBuilder(
            context.applicationContext, // passing application instance which we created in "CriminalIntentApplication"
            CrimeDatabase::class.java, // specifies class of database you are creating, CrimeDatabse which is abstract class is the subclass of RoomDatabase
            DATABASE_NAME // string constant that represent name of database file
        )
        //previously using from asset which was the database name
        .createFromAsset(DATABASE_NAME)
        //now adding the migration instead
        .addMigrations(migration_1_2)
        .build() // creates the instance of the database and returns object of type CrimeDatabase

    //after setting up repository, fill out CrimeRepository so components can perform operations they need on database
    //add functions for each function in DAO(CrimeDao)
    fun getCrimes(): Flow<List<Crime>> {
        Log.d("CrimeRepository", "Fetching crimes from database")
        return database.crimeDao().getCrimes().onEach { crimes ->
            Log.d("CrimeRepository", "Fetched crimes: $crimes")
        }
    }
    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)
    //it doesn't return a value instead it performs the action like updating database so it doesn't have return type and it is written in this way
    fun updateCrime(crime: Crime) {
        coroutineScope.launch { database.crimeDao().updateCrime(crime) }

    }




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
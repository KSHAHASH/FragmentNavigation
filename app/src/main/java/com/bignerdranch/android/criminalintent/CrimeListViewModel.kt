package com.bignerdranch.android.criminalintent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

//Storing list of Crime objects in this CrimelistViewModel, for the new screen,
//since this data will be displayed on a new screen you will create and new Fragment called CrimelistFragment
//MainActivity will host and instance of CrimeListFragment, which in turn will daiplay the list of crimes on the screen
class CrimeListViewModel : ViewModel() {


    val crimes = mutableListOf<Crime>()

        init {
            Log.d("CrimeListViewModel", "init starting")
            //launching a coroutine using viewModelScope property
            viewModelScope.launch {
                Log.d("CrimeListViewModel", "Coroutine launched")
                    crimes += loadCrimes()
                }
                Log.d("CrimeListViewModel", "Loading crimes finished")
            }


    //creating suspending function
    suspend fun loadCrimes(): List<Crime> {
        val result = mutableListOf<Crime>()
        delay(5000)
        for (i in 0 until 100){
            val crime = Crime(
                id = UUID.randomUUID(),
                title = "Crime #$i",
                date = Date(),
                isSolved = i % 2 == 0,
                requiresPolice = i % 5 == 0
            )
            result += crime
        }
        return result
    }
}
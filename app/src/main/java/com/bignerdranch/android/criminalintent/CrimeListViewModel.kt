package com.bignerdranch.android.criminalintent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

//Storing list of Crime objects in this CrimelistViewModel, for the new screen,
//since this data will be displayed on a new screen you will create and new Fragment called CrimelistFragment
//MainActivity will host and instance of CrimeListFragment, which in turn will daiplay the list of crimes on the screen
class CrimeListViewModel : ViewModel() {

    //accessing singleton instance of the CrimeRepository class, get() is a companion object function in CrimeRepository to provide access to singleton instance
    private val crimeRepository = CrimeRepository.get()
    //val crimes = crimeRepository.getCrimes()

    //first step in setting stateFlow is to create instance of MutableStateFlow and providing initial empty value
    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()
    init {
        //launching a coroutine using viewModelScope property
        viewModelScope.launch {
            crimeRepository.getCrimes().collect(){
                //assigns the value to the _crimes, _crimes.value gives the current list of Crime objects
                //.value is a way to directly access current data in a mutableState Flow

                Log.d("CrimeListViewModel", "Collected crimes: $it")
                _crimes.value = it
            }
        }
    }

}
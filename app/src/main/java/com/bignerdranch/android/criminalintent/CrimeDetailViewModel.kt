package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeDetailViewModel(crimeId: UUID): ViewModel() {
    //get method is a singleton accessor method returning instance of repository to interact with
    private val crimeRepository = CrimeRepository.get()

    //stateFlow that can be updated initially null because Crime data hasn't been loaded yet
    //must be private so that other components cannot modify it
    private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null)
    //read only StateFlow derived from _crime
    val crime: StateFlow<Crime?> = _crime.asStateFlow()

//runs when the instance of CrimeDetailViewModel is created
    init {
        viewModelScope.launch {
            _crime.value = crimeRepository.getCrime(crimeId)
        }
    }
    //after updating the crime in the CrimeDetailFragment, we need to send the user input data
    // that was updated in the CrimeDetailFragment in CrimeDetailViewModel as well
    //(Crime) ->Crime , lamda is a function type that has a Crime object as input and returns new or modified Crime object
    //_crime --> it is a mutableStateFlow, update is a flow provided by StateFlow in kotlin
    //onUpdate(it) takes the oldCrime and returns the modified CrimeObject
    //upDateCrime is a function that takes another functions called onUpdate which takes Crime object as its parameter and returns Crime object
    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { oldCrime ->
            oldCrime?.let { onUpdate(it) } //only update the crime if it sees changes from the before
        }
    }

    //Updating the database when CrimeViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        //calling the updated function outside a coroutine scope
        crime.value?.let { crimeRepository.updateCrime(it) }

    }
}

// creates CrimeDetailViewModel instances with specified crimeId
class CrimeDetailViewModelFactory(
    private val crimeId: UUID
) : ViewModelProvider.Factory {
    //responsible for creating instances of ViewModel here CrimeDetailViewModel
    //create checks modelClass, if it matches creates a new instance of CrimeDetailViewModel using the provided crimeId
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
}
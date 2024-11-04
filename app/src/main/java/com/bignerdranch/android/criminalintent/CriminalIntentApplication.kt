package com.bignerdranch.android.criminalintent

import android.app.Application

//after creating the repository (CrimeRepository)
//since getter function in CrimeRepository will throw and exception if it hasn't been initialized
//so create and Application subclass that extends Application and initialize the CrimeRepository here
class CriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //initialize the repository
        CrimeRepository.initialize(this)
    }
}
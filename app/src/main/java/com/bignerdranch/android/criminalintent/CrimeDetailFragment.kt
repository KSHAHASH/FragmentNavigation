package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeDetailBinding
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeDetailFragment : Fragment() {

    //creating a nullable backing property _binding
    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    //accessing the arguments that was passed in the CrimeDetailFragment inside nav_graph
    private val args: CrimeDetailFragmentArgs by navArgs()
    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }


    //this is the function where you bind and inflate the layouts view unlike in activity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //container-> views parent
        //false -> third parameter tells layout inflate whether to immediately add the inflated view to views parent
        _binding = FragmentCrimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    //to wire up the views of the fragment always in onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//.apply allows us to interact with view bindings in a concise way
        //it means we are configuring or updating binding object that represents all of the views in the layout
        binding.apply {
            //adding listener on a view, lambda is invoked with four parameters, only care about first one
            //text  --> current text in the EditText in layout
            //creates the copy of the crime object and updates its property title with the text entered by user
            //we are copying but not directly assigning title because it is immutable as declared by val
            //so we are creating a new instance of the crime
            //doOnTextChanged is a listener for EditText class

            //responding to the user input
            crimeTitle.doOnTextChanged { text, _, _, _ ->
            crimeDetailViewModel.updateCrime { oldCrime ->
                oldCrime.copy(title = text.toString())
            }
            }

            //disabling button, apply is used to configure an object, inside apply you can access
            //objects property directly, text is a property in crimeDate button
            crimeDate.apply {
//
                isEnabled = false
            }

            //listening for checkboxes, similarly listener on checkbox

            //responding to the user input
            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }
        }

        //updating your UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    //let function is called only when crime is not null, it refers to non null crime object
                    crime?.let { updateUi(it) }
                }
            }
        }

    }

    //onDestroyView() is called when the fragments view is about to be destroyed, in this case we are setting the backing property of _binding to null
    //which releases the references of the binding object,
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //function to update the UI of the CrimeDetailFragment
    private fun updateUi(crime: Crime) {
        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = crime.date.toString()
            crimeSolved.isChecked = crime.isSolved
        }
    }

//    private fun getCrimeReport(crime: Crime): String {
//        val solvedString = if (crime.isSolved) {
//            getString(R.string.crime_report_solved)
//        } else {
//            getString(R.string.crime_report_unsolved)
//        }
//
//        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
//        val suspectText = if (crime.suspect.isBlank()) {
//            getString(R.string.crime_report_no_suspect)
//        } else {
//            getString(R.string.crime_report_suspect, crime.suspect)
//        }

        //returns accordingly
        //crime_report in string = 1$s! The crime was discovered on %2$s. %3$s, and %4$s
        //the parameters will fill in the placeholders
//        return getString(
//            R.string.crime_report,
//            crime.title, dateString, solvedString, suspectText
//        )
    }


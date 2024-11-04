package com.bignerdranch.android.criminalintent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

private const val TAG = "CrimeListFragment"

//NOTE: THIS IS THE MAIN FILE OR THE PARENT FILE FOR THE FRAGMENTS
//THIS IS WHERE WE HAVE THE RECYCLERVIEW IS LOCATED IN ITS XML FILE (fragment_crime_list)
//that's why we are referring to the binding
class CrimeListFragment : Fragment() {

    //variable _binding is a type of FragmentCrimeListBinding(binding class generated from XML layouotFile
    //? means the variable can hold null values, making it a nullable type
    // = means the initial value of _binding is set to null, which is common in fragment because binding is initialized in onCreateView
    // and cleared in onDestroyView, as fragments view might be destroyed and recreated multiple times
    private var _binding: FragmentCrimeListBinding? = null // nullable because Fragments view can be destroyed during lifecycle so at that time _binding is set to null
    private val binding // binding property is only accessed when it is not null
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    // just like in MainActivity to obtain the viewModel, crimeListViewModel will fetch data list in CrimeListViewModel
    private val crimeListViewModel: CrimeListViewModel by viewModels()


// Bundle represents a saved state of the fragment, to restore fragments previous state when it is recreated
    //such as after a device rotation
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //container refers to the FragmentContainerView at the activity_main_xml as it is the viewGroup for this file
        //inflate the layout using viewBinding
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)

        //binding the layout using layoutManager, using LinearLayoutManager which will position items in list vertically one after another
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

//        val crimes = crimeListViewModel.crimes
//        val adapter = CrimeListAdapter(crimes)
//        binding.crimeRecyclerView.adapter = adapter
    //NOTE:since we are using coroutines now and we have delayed it by 5000ms so there is no need of this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Consuming data from coroutine

        //viewLifecycleOwner is a reference to the lifecycle of Fragment's view (inflated xml file for fragment) than than the fragment itself
        //launching the coroutine inside the fragment, (lifecycleScope---> that specifies coroutine scope for fragment, launch tells you it's a coroutine)
      // repeatOnLifecycle --> suspending function to execute coroutine code that was in ListViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                //val crimes = crimeListViewModel.loadCrimes()
                //crimeListViewModel is a flow that emits a list fo crimes
                //lambda function receives emitted value as crimes(list of crime objects) from crimeListViewModel.crimes
                crimeListViewModel.crimes.collect { crimes ->
                    binding.crimeRecyclerView.adapter =
                        CrimeListAdapter(crimes)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
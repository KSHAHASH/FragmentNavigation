package com.bignerdranch.android.criminalintent

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeBinding
import com.bignerdranch.android.criminalintent.databinding.ListItemSeriousCrimeBinding
import java.util.UUID

//abstract class CrimeTemplate(view: View): RecyclerView.ViewHolder(view){
//    abstract fun bind(crime: Crime)
//
//}

//ViewHolder class will hold on to the view in a property named itemView
//binding now holds the list_item_crime xml views
//CrimeHolder is used to represent individual item in the RecyclerView which holds the references of each item as referenced by binding
//binding.root represents the top -level or root view in the XML file that is Linear Layout in list_item_crime.xml
//whereas val binding refers to the references to each item in that xml file
//CrimeHolder is one of a ViewHolder
class CrimeHolder(private val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root) {
    //lambda is used here to navigate to the different screen Unit is equal to void, that doesn't return anything
     fun bind(crime: Crime, onCrimeClicked: (crimeID:UUID) ->Unit) {
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()

        binding.root.setOnClickListener {
            //deleted the toast message, replacing it with the detail screen of CrimeDetailFragment
            onCrimeClicked(crime.id)
        }

        binding.crimeSolved.visibility = if(crime.isSolved){
            View.VISIBLE // we can directly use the View class here control the visibility
        }
        else{
            View.GONE
        }

    }
}

//class SeriousCrimeHolder(private val binding: ListItemSeriousCrimeBinding): CrimeTemplate(binding.root){
//    override fun bind(crime:Crime){
//        binding.crimeTitle.text = crime.title
//        binding.crimeDate.text = crime.date.toString()
//
//        binding.callTheCops.setOnClickListener {
//            Toast.makeText(
//                binding.root.context,
//                "Call the cops!",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//
//        binding.crimeSolved.visibility = if (crime.isSolved){
//            View.VISIBLE
//        }
//        else{
//            View.GONE
//        }
//    }
//}

    class CrimeListAdapter(private val crimes: List<Crime>, private val onCrimeClicked: (crimeID:UUID) ->Unit) : RecyclerView.Adapter<CrimeHolder>() {

        //creates a binding to display, wrapping the view in a view holder, inflate and bind
        //to create the viewHolder for the recyclerView along with its binding
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            //creates an inflater object by getting LayoutInflater from the context of the parent ViewGroup
            //inflate the XML layout, parent is the ViewGroup the new view will be added to
            //return type of the function is CrimeHolder
            //in this case parent refers to the recyclerView, because it is what calls the adapter's onCreateViewHolder
            //when we are calling parent.context it is calling the context of the recyclerView which is indeed set to the Activity because
            //activity is hosting the recyclerView
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
            return CrimeHolder(binding)
        }

        // responsible for populating a given viewHolder with the crime from the given position
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            // crimes[position] refers to the index for each item in the viewHolder
            //remember   itemView <--- viewHolder
            //this onBindViewHolder is responsible for generating each item in the holder
            val crime = crimes[position]
            Log.d(
                "CrimeListAdapter",
                "Crime at position $position: Title: ${crime.title}, Date: ${crime.date}"
            )
            holder.bind(crime, onCrimeClicked) // it is calling the function named "bind" from the above to bind the holder with crimes
        }

        // returns the number of items in the list fo crimes to answer the recycler view's request
        //displays the amount of data that we want to display in the recyclerView
        override fun getItemCount() = crimes.size
    }


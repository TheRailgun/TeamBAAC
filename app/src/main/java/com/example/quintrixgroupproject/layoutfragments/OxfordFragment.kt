package com.example.quintrixgroupproject.layoutfragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quintrixgroupproject.R
import com.example.quintrixgroupproject.oxfordapi.EntriesResponse
import com.example.quintrixgroupproject.viewmodels.OxfordViewModel

//maybe make OxfordFragment take in a list of view model types that has the correct data
//already in it to be displayed
//this would require changing the viewModel as well
//(Look at List of Sensors app for template)
class OxfordFragment : Fragment() {
    //create view model for oxford api class
    private lateinit var oxfordViewModel : OxfordViewModel
    private lateinit var oxfordRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oxfordViewModel = ViewModelProvider(this).get(OxfordViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_oxford, container, false)
        oxfordRecyclerView = view.findViewById(R.id.oxford_recycler_view)
        oxfordRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oxfordViewModel.entries.observe(
            this.viewLifecycleOwner,
            Observer {
                Log.d(TAG, "Have parks from view model $it")
            }
        )
    }

    //add more textViews depending on what our list element looks like for the recycler view
    //this is just a skeleton
    private class OxfordHolder(textView : TextView) : RecyclerView.ViewHolder(textView) {
        val bindText : (CharSequence) -> Unit = textView::setText
    }

    private inner class OxfordAdapter(private val entriesResponse: EntriesResponse)
        : RecyclerView.Adapter<OxfordHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OxfordHolder {
            //will have to change this depeding on what our final layout looks like and what the OxfordHolder
            //is expecting
            val view = LayoutInflater.from(parent.context).inflate(R.layout.oxford_entry_item, parent, false) as TextView
            return OxfordHolder(view)
        }

        override fun onBindViewHolder(holder: OxfordHolder, position: Int) {
            //this will also change depending on what we define getItemCount to be
            //if we are accessing nested lists not sure how the single position will work
            //might need to do some preprocessing of the data to flatten it into a single list and
            //create new classes that better encapsulate what data actually needs to be shown to the
            //user vs what data the api is returning
            val result = entriesResponse.results[position]
            holder.bindText(result.toString())
        }

        override fun getItemCount(): Int {
            //this might not be the correct value we want
            //maybe want to go through all the results and add the size of all lexical
            //entries together (or sum together all entries with each lexical entry)
            //depending on what we want to display to the user
            return entriesResponse.results.size
        }

    }



    companion object {
        fun newInstance() = OxfordFragment()
    }

}
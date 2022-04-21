package com.example.quintrixgroupproject

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.quintrixgroupproject.mwapi.*
import com.example.quintrixgroupproject.oxfordapi.EntriesResponse
import com.example.quintrixgroupproject.oxfordapi.LemmasResponse
import com.example.quintrixgroupproject.oxfordapi.OxfordFetcher
import com.google.gson.internal.LinkedTreeMap

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userEditText = findViewById<EditText>(R.id.editTextTextPersonName)
        val searchButton = findViewById<Button>(R.id.search_button)

        searchButton.setOnClickListener {
            //should have a check if editText is not empty
            val userWord = userEditText.text.toString()
            val oxfordLiveDataEntries : LiveData<EntriesResponse> = OxfordFetcher().getEntries(userWord)
            oxfordLiveDataEntries.observe(
                this,
                Observer { Log.d(TAG, "Response for entries received = $it" ) }
            )
            val oxfordLiveDataLemmas : LiveData<LemmasResponse> = OxfordFetcher().getLemmas(userWord)
            oxfordLiveDataLemmas.observe(
                this,
                Observer { Log.d(TAG, "Response for lemmas received = $it") }
            )

            val mwLiveDataDictionary : LiveData<List<DictionaryResponse2Item>> = MWFetcher().getDictionaryEntry(userWord)
            mwLiveDataDictionary.observe(
                this,
                Observer { Log.d(TAG, "Response for MW dictionary received = $it")

                }
            )
            val mwLiveDataThesaurus : LiveData<List<ThesaurusResponse2Item>> = MWFetcher().getThesaurusEntry(userWord)
            mwLiveDataThesaurus.observe(
                this,
                Observer { Log.d(TAG, "Response for MW thesaurus received = $it") }
            )
        }
        /*
        val oxfordLiveDataEntries : LiveData<EntriesResponse> = OxfordFetcher().getEntries()
        oxfordLiveDataEntries.observe(
            this,
            Observer { Log.d(TAG, "Response for entries received = $it" ) }
        )
        val oxfordLiveDataLemmas : LiveData<LemmasResponse> = OxfordFetcher().getLemmas()
        oxfordLiveDataLemmas.observe(
            this,
            Observer { Log.d(TAG, "Response for lemmas received = $it") }
        )

        val mwLiveDataDictionary : LiveData<List<DictionaryResponse2Item>> = MWFetcher().getDictionaryEntry()
        mwLiveDataDictionary.observe(
            this,
            Observer { Log.d(TAG, "Response for MW dictionary received = $it")
                Log.d(TAG, "MW Dictionary sseq = ${it[0].def[0].sseq.javaClass.kotlin}")
                //Log.d(TAG, "MW Dictionary sseq 0th = ${it[0].def[0].sseq[0][0][0].javaClass.kotlin}")
                //Log.d(TAG, "MW Dictionary sseq 1st = ${it[0].def[0].sseq[0][0][1].javaClass.kotlin}")
                //for (i in 0 until it[0].def[0].sseq[0][0].size){
                    //Log.d(TAG, "MW Dictionary sseq $i = ${it[0].def[0].sseq[0][0][i].javaClass.kotlin}")
                //}
                var x = it[0].def[0].sseq[0][0][1] as LinkedTreeMap<*, *>
                Log.d(TAG, "MW sseq linked tree map key set = ${x.keys}")
                Log.d(TAG, "MW sseq sn values = ${x["sn"]}")
                //the text under dt has tags that relate to formatting and cross referencing
                //with other entries
                //can't simply remove them b/c they sometimes contain the keyword within a sentence
                //or definition but also don't want to display them to the user
                //maybe write a function that formats this in a way that can be displayed to the user
                //one of the later things to do IMO
                Log.d(TAG, "MW sseq dt values = ${x["dt"]}")

                }
        )
        val mwLiveDataThesaurus : LiveData<List<ThesaurusResponse2Item>> = MWFetcher().getThesaurusEntry()
        mwLiveDataThesaurus.observe(
            this,
            Observer { Log.d(TAG, "Response for MW thesaurus received = $it") }
        )
         */
    }
}
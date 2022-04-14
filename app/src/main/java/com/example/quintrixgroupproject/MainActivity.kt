package com.example.quintrixgroupproject

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.quintrixgroupproject.mwapi.DictionaryResponse
import com.example.quintrixgroupproject.mwapi.MWFetcher
import com.example.quintrixgroupproject.mwapi.ThesaurusResponse
import com.example.quintrixgroupproject.oxfordapi.EntriesResponse
import com.example.quintrixgroupproject.oxfordapi.LemmasResponse
import com.example.quintrixgroupproject.oxfordapi.OxfordFetcher

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        val mwLiveDataDictionary : LiveData<List<DictionaryResponse>> = MWFetcher().getDictionaryEntry()
        mwLiveDataDictionary.observe(
            this,
            Observer { Log.d(TAG, "Response for MW dictionary received = $it") }
        )
        val mwLiveDataThesaurus : LiveData<List<ThesaurusResponse>> = MWFetcher().getThesaurusEntry()
        mwLiveDataThesaurus.observe(
            this,
            Observer { Log.d(TAG, "Response for MW thesaurus received = $it") }
        )
    }
}
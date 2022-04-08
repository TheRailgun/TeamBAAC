package com.example.quintrixgroupproject

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.quintrixgroupproject.api.EntriesResponse
import com.example.quintrixgroupproject.api.LemmasResponse
import com.example.quintrixgroupproject.api.OxfordFetcher

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
    }
}
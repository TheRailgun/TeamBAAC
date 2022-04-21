package com.example.quintrixgroupproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.quintrixgroupproject.oxfordapi.EntriesResponse
import com.example.quintrixgroupproject.oxfordapi.LemmasResponse
import com.example.quintrixgroupproject.oxfordapi.OxfordFetcher

//maybe break this into two viewmodels b/c the user will only ever see
//data from one of them at a time b/c they each correspond to different api calls
//not sure why this compiles when we add userWord
//b/c in OxfordFragment I do not initialize the viewModel with a string
class OxfordViewModel(userWord : String) : ViewModel() {
    val entries : LiveData<EntriesResponse>
    val lemmas : LiveData<LemmasResponse>
    init {
        entries = OxfordFetcher().getEntries(userWord)
        lemmas = OxfordFetcher().getLemmas(userWord)
    }
}
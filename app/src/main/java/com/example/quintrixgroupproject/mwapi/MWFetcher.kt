package com.example.quintrixgroupproject.mwapi

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quintrixgroupproject.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class MWFetcher {

    private val mwApiJSON : MWAPI
    private val mwApiString : MWAPI
    init {
        val retrofitJSON : Retrofit = Retrofit.Builder()
            .baseUrl("https://www.dictionaryapi.com/api/v3/references/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mwApiJSON = retrofitJSON.create(MWAPI::class.java)

        val retrofitString : Retrofit = Retrofit.Builder()
            .baseUrl("https://www.dictionaryapi.com/api/v3/references/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        mwApiString = retrofitString.create(MWAPI::class.java)
    }

    //replace string with appropriate POJO for parsing the json
    //should maybe refactor these so that there is only one function that takes in
    //a function as a parameter b/c there is a lot of rewritten code b/w the fetcher classes
    //as of right now
    fun getDictionaryEntry(userWord : String) : LiveData<List<DictionaryResponse2Item>> {
        val responseLiveData : MutableLiveData<List<DictionaryResponse2Item>> = MutableLiveData()
        //val userWord = "programming"
        val mwRequest : Call<List<DictionaryResponse2Item>> = mwApiJSON.getDictionaryEntry(userWord)

        mwRequest.enqueue(object : Callback<List<DictionaryResponse2Item>> {
            override fun onResponse(call: Call<List<DictionaryResponse2Item>>, response: Response<List<DictionaryResponse2Item>>) {
                Log.d(TAG, "MW dictionary entry received = ${response.body()}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<List<DictionaryResponse2Item>>, t: Throwable) {
                Log.e(TAG, "Failed to fetch MW dictionary entry", t)
            }

        })
        return responseLiveData
    }

    fun getThesaurusEntry(userWord : String) : LiveData<List<ThesaurusResponse2Item>> {
        val responseLiveData : MutableLiveData<List<ThesaurusResponse2Item>> = MutableLiveData()
        //val userWord = "programming"
        val mwRequest : Call<List<ThesaurusResponse2Item>> = mwApiJSON.getThesaurusEntry(userWord)

        mwRequest.enqueue(object : Callback<List<ThesaurusResponse2Item>> {
            override fun onResponse(call: Call<List<ThesaurusResponse2Item>>, response: Response<List<ThesaurusResponse2Item>>) {
                Log.d(TAG, "MW thesaurus entry received = ${response.body()}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<List<ThesaurusResponse2Item>>, t: Throwable) {
                Log.e(TAG, "Failed to fetch MW thesaurus entry", t)
            }

        })
        return responseLiveData
    }

}
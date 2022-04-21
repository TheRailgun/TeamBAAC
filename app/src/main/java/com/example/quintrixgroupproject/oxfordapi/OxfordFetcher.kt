package com.example.quintrixgroupproject.oxfordapi

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


class OxfordFetcher {
    //added these two just for testing, will remove the string one for final app
    private val oxfordApiJSON : OxfordAPI
    private val oxfordApiString : OxfordAPI
    init {
        val retrofitJSON : Retrofit = Retrofit.Builder()
            .baseUrl("https://od-api.oxforddictionaries.com/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        oxfordApiJSON = retrofitJSON.create(OxfordAPI::class.java)

        val retrofitString : Retrofit = Retrofit.Builder()
            .baseUrl("https://od-api.oxforddictionaries.com/api/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        oxfordApiString = retrofitString.create(OxfordAPI::class.java)

    }

    //replace string with EntriesResponse
    fun getEntries(userWord : String) : LiveData<EntriesResponse> {
        val responseLiveData : MutableLiveData<EntriesResponse> = MutableLiveData()
        //replace userWord w/ what the user puts in the EditText
        //userWord = "programs" return null for the entries b/c it is an inflection and not root word
        //programming doesn't return null (even though this seems like it is also an inflection)
        //val userWord = "programming"
        val oxfordRequest : Call<EntriesResponse> = oxfordApiJSON.getEntries(getHeaderMap(), userWord)

        oxfordRequest.enqueue(object : Callback<EntriesResponse> {
            override fun onResponse(
                call: Call<EntriesResponse>,
                response: Response<EntriesResponse>
            ) {
                //this log shows up in logcat (which is good)
                Log.d(TAG, "Entries received = ${response.body()}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<EntriesResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch entries", t)
            }

        })
        return responseLiveData
    }

    fun getLemmas(userWord : String) : LiveData<LemmasResponse> {
        val responseLiveData : MutableLiveData<LemmasResponse> = MutableLiveData()
        //val userWord = "programming"
        val oxfordRequest : Call<LemmasResponse> = oxfordApiJSON.getLemmas(getHeaderMap(), userWord)

        oxfordRequest.enqueue(object : Callback<LemmasResponse> {
            override fun onResponse(call: Call<LemmasResponse>, response: Response<LemmasResponse>) {
                Log.d(TAG, "Lemmas received = ${response.body()}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<LemmasResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch lemmas", t)
            }

        })
        return responseLiveData
    }

    //https://proandroiddev.com/headers-in-retrofit-a8d71ede2f3e
    //resource I used
    private fun getHeaderMap() : Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Accept"] = "application/json"
        headerMap["app_id"] = o_app_id
        headerMap["app_key"] = o_api_key
        return headerMap
    }
}
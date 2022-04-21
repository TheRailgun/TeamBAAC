package com.example.quintrixgroupproject

import com.example.quintrixgroupproject.api.EntriesResponse
import com.example.quintrixgroupproject.api.LemmasResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import java.io.FileInputStream
import java.util.*

//For some reason I can't move this file into the api package without breaking the BuildConfig
//Look into this later

const val api_key : String = BuildConfig.O_API_KEY
const val app_id : String = BuildConfig.O_APP_ID

interface OxfordAPI {

    //replace program with word user types in, just did this to test
    //replace string with EntriesResponse
    //API Documentation said that the Lemmas call should be used before this one to make sure
    //that the word is a root word (or headword) in the dictionary and not an inflection of it
    @GET("entries/en-us/{word}?strictMatch=false")
    fun getEntries(@HeaderMap headers : Map<String, String>, @Path("word") userWord : String) : Call<EntriesResponse>

    @GET("lemmas/en/{word}")
    fun getLemmas(@HeaderMap headers: Map<String, String>, @Path("word") userWord: String) : Call<LemmasResponse>

}
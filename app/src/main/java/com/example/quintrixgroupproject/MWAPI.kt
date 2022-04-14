package com.example.quintrixgroupproject

import com.example.quintrixgroupproject.mwapi.DictionaryResponse
import com.example.quintrixgroupproject.mwapi.ThesaurusResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

const val mw_d_api_key = BuildConfig.MW_D_API_KEY
const val mw_t_api_key = BuildConfig.MW_T_API_KEY

interface MWAPI {

    @GET("collegiate/json/{word}?key=$mw_d_api_key")
    fun getDictionaryEntry(@Path("word") userWord : String) : Call<List<DictionaryResponse>>

    @GET("thesaurus/json/{word}?key=$mw_t_api_key")
    fun getThesaurusEntry(@Path("word") userWord: String) : Call<List<ThesaurusResponse>>

}
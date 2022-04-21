package com.example.quintrixgroupproject.translation

import android.os.Build
import com.example.quintrixgroupproject.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

//url https://translate.yandex.net/api/v1.5/tr.json/translate
const val yandex_key = BuildConfig.YANDEX_KEY
interface TranslateAPI {
    @GET("tr.json/translate?")
    fun translateToFrom(
        @Query("text") text: String,
        @Query("key") key : String,
        @Query("lang") lang : String//,
        //@Query("source_lang") sourceLanguage: String,
       // @Query("target_lang") targetLanguage: String
    ) : Call<TranslateResponse>
}
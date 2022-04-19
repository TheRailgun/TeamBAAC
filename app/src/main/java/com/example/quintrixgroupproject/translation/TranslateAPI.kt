package com.example.quintrixgroupproject.translation

import com.example.quintrixgroupproject.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

//url https://translate.yandex.net/api/v1.5/tr.json/translate
interface TranslateAPI {
    @GET("/translate?key=${BuildConfig.YANDEX_KEY}&text={text}&lang={source_lang}-{target_lang}")
    fun translateToFrom(
        @Path("text") text: String,
        @Path("source_lang") sourceLanguage: String,
        @Path("target_lang") targetLanguage: String
    ) : Call<TranslateResponse>
}
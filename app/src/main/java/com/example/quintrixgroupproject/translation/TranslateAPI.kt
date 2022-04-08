package com.example.quintrixgroupproject.translation

import com.example.quintrixgroupproject.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

//url https://translate.yandex.net/api/v1.5/tr.json/translate
//key trnsl.1.1.20220408T232953Z.0568da5ef27a5fd2.c4d2e672f759a28bb7570f3514094d74dcb0b88e
interface TranslateAPI {
    @GET("/translate?key=${BuildConfig.YANDEX_KEY}&text={text}&lang={source_lang}-{target_lang}")
    fun translateToFrom(
        @Path("text") text: String,
        @Path("source_lang") sourceLanguage: String,
        @Path("target_lang") targetLanguage: String
    ) : Call<TranslateResponse>
}
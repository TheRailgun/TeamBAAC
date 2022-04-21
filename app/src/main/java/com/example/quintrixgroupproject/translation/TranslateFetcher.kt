package com.example.quintrixgroupproject.translation
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quintrixgroupproject.BuildConfig
import com.example.quintrixgroupproject.OxfordAPI
import com.example.quintrixgroupproject.api_key
import com.example.quintrixgroupproject.app_id
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG ="TranslateFetcher"

class TranslateFetcher {
    private val translateApi : TranslateAPI
    init{
        val retrofit: Retrofit= Retrofit.Builder()
            .baseUrl("https://translate.yandex.net/api/v1.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        translateApi = retrofit.create(TranslateAPI::class.java)
    }


    fun translateText(text: String, translateToFrom: String) : LiveData<TranslateResponse>{
        val responseLiveData : MutableLiveData<TranslateResponse> = MutableLiveData()


        val translateRequest : Call<TranslateResponse> = translateApi.translateToFrom(text, BuildConfig.YANDEX_KEY, translateToFrom)//, sourceLang, targetLang)

        translateRequest.enqueue(object: Callback<TranslateResponse>{
            override fun onResponse(
                call: Call<TranslateResponse>,
                response: Response<TranslateResponse>
            ) {
                Log.d(TAG, "translate request recieved. data = ${response.body()} beginning text = ${text}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<TranslateResponse>, t: Throwable) {
                Log.e(TAG, "translate function error occurred.", t)
            }
        })
            return responseLiveData
    }


}
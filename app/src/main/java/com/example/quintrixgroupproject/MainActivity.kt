package com.example.quintrixgroupproject

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.quintrixgroupproject.api.EntriesResponse
import com.example.quintrixgroupproject.api.LemmasResponse
import com.example.quintrixgroupproject.api.OxfordFetcher
import com.example.quintrixgroupproject.translation.TranslateFetcher
import com.example.quintrixgroupproject.translation.TranslateResponse

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val translateLiveDataEntries : LiveData<TranslateResponse> = TranslateFetcher()
            .translateText("Sample Text", "en-es")
        translateLiveDataEntries.observe(
            this,
            Observer {
                Log.d(TAG, "Response for translate = $it")
            }
        )
    }

    fun viewTranslation(view: View) {
        val intent = Intent(this@MainActivity, TranslationActivity::class.java)
        val txt : TextView = findViewById(R.id.editTextTranslation)
        val query = txt.text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
    }
}
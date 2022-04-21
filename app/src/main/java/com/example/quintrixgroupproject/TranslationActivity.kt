package com.example.quintrixgroupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.quintrixgroupproject.translation.TranslateFetcher
import com.example.quintrixgroupproject.translation.TranslateResponse

class TranslationActivity : AppCompatActivity() {
    var textView: TextView? = null
    var editTextView : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translation)
        textView = findViewById<View>(R.id.textView) as TextView
        editTextView = findViewById(R.id.editTextTranslation) as EditText
        editTextView!!.setText("${intent.getStringExtra("query")}")
    }

    fun translate(view: View?){
        val query = editTextView!!.text.toString()
        var str = StringBuilder()
        val translateLiveDataEntries : LiveData<TranslateResponse>? = query?.let {
            TranslateFetcher()
                .translateText(it, "en-es")
        }
        translateLiveDataEntries?.observe(this) {
            for (i in it.text)
                str.append(i)
            textView!!.setText("${str.toString()}")
        }

        //  textView!!.setText()
    /*val translateLiveDataEntries : LiveData<TranslateResponse> = TranslateFetcher()
            .translateText("Sample Text", "en-es")
        translateLiveDataEntries.observe(
            this,
            Observer {
                Log.d(TAG, "Response for translate = $it")
            }
        )*/
    }
}
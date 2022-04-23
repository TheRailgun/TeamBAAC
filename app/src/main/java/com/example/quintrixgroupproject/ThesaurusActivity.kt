package com.example.quintrixgroupproject

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.quintrixgroupproject.mwapi.MWFetcher
import com.example.quintrixgroupproject.mwapi.ThesaurusResponse2Item

class ThesaurusActivity: AppCompatActivity() {
    var textView: TextView? = null
    var textViewAnt: TextView? = null
    var editTextView: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thesaurus)
        textView = findViewById<View>(R.id.textView) as TextView
        textViewAnt = findViewById<View>(R.id.textView2) as TextView
        editTextView = findViewById(R.id.editTextThesaurus) as EditText
        val btn_click_me = findViewById(R.id.button) as Button
        btn_click_me.setOnClickListener {
            getSyns()
        }

        editTextView!!.setText("${intent.getStringExtra("query")}")
        if(editTextView!=null) {
            var userWord = editTextView!!.text.toString()
            println(userWord)
            val mwLiveDataThesaurus: LiveData<List<ThesaurusResponse2Item>> =
                MWFetcher().getThesaurusEntry(userWord)
            mwLiveDataThesaurus.observe(this)
            {
                Log.d(ContentValues.TAG, "Response for MW thesaurus received = $it")
                Log.d(ContentValues.TAG, "Response for MW thesaurus received = ${it.get(0).meta.syns.joinToString()}")
                textView!!.text = it.get(0).meta.syns.get(0).joinToString("\n")
                if(!it.get(0).meta.ants.isEmpty()) {
                    textViewAnt!!.text = it.get(0).meta.ants.get(0).joinToString("\n")
                } else{
                    textViewAnt!!.text = ""
                }
            }

        }
        else
        {
            Log.d(ContentValues.TAG,"editTextView is null")
        }
    }
    fun getSyns(){
        textView = findViewById<View>(R.id.textView) as TextView
        textViewAnt = findViewById<View>(R.id.textView2) as TextView
        if(editTextView!=null) {
            var userWord = editTextView!!.text.toString()
            println(userWord)
            val mwLiveDataThesaurus: LiveData<List<ThesaurusResponse2Item>> =
                MWFetcher().getThesaurusEntry(userWord)
            mwLiveDataThesaurus.observe(this)
            {
                Log.d(ContentValues.TAG, "Response for MW thesaurus received = ${it.get(0).meta.ants.joinToString()}")
                Log.d(ContentValues.TAG, "Response for MW thesaurus received = ${it.get(0).meta.syns.joinToString()}")
                textView!!.text = it.get(0).meta.syns.get(0).joinToString("\n")
                if(!it.get(0).meta.ants.isEmpty()) {
                    textViewAnt!!.text = it.get(0).meta.ants.get(0).joinToString("\n")
                } else{
                    textViewAnt!!.text = ""
                }
            }

        }
        else
        {
            Log.d(ContentValues.TAG,"editTextView is null")
        }
    }
}
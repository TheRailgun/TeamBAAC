package com.example.quintrixgroupproject

import android.content.ContentValues
import android.content.Intent
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

class ThesaurusActivity : AppCompatActivity() {
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
        if (editTextView != null) {
            var userWord = editTextView!!.text.toString()
            println(userWord)
            try {
                val mwLiveDataThesaurus: LiveData<List<ThesaurusResponse2Item>> =
                    MWFetcher().getThesaurusEntry(userWord)

                mwLiveDataThesaurus.observe(this)
                {
                    Log.d(ContentValues.TAG, "Response for MW thesaurus received = $it")
                    if (it.isEmpty() || it == null) {
                        textView!!.text = "Not a recognized word"
                    } else {
                        Log.d(
                            ContentValues.TAG,
                            "Response for MW thesaurus received = ${it.get(0).meta.syns.joinToString()}"
                        )
                        textView!!.text = it.get(0).meta.syns.get(0).joinToString(", ")
                        if (!it.get(0).meta.ants.isEmpty()) {
                            textViewAnt!!.text = it.get(0).meta.ants.get(0).joinToString(", ")
                        } else {
                            textViewAnt!!.text = ""
                        }
                    }

                }
            } catch (e: IllegalStateException) {
                textView!!.text = "Not a recognized word"
                Log.i("Thesaurus", "Caught error")
            }
        } else {
            Log.d(ContentValues.TAG, "editTextView is null")
        }

    }

    fun getSyns() {
        textView = findViewById<View>(R.id.textView) as TextView
        textViewAnt = findViewById<View>(R.id.textView2) as TextView
        if (editTextView != null) {
            var userWord = editTextView!!.text.toString()
            println(userWord)
            val mwLiveDataThesaurus: LiveData<List<ThesaurusResponse2Item>> =
                MWFetcher().getThesaurusEntry(userWord)
            mwLiveDataThesaurus.observe(this)
            {
                if (it.isEmpty()) {
                    textView!!.text = "Not a recognized word"
                } else {
                    Log.d(
                        ContentValues.TAG,
                        "Response for MW thesaurus received = ${it.get(0).meta.ants.joinToString()}"
                    )
                    Log.d(
                        ContentValues.TAG,
                        "Response for MW thesaurus received = ${it.get(0).meta.syns.joinToString()}"
                    )
                    textView!!.text = it.get(0).meta.syns.get(0).joinToString(", ")
                    if (!it.get(0).meta.ants.isEmpty()) {
                        textViewAnt!!.text = it.get(0).meta.ants.get(0).joinToString(", ")
                    } else {
                        textViewAnt!!.text = ""
                    }
                }
            }

        } else {
            Log.d(ContentValues.TAG, "editTextView is null")
        }
    }
    fun viewHome(view: View) {
        val intent = Intent(this@ThesaurusActivity, MainActivity::class.java)
        val query = findViewById<EditText>(R.id.editTextThesaurus).text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
        finish()
    }

    fun viewDef(view: View) {
        val intent = Intent(this@ThesaurusActivity, OxfordActivity::class.java)
        val query = findViewById<EditText>(R.id.editTextThesaurus).text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
        finish()
    }
    fun viewTranslation(view: View) {
        val intent = Intent(this@ThesaurusActivity, TranslationActivity::class.java)
        val query = findViewById<EditText>(R.id.editTextThesaurus).text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
        finish()
    }
}
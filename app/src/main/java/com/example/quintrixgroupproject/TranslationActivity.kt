package com.example.quintrixgroupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.quintrixgroupproject.translation.TranslateFetcher
import com.example.quintrixgroupproject.translation.TranslateResponse

class TranslationActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var textView: TextView? = null
    var editTextView : EditText? = null
    var selectedLanguage : String = "es"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translation)
        textView = findViewById<View>(R.id.textView) as TextView
        editTextView = findViewById(R.id.editTextTranslation) as EditText
        editTextView!!.setText("${intent.getStringExtra("query")}")

        val spinner: Spinner = findViewById(R.id.source_lang)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.languages_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
        translate(textView)
    }

    fun translate(view: View?){
        val query = editTextView!!.text.toString()
        var str = StringBuilder()
        if(query != "") {
            val translateLiveDataEntries: LiveData<TranslateResponse>? = query.let {
                TranslateFetcher()
                    .translateText(it, "en-${selectedLanguage}")
            }
            translateLiveDataEntries?.observe(this) {
                for (i in it.text)
                    str.append(i)
                textView!!.setText("${str.toString()}")
            }
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(pos).toString()
        selectedLanguage = languageSelection(text)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun languageSelection(lang : String):String {
        var result = when(lang) {
            "French" -> "fr"
            "German" -> "de"
            "Spanish" -> "es"
            "Chinese" -> "zh"
            "Polish" -> "pl"
            "Hindi" -> "hi"
            "Greek" -> "el"
            "Korean" -> "ko"
            "Japanese" -> "ja"
            "Arabic" -> "ar"
            else-> ""
        }
        return result
    }

    fun viewHome(view: View) {
        val intent = Intent(this@TranslationActivity, MainActivity::class.java)
        val query = findViewById<EditText>(R.id.editTextTranslation).text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
        finish()
    }

    fun viewDef(view: View) {
        val intent = Intent(this@TranslationActivity, OxfordActivity::class.java)
        val query = findViewById<EditText>(R.id.editTextTranslation).text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
        finish()
    }
    fun viewThesaurus(view: View) {
        val intent = Intent(this@TranslationActivity, ThesaurusActivity::class.java)
        val txt : TextView = findViewById(R.id.editTextTranslation)
        val query = txt.text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
        finish()
    }
}
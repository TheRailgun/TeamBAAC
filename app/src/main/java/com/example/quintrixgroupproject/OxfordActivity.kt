package com.example.quintrixgroupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.quintrixgroupproject.oxfordapi.EntriesResponse
import com.example.quintrixgroupproject.oxfordapi.OxfordFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OxfordActivity : AppCompatActivity() {

    var textViewDef: TextView? = null
    //var textViewDefPoS : TextView? = null
    //didn't include part of speech because for the dozen or so different words I tried what came back
    //was always null so it just left a big gap in the layout which didn't look good
    //could have replace with an error message in the textview but getting an error for most words looks bad
    var textViewDefPro : TextView? = null
    var textViewDefDef : TextView? = null
    var editTextDef: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oxford)
        textViewDef = findViewById(R.id.textViewDef)
        //textViewDefPoS = findViewById(R.id.textViewDefPos)
        textViewDefPro = findViewById(R.id.textViewDefPro)
        textViewDefDef = findViewById(R.id.textViewDefDef)
        editTextDef = findViewById(R.id.editTextDef)
        //call the api for the first time here in oncreate
        //maybe don't use global scoper
        //editTextDef!!.setText(intent.getStringExtra("query").toString())
        //there is probably a better solution than having a default but works for the moment
        var potInfQuery = intent.getStringExtra("query") ?: "default"
        Log.d("onCreate OxfordActivity", "${intent.getStringExtra("query")}")
        //GlobalScope.launch(Dispatchers.IO) {
        val OF = OxfordFetcher()
        val oxfordLemmas = OF.getLemmas(potInfQuery)
        var oxfordEntries: LiveData<EntriesResponse>? = OF.getEntries(potInfQuery)
        var entryFound: Boolean? = null
        Log.d("onCreate OxfordActivity", "lemmas = ${oxfordLemmas?.value}")
        Log.d("onCreate OxfordActivity", "entries = ${oxfordEntries?.value}")
        //Log.d("onCreate OxfordActivity", "entryFound = $entryFound")
        //launch(Dispatchers.Main) {
        oxfordEntries?.observe(
            this@OxfordActivity,
            Observer {
                Log.d("onCreate OxfordActivity", "entry in observe = $it")
                entryFound = it != null
                Log.d("onCreate OxfordActivity", "entryFound in observe = $entryFound")
                editTextDef?.setText(potInfQuery)
                if (entryFound == null || entryFound == false) {
                    textViewDef?.text = "Word not found. Please try a different spelling or inflection."
                    textViewDefPro?.text = ""
                    textViewDefDef?.text = ""
                    Log.d("onCreate OxfordActivity", "textview in observe = ${textViewDef?.text}")

                    oxfordLemmas.observe(
                        this@OxfordActivity,
                        Observer {
                            Log.d("onCreate OxfordActivity", "lemma in observe = $it")
                            if (entryFound == null || entryFound == false) {
                                val numResults = oxfordLemmas.value?.results?.size ?: 0
                                outerLoop@ for (i in 0 until numResults) {
                                    var curNumLexEntry =
                                        oxfordLemmas.value?.results?.get(i)?.lexicalEntries?.size ?: 0
                                    for (j in 0 until curNumLexEntry) {
                                        var curNumInflections =
                                            oxfordLemmas.value?.results?.get(i)?.lexicalEntries?.get(j)?.inflectionOf?.size ?: 0
                                        for (k in 0 until curNumInflections) {
                                            potInfQuery =
                                                oxfordLemmas.value?.results?.get(i)?.lexicalEntries?.get(j)?.inflectionOf?.get(k)?.text ?: ""
                                            Log.d("onCreate OxfordActivity", "query = $potInfQuery")
                                            if (potInfQuery != "") {
                                                oxfordEntries = OF.getEntries(potInfQuery)
                                                oxfordEntries?.observe(
                                                    this@OxfordActivity,
                                                    Observer { e ->
                                                        entryFound = e != null
                                                        Log.d("onCreate OxfordActivity", "entryFound in inner observe = $entryFound")
                                                        //removing line below changes whether editText shows what the user typed (when commented out)
                                                        //or the headword the dictionary api expects
                                                        editTextDef?.setText(potInfQuery)
                                                        if (entryFound == null || entryFound == false) {
                                                            textViewDef?.text =
                                                                "Word not found. Please try a different spelling or inflection."
                                                            textViewDefPro?.text = ""
                                                            textViewDefDef?.text = ""
                                                        } else {
                                                            textViewDef?.text = ""
                                                            //textViewDefPoS?.text = ""
                                                            textViewDefPro?.text = ""
                                                            textViewDefDef?.text = ""
                                                            //textViewDef?.append(oxfordEntries?.value.toString())
                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.word ?: potInfQuery)
                                                            //textViewDef?.append(System.getProperty("line.separator"))
                                                            //textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.language ?: "en-us")
                                                            //textViewDef?.append(System.getProperty("line.separator"))
                                                            for(i in 0 until (oxfordEntries?.value?.results?.size ?: 0)){

                                                                if(i == 0){
                                                                    for(l in 0 until (oxfordEntries?.value?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.size ?: 0)){
                                                                        textViewDefPro?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.get(l)?.phoneticSpelling)
                                                                        textViewDefPro?.append(System.getProperty("line.separator"))
                                                                    }
                                                                }

                                                                for(j in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.size ?: 0)){
                                                                    for(k in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.size ?: 0)){
                                                                        for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.size ?: 0)){
                                                                            textViewDefDef?.append("-> " + oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.get(l)?.shortDefinitions?.get(0).toString())
                                                                            textViewDefDef?.append(System.getProperty("line.separator"))
                                                                            textViewDefDef?.append(System.getProperty("line.separator"))
                                                                        }
                                                                        /*
                                                                        for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.size ?: 0)){
                                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.get(l)?.phoneticSpelling)
                                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                                        }

                                                                         */

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                )
                                                /*

                                                Log.d("onCreate OxfordActivity", "Oxford entries = $oxfordEntries")
                                                entryFound = textViewDef?.text != "Word not found. Please try a different spelling or inflection."
                                                if(entryFound as Boolean){
                                                    break@outerLoop
                                                }

                                                 */


                                            }
                                        }
                                    }
                                }
                            }
                            //Toast.makeText(this@OxfordActivity, "Word not found. Please try a different spelling or inflection", Toast.LENGTH_LONG).show()
                        }
                    )
                } else {
                    editTextDef?.setText(potInfQuery)
                    textViewDef?.text = ""
                    //textViewDefPoS?.text = ""
                    textViewDefPro?.text = ""
                    textViewDefDef?.text = ""
                    //textViewDef?.append(oxfordEntries?.value.toString())
                    textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.word ?: potInfQuery)
                    //textViewDef?.append(System.getProperty("line.separator"))
                    //textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.language ?: "en-us")
                    //textViewDef?.append(System.getProperty("line.separator"))
                    for(i in 0 until (oxfordEntries?.value?.results?.size ?: 0)){

                        if(i == 0){
                            for(l in 0 until (oxfordEntries?.value?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.size ?: 0)){
                                textViewDefPro?.append(oxfordEntries?.value?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.get(l)?.phoneticSpelling)
                                textViewDefPro?.append(System.getProperty("line.separator"))
                            }
                        }

                        for(j in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.size ?: 0)){
                            for(k in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.size ?: 0)){
                                for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.size ?: 0)){
                                    textViewDefDef?.append("-> " + oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.get(l)?.shortDefinitions?.get(0).toString())
                                    textViewDefDef?.append(System.getProperty("line.separator"))
                                    textViewDefDef?.append(System.getProperty("line.separator"))
                                }
                                /*
                                for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.size ?: 0)){
                                    textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.get(l)?.phoneticSpelling)
                                    textViewDef?.append(System.getProperty("line.separator"))
                                }

                                 */

                            }
                        }
                    }
                }
                //Log.d("onCreate OxfordActivity", "textview in observe after if = ${textViewDef?.text}")
            }
        )


    }


    fun getDefinition(view: View?) {
        textViewDef = findViewById(R.id.textViewDef)
        textViewDefPro = findViewById(R.id.textViewDefPro)
        textViewDefDef = findViewById(R.id.textViewDefDef)
        editTextDef = findViewById(R.id.editTextDef)
        //call the api for the first time here in oncreate
        //maybe don't use global scoper
        //editTextDef!!.setText(intent.getStringExtra("query").toString())
        //there is probably a better solution than having a default but works for the moment
        //var potInfQuery = intent.getStringExtra("query") ?: "default"
        //Log.d("onCreate OxfordActivity", "${intent.getStringExtra("query")}")
        //GlobalScope.launch(Dispatchers.IO) {
        var potInfQuery = editTextDef?.text.toString()
        val OF = OxfordFetcher()
        val oxfordLemmas = OF.getLemmas(potInfQuery)
        var oxfordEntries: LiveData<EntriesResponse>? = OF.getEntries(potInfQuery)
        var entryFound: Boolean? = null
        Log.d("onCreate OxfordActivity", "lemmas = ${oxfordLemmas?.value}")
        Log.d("onCreate OxfordActivity", "entries = ${oxfordEntries?.value}")
        //Log.d("onCreate OxfordActivity", "entryFound = $entryFound")
        //launch(Dispatchers.Main) {
        oxfordEntries?.observe(
            this@OxfordActivity,
            Observer {
                Log.d("onCreate OxfordActivity", "entry in observe = $it")
                entryFound = it != null
                Log.d("onCreate OxfordActivity", "entryFound in observe = $entryFound")
                editTextDef?.setText(potInfQuery)
                if (entryFound == null || entryFound == false) {
                    textViewDef?.text = "Word not found. Please try a different spelling or inflection."
                    textViewDefPro?.text = ""
                    textViewDefDef?.text = ""
                    Log.d("onCreate OxfordActivity", "textview in observe = ${textViewDef?.text}")

                    oxfordLemmas.observe(
                        this@OxfordActivity,
                        Observer {
                            Log.d("onCreate OxfordActivity", "lemma in observe = $it")
                            if (entryFound == null || entryFound == false) {
                                val numResults = oxfordLemmas.value?.results?.size ?: 0
                                outerLoop@ for (i in 0 until numResults) {
                                    var curNumLexEntry =
                                        oxfordLemmas.value?.results?.get(i)?.lexicalEntries?.size ?: 0
                                    for (j in 0 until curNumLexEntry) {
                                        var curNumInflections =
                                            oxfordLemmas.value?.results?.get(i)?.lexicalEntries?.get(j)?.inflectionOf?.size ?: 0
                                        for (k in 0 until curNumInflections) {
                                            potInfQuery =
                                                oxfordLemmas.value?.results?.get(i)?.lexicalEntries?.get(j)?.inflectionOf?.get(k)?.text ?: ""
                                            Log.d("onCreate OxfordActivity", "query = $potInfQuery")
                                            if (potInfQuery != "") {
                                                oxfordEntries = OF.getEntries(potInfQuery)
                                                oxfordEntries?.observe(
                                                    this@OxfordActivity,
                                                    Observer { e ->
                                                        entryFound = e != null
                                                        Log.d("onCreate OxfordActivity", "entryFound in inner observe = $entryFound")
                                                        //removing line below changes whether editText shows what the user typed (when commented out)
                                                        //or the headword the dictionary api expects
                                                        editTextDef?.setText(potInfQuery)
                                                        if (entryFound == null || entryFound == false) {
                                                            textViewDef?.text =
                                                                "Word not found. Please try a different spelling or inflection."
                                                            textViewDefPro?.text = ""
                                                            textViewDefDef?.text = ""
                                                        } else {
                                                            textViewDef?.text = ""
                                                            //textViewDefPoS?.text = ""
                                                            textViewDefPro?.text = ""
                                                            textViewDefDef?.text = ""
                                                            //textViewDef?.append(oxfordEntries?.value.toString())
                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.word ?: potInfQuery)
                                                            //textViewDef?.append(System.getProperty("line.separator"))
                                                            //textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.language ?: "en-us")
                                                            //textViewDef?.append(System.getProperty("line.separator"))
                                                            for(i in 0 until (oxfordEntries?.value?.results?.size ?: 0)){
                                                                if(i == 0){
                                                                    for(l in 0 until (oxfordEntries?.value?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.size ?: 0)){
                                                                        textViewDefPro?.append(oxfordEntries?.value?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.get(l)?.phoneticSpelling)
                                                                        textViewDefPro?.append(System.getProperty("line.separator"))
                                                                    }
                                                                }
                                                                for(j in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.size ?: 0)){
                                                                    for(k in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.size ?: 0)){
                                                                        for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.size ?: 0)){
                                                                            textViewDefDef?.append("-> " + oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.get(l)?.shortDefinitions?.get(0).toString())
                                                                            textViewDefDef?.append(System.getProperty("line.separator"))
                                                                            textViewDefDef?.append(System.getProperty("line.separator"))
                                                                        }
                                                                        /*
                                                                        for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.size ?: 0)){
                                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.get(l)?.phoneticSpelling)
                                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                                        }

                                                                         */

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                )
                                                /*
                                                not sure why this breaks code in this area but not in onCreate
                                                wasn't needed in onCreate either, was just helpful for debugging
                                                Log.d("onCreate OxfordActivity", "Oxford entries = $oxfordEntries")
                                                entryFound = textViewDef?.text != "Word not found. Please try a different spelling or inflection."
                                                if(entryFound as Boolean){
                                                    break@outerLoop
                                                }

                                                 */

                                            }
                                        }
                                    }
                                }
                                //Toast.makeText(this@OxfordActivity, "Word not found. Please try a different spelling or inflection", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                } else {
                    editTextDef?.setText(potInfQuery)
                    textViewDef?.text = ""
                    //textViewDefPoS?.text = ""
                    textViewDefPro?.text = ""
                    textViewDefDef?.text = ""
                    //textViewDef?.append(oxfordEntries?.value.toString())
                    textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.word ?: potInfQuery)
                    //textViewDef?.append(System.getProperty("line.separator"))
                    //textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.language ?: "en-us")
                    //textViewDef?.append(System.getProperty("line.separator"))
                    for(i in 0 until (oxfordEntries?.value?.results?.size ?: 0)){
                        if(i == 0){
                            for(l in 0 until (oxfordEntries?.value?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.size ?: 0)){
                                textViewDefPro?.append(oxfordEntries?.value?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.get(l)?.phoneticSpelling)
                                textViewDefPro?.append(System.getProperty("line.separator"))
                            }
                        }
                        for(j in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.size ?: 0)){
                            for(k in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.size ?: 0)){
                                for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.size ?: 0)){
                                    textViewDefDef?.append("-> " + oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.get(l)?.shortDefinitions?.get(0).toString())
                                    textViewDefDef?.append(System.getProperty("line.separator"))
                                    textViewDefDef?.append(System.getProperty("line.separator"))
                                }
                                /*
                                for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.size ?: 0)){
                                    textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.get(l)?.phoneticSpelling)
                                    textViewDef?.append(System.getProperty("line.separator"))
                                }

                                 */

                            }
                        }
                    }

                }
                //Log.d("onCreate OxfordActivity", "textview in observe after if = ${textViewDef?.text}")
            }
        )
    }
    fun viewHome(view: View) {
        val intent = Intent(this@OxfordActivity, MainActivity::class.java)
        val query = findViewById<EditText>(R.id.editTextDef).text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
        finish()
    }

    fun viewThesaurus(view: View) {
        val intent = Intent(this@OxfordActivity, ThesaurusActivity::class.java)
        val query = findViewById<EditText>(R.id.editTextDef).text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
        finish()
    }
    fun viewTranslation(view: View) {
        val intent = Intent(this@OxfordActivity, TranslationActivity::class.java)
        val query = findViewById<EditText>(R.id.editTextDef).text.toString()
        intent.putExtra("query", query)
        startActivity(intent)
        finish()
    }
}
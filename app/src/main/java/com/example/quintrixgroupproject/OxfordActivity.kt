package com.example.quintrixgroupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.quintrixgroupproject.oxfordapi.EntriesResponse
import com.example.quintrixgroupproject.oxfordapi.OxfordFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OxfordActivity : AppCompatActivity() {

    var textViewDef: TextView? = null
    var editTextDef: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oxford)
        textViewDef = findViewById(R.id.textViewDef)
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
                                                        } else {
                                                            textViewDef?.text = ""
                                                            //textViewDef?.append(oxfordEntries?.value.toString())
                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.word ?: potInfQuery)
                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.language)
                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                            for(i in 0 until (oxfordEntries?.value?.results?.size ?: 0)){
                                                                for(j in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.size ?: 0)){
                                                                    for(k in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.size ?: 0)){
                                                                        for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.size ?: 0)){
                                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.get(l)?.shortDefinitions.toString())
                                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                                        }
                                                                        for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.size ?: 0)){
                                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.get(l)?.phoneticSpelling)
                                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                                        }

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
                        }
                    )
                } else {
                    editTextDef?.setText(potInfQuery)
                    textViewDef?.text = ""
                    //textViewDef?.append(oxfordEntries?.value.toString())
                    textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.word ?: potInfQuery)
                    textViewDef?.append(System.getProperty("line.separator"))
                    textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.language)
                    textViewDef?.append(System.getProperty("line.separator"))
                    for(i in 0 until (oxfordEntries?.value?.results?.size ?: 0)){
                        for(j in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.size ?: 0)){
                            for(k in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.size ?: 0)){
                                for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.size ?: 0)){
                                    textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.get(l)?.shortDefinitions.toString())
                                    textViewDef?.append(System.getProperty("line.separator"))
                                }
                                for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.size ?: 0)){
                                    textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.get(l)?.phoneticSpelling)
                                    textViewDef?.append(System.getProperty("line.separator"))
                                }

                            }
                        }
                    }
                }
                //Log.d("onCreate OxfordActivity", "textview in observe after if = ${textViewDef?.text}")
            }
        )
        /*
        //why does textView suddenly not have the text that was assigned to it in the observe call???
        entryFound = textViewDef?.text != "Word not found. Please try a different spelling or inflection."
        Log.d("onCreate OxfordActivity", "textView = ${textViewDef?.text}")
        Log.d("onCreate OxfordActivity", "entryFound before lemmas = $entryFound")
        entryFound = false
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
                                            editTextDef?.setText(potInfQuery)
                                            if (entryFound == null || entryFound == false) {
                                                textViewDef?.text =
                                                    "Word not found. Please try a different spelling or inflection."
                                            } else {
                                                textViewDef?.text = ""
                                                textViewDef?.append(oxfordEntries?.value.toString())
                                            }
                                        }
                                    )
                                    Log.d("onCreate OxfordActivity", "Oxford entries = $oxfordEntries")
                                    entryFound = textViewDef?.text != "Word not found. Please try a different spelling or inflection."
                                    if(entryFound as Boolean){
                                        break@outerLoop
                                    }

                                }
                            }
                        }
                    }
                }
            }
        )

         */

        //}.join()
        //entryFound = oxfordEntries?.value != null
        //Log.d("onCreate OxfordActivity", "lemmas after observe = ${oxfordLemmas.value}")
        //Log.d("onCreate OxfordActivity", "entries after observe = ${oxfordEntries?.value}")
        //Log.d("onCreate OxfordActivity", "entryFound after observe = $entryFound")


        /*
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
                        if (potInfQuery != "") {
                            oxfordEntries = OF.getEntries(potInfQuery)

                            if (oxfordEntries != null) {
                                Log.d(
                                    "onCreate OxfordActivity",
                                    "Oxford entries = $oxfordEntries"
                                )
                                entryFound = true
                                break@outerLoop
                            }
                        }
                    }
                }
            }
        }

         */


        //launch(Dispatchers.Main) {
        /*
                editTextDef?.setText(potInfQuery)
                if(entryFound == null || entryFound == false){
                    textViewDef?.text = "Word not found. Please try a different spelling or inflection."
                }
                else{
                    textViewDef?.text = ""
                    textViewDef?.append(oxfordEntries?.value.toString())
                }

         */

        //}
        //}


    }


    fun getDefinition(view: View?) {
        textViewDef = findViewById(R.id.textViewDef)
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
                                                        } else {
                                                            textViewDef?.text = ""
                                                            //textViewDef?.append(oxfordEntries?.value.toString())
                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.word ?: potInfQuery)
                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.language)
                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                            for(i in 0 until (oxfordEntries?.value?.results?.size ?: 0)){
                                                                for(j in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.size ?: 0)){
                                                                    for(k in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.size ?: 0)){
                                                                        for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.size ?: 0)){
                                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.get(l)?.shortDefinitions.toString())
                                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                                        }
                                                                        for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.size ?: 0)){
                                                                            textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.get(l)?.phoneticSpelling)
                                                                            textViewDef?.append(System.getProperty("line.separator"))
                                                                        }

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
                            }
                        }
                    )
                } else {
                    editTextDef?.setText(potInfQuery)
                    textViewDef?.text = ""
                    //textViewDef?.append(oxfordEntries?.value.toString())
                    textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.word ?: potInfQuery)
                    textViewDef?.append(System.getProperty("line.separator"))
                    textViewDef?.append(oxfordEntries?.value?.results?.get(0)?.language)
                    textViewDef?.append(System.getProperty("line.separator"))
                    for(i in 0 until (oxfordEntries?.value?.results?.size ?: 0)){
                        for(j in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.size ?: 0)){
                            for(k in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.size ?: 0)){
                                for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.size ?: 0)){
                                    textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.senses?.get(l)?.shortDefinitions.toString())
                                    textViewDef?.append(System.getProperty("line.separator"))
                                }
                                for(l in 0 until (oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.size ?: 0)){
                                    textViewDef?.append(oxfordEntries?.value?.results?.get(i)?.lexicalEntries?.get(j)?.entries?.get(k)?.pronunciations?.get(l)?.phoneticSpelling)
                                    textViewDef?.append(System.getProperty("line.separator"))
                                }

                            }
                        }
                    }

                }
                //Log.d("onCreate OxfordActivity", "textview in observe after if = ${textViewDef?.text}")
            }
        )
    }
}
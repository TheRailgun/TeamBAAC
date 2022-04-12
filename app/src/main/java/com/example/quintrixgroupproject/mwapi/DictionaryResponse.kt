package com.example.quintrixgroupproject.mwapi

import java.lang.IllegalArgumentException

//relies on some data classes in Thesaurus response, decouple them later but
//there was a lot of code in common so I kept it this way
//broken in the exact same way that ThesaurusResponse is
//com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $
//One of the errors is that the response type from the api call is not a DictionaryResponse but
//a list of DictionaryResponses (assume this is also the error with ThesaurusResponse and that the fix will be the same)
//the next error is this
//java.lang.RuntimeException: Failed to invoke private com.example.quintrixgroupproject.mwapi.SseqElement() with no args
//caused by
//java.lang.InstantiationException: Can't instantiate abstract class com.example.quintrixgroupproject.mwapi.SseqElement
data class DictionaryResponse(
    val meta : DMeta,
    val hwi : DHwi,
    val vrs : List<DVR>,
    val fl : String,
    //val def : List<DDef>,
    val data : String,
    val shortdef : List<String>
)

//Expected BEGIN_OBJECT but was STRING at line 1 column 452 path $[0].def[0].sseq[0][0][0]
//when the element inside the list is Senses (a data class)
//When I make it a string I get
//Expected a string but was BEGIN_OBJECT at line 1 column 460 path $[0].def[0].sseq[0][0][1]
//not sure how to fix this
//this seems like a problem with the JSON and not in my code but I could be wrong
//get the same problem when trying a few diff words with the api
data class DDef(
    val sseq : List<List<List<Senses>>>
)

data class Senses(
    val sense : SenseElem
)

open class Senses2(){
    class SenseString(val value : String = "") : Senses2()
    class SenseElemClass(val value : SenseElem? = null) : Senses2()
    class SenseEmpty() : Senses2()


    fun toValue() : Any? {
        return when(this) {
            is SenseString -> value
            is SenseElemClass -> value
            else -> throw IllegalArgumentException()
        }
    }

}

data class SenseElem (
    val sn : String,
    val dt : List<List<String>>
)

data class DHwi(
    val hw : String,
    val prs : List<DPR>
)

data class DPR(
    val mw : String,
    val sound : Sound
)

data class Sound(
    val audio : String,
    val ref : String,
    val stat : String
)

data class NearListElement(
    val wd : String
)

data class DMeta(
    val id : String,
    val uuid : String,
    val sort : String,
    val src : String,
    val section : String,
    val stems : List<String>,
    //val syns : List<List<String>>,
    //val ants : List<List<String>>,
    val offensive : Boolean
)

data class DVR(
    val vl : String,
    val va : String
)
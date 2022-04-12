package com.example.quintrixgroupproject.mwapi

//currently very broken, still trying to fix it.
//moved onto Dictionary response in the meantime
//com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $
//https://stackoverflow.com/questions/9598707/gson-throwing-expected-begin-object-but-was-begin-array

data class ThesaurusResponse(
    val meta : TMeta,
    val hwi : THwi,
    val vrs: List<TVR>,
    val fl : String,
    val sls : List<String>,
    val def : List<TDef>,
    val shortdef : List<String>
)

data class TDef(
    val sseq : List<List<List<SseqElement>>>
)


sealed class SseqElement(val v : String? = null) {
    class SseqClassValue(val value : SseqClass? = null) : SseqElement()
    class StringValue(val value : String = "") : SseqElement()
    class EmptyValue() : SseqElement()
}




/*
data class SseqElement(
    val sense : SseqClass
)

 */



sealed class DtUnion {
    class DtClassArrayValue(val value : List<DtClass>) : DtUnion()
    class StringValue(val value : String) : DtUnion()
}



data class SseqClass(
    val dt : List<List<DtUnion>>,
    val syn_list : List<List<RelListElement>>,
    val rel_list : List<List<RelListElement>>,
    val sim_list : List<List<Sim>>,
    val opp_list : List<List<Opp>>,
    val nearList : List<List<NearListElement>>? = null
)

data class Opp(
    val wd : String
)

data class Sim(
    val wd : String,
    val wvrs : List<Wvr>? = null
)

data class Wvr (
    val wvl: String,
    val wva: String
)

data class DtClass (
    val t: String
)

data class RelListElement (
    val wd: String
)


data class THwi (
    val hw: String
)

data class TMeta (
    val id: String,
    val uuid: String,
    val src: String,
    val section: String,
    val target: Target,
    val stems: List<String>,
    val syns: List<List<String>>,
    val ants: List<List<String>>,
    val offensive: Boolean
)

data class Target (
    val tuuid: String,
    val tsrc: String
)

data class TVR(
    val vl : String,
    val va : String
)

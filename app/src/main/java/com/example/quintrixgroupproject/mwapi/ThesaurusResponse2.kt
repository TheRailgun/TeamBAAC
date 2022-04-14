package com.example.quintrixgroupproject.mwapi

class ThesaurusResponse2 : ArrayList<ThesaurusResponse2Item>()

data class ThesaurusResponse2Item(
    val def: List<T2Def>,
    val fl: String,
    val hwi: T2Hwi,
    val meta: T2Meta,
    val shortdef: List<String>
)

data class T2Def(
    val sseq: List<List<List<Any>>>
)

data class T2Hwi(
    val hw: String
)

data class T2Meta(
    val ants: List<Any>,
    val id: String,
    val offensive: Boolean,
    val section: String,
    val src: String,
    val stems: List<String>,
    val syns: List<List<String>>,
    val target: T2Target,
    val uuid: String
)

data class T2Target(
    val tsrc: String,
    val tuuid: String
)
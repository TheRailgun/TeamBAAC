package com.example.quintrixgroupproject.api

//reusing some data classes from the EntriesResponse.
//not sure if this is good practice or if I should rewrite some redundant code
//just to keep the two Responses decoupled (not really reusing much here, just LexicalCategory
//and GrammaticalFeature)

data class LemmasResponse(
    val metaData: MetaData,
    val results : List<LemmaResult>
)

data class LemmaResult(
    val id: String,
    val language: String,
    val lexicalEntries: List<LemmaLexicalEntry>,
    val type: String,
    val word: String
)

data class LemmaLexicalEntry(
    val grammaticalFeatures: List<GrammaticalFeature>,
    val inflectionOf: List<LexicalCategory>,
    val language: String,
    val lexicalCategory: LexicalCategory,
    val text: String
)
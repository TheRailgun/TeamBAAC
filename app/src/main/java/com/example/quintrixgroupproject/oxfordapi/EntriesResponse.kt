package com.example.quintrixgroupproject.oxfordapi


data class EntriesResponse(
    val metadata : MetaData,
    val results: List<Result>
)


data class MetaData(
    val operation : String,
    val provider : String,
    val schema : String
)


data class Result(
    val id : String,
    val language: String,
    val lexicalEntries: List<LexicalEntry>,
    val pronunciations: List<Pronunciation>,
    val type: String,
    val word: String
)



data class LexicalEntry (
    val compounds: List<Compound>,
    val derivativeOf: List<Compound>,
    val derivatives: List<Compound>,
    val entries: List<Entry>,
    val grammaticalFeatures: List<GrammaticalFeature>,
    val language: String,
    val lexicalCategory: LexicalCategory,
    val notes: List<GrammaticalFeature>,
    val phrasalVerbs: List<Compound>,
    val phrases: List<Compound>,
    val pronunciations: List<Pronunciation>,
    val root: String,
    val text: String,
    val variantForms: List<VariantForm>
)

data class Compound (
    val domains: List<LexicalCategory>,
    val id: String,
    val language: String,
    val regions: List<LexicalCategory>,
    val registers: List<LexicalCategory>,
    val text: String
)

data class LexicalCategory (
    val id: String,
    val text: String
)

data class Entry (
    val crossReferenceMarkers: List<String>,
    val crossReferences: List<GrammaticalFeature>,
    val etymologies: List<String>,
    val grammaticalFeatures: List<GrammaticalFeature>,
    val homographNumber: String,
    val inflections: List<Inflection>,
    val notes: List<GrammaticalFeature>,
    val pronunciations: List<Pronunciation>,
    val senses: List<Sense>,
    val variantForms: List<VariantForm>
)

data class GrammaticalFeature (
    val id: String,
    val text: String,
    val type: String
)

data class Inflection (
    val domains: List<LexicalCategory>,
    val grammaticalFeatures: List<GrammaticalFeature>,
    val inflectedForm: String,
    val lexicalCategory: LexicalCategory,
    val pronunciations: List<Pronunciation>,
    val regions: List<LexicalCategory>,
    val registers: List<LexicalCategory>
)

data class Pronunciation (
    val audioFile: String,
    val dialects: List<String>,
    val phoneticNotation: String,
    val phoneticSpelling: String,
    val regions: List<LexicalCategory>,
    val registers: List<LexicalCategory>
)

data class Sense (
    val antonyms: List<Compound>,
    val constructions: List<VariantForm>,
    val crossReferenceMarkers: List<String>,
    val crossReferences: List<GrammaticalFeature>,
    val definitions: List<String>,
    val domainClasses: List<LexicalCategory>,
    val domains: List<LexicalCategory>,
    val etymologies: List<String>,
    val examples: List<Example>,
    val id: String,
    val inflections: List<Inflection>,
    val notes: List<GrammaticalFeature>,
    val pronunciations: List<Pronunciation>,
    val regions: List<LexicalCategory>,
    val registers: List<LexicalCategory>,
    val semanticClasses: List<LexicalCategory>,
    val shortDefinitions: List<String>,
    val subsenses: List<Sense>,
    val synonyms: List<Compound>,
    val thesaurusLinks: List<ThesaurusLink>,
    val variantForms: List<VariantForm>
)

//didn't know you could have recursive nesting of data classes...
//data class SubSenses()

data class VariantForm (
    val domains: List<LexicalCategory>,
    val examples: List<List<String>>? = null,
    val notes: List<GrammaticalFeature>,
    val regions: List<LexicalCategory>,
    val registers: List<LexicalCategory>,
    val text: String,
    val pronunciations: List<Pronunciation>? = null
)

data class Example (
    val definitions: List<String>,
    val domains: List<LexicalCategory>,
    val notes: List<GrammaticalFeature>,
    val regions: List<LexicalCategory>,
    val registers: List<LexicalCategory>,

    //(name = "senseIds")
    val senseIds: List<String>,

    val text: String
)

data class ThesaurusLink (
    //(name = "entry_id")
    val entry_id: String,

    //(name = "sense_id")
    val sense_id: String
)

package fr.epf.min2.projetkotlinquintin_denis.models

import java.io.Serializable

data class CountryResponse(
    val name: Name,
    val capital: List<String>,
    val flags: Flags
) : Serializable

data class Name(
    val common: String,
    val official: String
) : Serializable

data class Flags(
    val png: String,
    val svg: String,
    val alt: String
) : Serializable

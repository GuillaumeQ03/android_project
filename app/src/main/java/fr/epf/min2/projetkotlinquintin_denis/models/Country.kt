package fr.epf.min2.projetkotlinquintin_denis.models

import java.io.Serializable

data class Country(
    val flag: String,
    val capital: String,
    val name: String
) : Serializable

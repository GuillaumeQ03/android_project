package fr.epf.min2.projetkotlinquintin_denis.models

import java.io.Serializable

data class CountryDetailsResponse(
    val name: Name,
    val tld: List<String>,
    val cca2: String,
    val ccn3: String,
    val cca3: String,
    val cioc: String,
    val independent: Boolean,
    val status: String,
    val unMember: Boolean,
    val currencies: Map<String, Currency>,
    val idd: IDD,
    val capital: List<String>,
    val altSpellings: List<String>,
    val region: String,
    val subregion: String,
    val languages: Map<String, String>,
    val translations: Map<String, Translation>,
    val latlng: List<Double>,
    val landlocked: Boolean,
    val borders: List<String>,
    val area: Double,
    val demonyms: Demonyms,
    val flag: String,
    val maps: Maps,
    val population: Int,
    val gini: Map<String, Double>,
    val fifa: String,
    val car: Car,
    val timezones: List<String>,
    val continents: List<String>,
    val flags: Flags,
    val coatOfArms: CoatOfArms,
    val startOfWeek: String,
    val capitalInfo: CapitalInfo,
    val postalCode: PostalCode
) : Serializable

data class Currency(
    val name: String,
    val symbol: String
) : Serializable

data class IDD(
    val root: String,
    val suffixes: List<String>
) : Serializable

data class Translation(
    val official: String,
    val common: String
) : Serializable

data class Demonyms(
    val eng: Demonym,
    val fra: Demonym
) : Serializable

data class Demonym(
    val f: String,
    val m: String
) : Serializable

data class Maps(
    val googleMaps: String,
    val openStreetMaps: String
) : Serializable

data class Car(
    val signs: List<String>,
    val side: String
) : Serializable

data class CoatOfArms(
    val png: String,
    val svg: String
) : Serializable

data class CapitalInfo(
    val latlng: List<Double>
) : Serializable

data class PostalCode(
    val format: String,
    val regex: String
) : Serializable

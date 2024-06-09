package fr.epf.min2.projetkotlinquintin_denis.api

import fr.epf.min2.projetkotlinquintin_denis.models.CountryDetailsResponse
import fr.epf.min2.projetkotlinquintin_denis.models.CountryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RestCountriesApi {
    @GET("name/{name}")
    suspend fun getCountryByName(@Path("name") name: String): List<CountryDetailsResponse>

    @GET("all?fields=name,capital,flags")
    suspend fun getAllCountries(): List<CountryResponse>
}
package fr.epf.min2.projetkotlinquintin_denis.models

fun transformCountryResponse(response: List<CountryResponse>): List<Country> {
    return response.map {
        Country(
            flag = it.flags.png,
            capital = it.capital.firstOrNull() ?: "No Capital",
            name = it.name.official
        )
    }
}

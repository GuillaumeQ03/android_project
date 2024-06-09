package fr.epf.min2.projetkotlinquintin_denis.models

fun transformCountryDetailsResponse(response: CountryDetailsResponse): CountryDetails {
    val englishTranslation = response.translations["eng"]?.common ?: "No English Translation"
    val currency = response.currencies.values.firstOrNull()
    val currencyName = currency?.name ?: "No Currency"
    val currencySymbol = currency?.symbol ?: "No Symbol"

    return CountryDetails(
        flagPng = response.flags.png,
        commonName = response.name.common,
        officialName = response.name.official,
        independent = response.independent,
        currencyName = currencyName,
        currencySymbol = currencySymbol,
        capital = response.capital.firstOrNull() ?: "No Capital",
        region = response.region,
        subregion = response.subregion,
        languages = response.languages,
        englishTranslation = englishTranslation,
        population = response.population,
        timezone = response.timezones.firstOrNull() ?: "No Timezone",
        googleMapsLink = response.maps.googleMaps
    )
}

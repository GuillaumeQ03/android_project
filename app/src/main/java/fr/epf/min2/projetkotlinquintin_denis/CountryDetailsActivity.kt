package fr.epf.min2.projetkotlinquintin_denis

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import fr.epf.min2.projetkotlinquintin_denis.models.CountryDetails
import java.io.File

class CountryDetailsActivity : AppCompatActivity() {
    private var isSaved = false
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_details)

        val backButton: ImageButton = findViewById(R.id.backButton)
        val saveButton: ImageButton = findViewById(R.id.saveButton)
        val flagImageView: ImageView = findViewById(R.id.flagImageView)
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val capitalTextView: TextView = findViewById(R.id.capitalTextView)
        val currencyTextView: TextView = findViewById(R.id.currencyTextView)
        val regionTextView: TextView = findViewById(R.id.regionTextView)
        val subregionTextView: TextView = findViewById(R.id.subregionTextView)
        val languagesTextView: TextView = findViewById(R.id.languagesTextView)
        val populationTextView: TextView = findViewById(R.id.populationTextView)
        val timezoneTextView: TextView = findViewById(R.id.timezoneTextView)
        val googleMapsLinkTextView: TextView = findViewById(R.id.googleMapsLinkTextView)

        val countryDetails = intent.getSerializableExtra("COUNTRY_DETAILS") as? CountryDetails

        countryDetails?.let {
            nameTextView.text = "${it.commonName} (${it.officialName})"
            capitalTextView.text = "Capital: ${it.capital}"
            currencyTextView.text = "Currency: ${it.currencyName} (${it.currencySymbol})"
            regionTextView.text = "Region: ${it.region}"
            subregionTextView.text = "Subregion: ${it.subregion}"
            languagesTextView.text = "Languages: ${it.languages.values.joinToString(", ")}"
            populationTextView.text = "Population: ${it.population}"
            timezoneTextView.text = "Timezone: ${it.timezone}"
            googleMapsLinkTextView.text = "Google Maps: ${it.googleMapsLink}"
            Picasso.get().load(it.flagPng).into(flagImageView)

            // Check if the country is already saved
            isSaved = isCountrySaved(it.officialName)
            updateSaveButtonIcon(saveButton)
        } ?: run {
            nameTextView.text = "Country details not available"
        }

        backButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            countryDetails?.let {
                if (isSaved) {
                    deleteCountryDetailsFile(it.officialName)
                    isSaved = false
                } else {
                    saveCountryDetailsToFile(it)
                    isSaved = true
                }
                updateSaveButtonIcon(saveButton)
            }
        }
    }

    private fun saveCountryDetailsToFile(countryDetails: CountryDetails) {
        val filename = "${countryDetails.officialName}.json"
        val file = File(filesDir, filename)

        if (!file.exists()) {
            val json = gson.toJson(countryDetails)
            file.writeText(json)
        } else {
            println("File $filename already exists. Skipping saving.")
        }
    }


    private fun deleteCountryDetailsFile(officialName: String) {
        val filename = "$officialName.json"
        val file = File(filesDir, filename)
        if (file.exists()) {
            file.delete()
        }
    }


    private fun isCountrySaved(officialName: String): Boolean {
        val filename = "$officialName.json"
        val file = File(filesDir, filename)
        val existingfile = file.exists()
        Log.d("CountryDetailsActivity", "$existingfile")
        return file.exists()
    }

    private fun updateSaveButtonIcon(saveButton: ImageButton) {
        if (isSaved) {
            saveButton.setImageResource(R.drawable.ic_save)
        } else {
            saveButton.setImageResource(R.drawable.ic_circle_arrow_down)
        }
    }
}

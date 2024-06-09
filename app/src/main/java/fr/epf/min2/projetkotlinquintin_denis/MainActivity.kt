package fr.epf.min2.projetkotlinquintin_denis

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.epf.min2.projetkotlinquintin_denis.adapters.CountryAdapter
import fr.epf.min2.projetkotlinquintin_denis.api.ApiClient
import fr.epf.min2.projetkotlinquintin_denis.models.Country
import fr.epf.min2.projetkotlinquintin_denis.models.CountryDetails
import fr.epf.min2.projetkotlinquintin_denis.models.transformCountryDetailsResponse
import fr.epf.min2.projetkotlinquintin_denis.models.transformCountryResponse
import kotlinx.coroutines.*
import java.io.File
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity(), CountryAdapter.OnCountryClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingOverlay: View
    private var allCountries = listOf<Country>()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        progressBar = findViewById(R.id.progressBar)
        loadingOverlay = findViewById(R.id.loadingOverlay)

        // Clear focus and disable the EditText
        searchEditText.clearFocus()
        searchEditText.isEnabled = false

        // Hide the soft keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)

        countryAdapter = CountryAdapter(this)
        recyclerView.adapter = countryAdapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCountries(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        CoroutineScope(Dispatchers.Main).launch {
            fetchCountries()
        }
    }

    override fun onCountryDetailsClick(country: Country) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                showLoading(true)

                // Clear focus and disable the EditText
                searchEditText.clearFocus()
                searchEditText.isEnabled = false

                // Hide the soft keyboard
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)

                val countryDetails = loadCountryDetails(country.name)
                val intent = Intent(this@MainActivity, CountryDetailsActivity::class.java)
                intent.putExtra("COUNTRY_DETAILS", countryDetails)
                startActivity(intent)

                showLoading(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun loadCountryDetails(countryName: String): CountryDetails {
        val filename = "$countryName.json"
        val file = File(filesDir, filename)
        val gson = Gson()

        return if (file.exists()) {
            val jsonString = file.readText()
            gson.fromJson(jsonString, CountryDetails::class.java)
        } else {
            val response = ApiClient.apiService.getCountryByName(countryName)
            val countryDetailsResponse = response[0]
            val countryDetails = transformCountryDetailsResponse(countryDetailsResponse)

            val json = gson.toJson(countryDetails)
            file.writeText(json)

            countryDetails
        }
    }


    private suspend fun fetchCountries() {
        try {
            showLoading(true)

            val file = File(filesDir, "allCountries.json")
            if (file.exists()) {
                val jsonString = file.readText()
                val type = object : TypeToken<List<Country>>() {}.type
                allCountries = gson.fromJson(jsonString, type)
                Log.d("MainActivity", "Loaded countries from file: $allCountries")
            } else {
                val response = ApiClient.apiService.getAllCountries()
                Log.d("MainActivity", "Raw API response: $response")
                val countries = transformCountryResponse(response)
                Log.d("MainActivity", "Transformed countries: $countries")
                allCountries = countries

                val json = gson.toJson(allCountries)
                file.writeText(json)
            }

            countryAdapter.submitList(allCountries)

            // Re-enable the search EditText and request focus
            searchEditText.isEnabled = true
            searchEditText.requestFocus()

            // Show the soft keyboard
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)

            showLoading(false)
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            Log.e("MainActivity", "SocketTimeoutException: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MainActivity", "Exception: ${e.message}")
        }
    }

    private fun filterCountries(query: String) {
        val filteredList = allCountries.filter {
            it.name.startsWith(query, true) || it.capital.startsWith(query, true)
        }
        countryAdapter.submitList(filteredList)
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        loadingOverlay.visibility = if (show) View.VISIBLE else View.GONE
    }
}

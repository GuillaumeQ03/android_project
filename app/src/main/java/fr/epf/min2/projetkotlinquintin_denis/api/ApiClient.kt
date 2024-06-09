package fr.epf.min2.projetkotlinquintin_denis.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://restcountries.com/v3.1/"
    private const val TIMEOUT = 120L // Timeout duration in seconds
    private const val MAX_RETRIES = 3 // Maximum number of retries

    val apiService: RestCountriesApi by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val retryInterceptor = Interceptor { chain ->
            val request = chain.request()
            val response = tryRequest(chain, request, 0)
            response
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(retryInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RestCountriesApi::class.java)
    }

    @Throws(IOException::class)
    private fun tryRequest(chain: Interceptor.Chain, request: Request, attempt: Int): okhttp3.Response {
        return try {
            chain.proceed(request)
        } catch (e: IOException) {
            if (attempt < MAX_RETRIES) {
                tryRequest(chain, request, attempt + 1)
            } else {
                throw e
            }
        }
    }
}

package fr.epf.min1.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var countryAPI: CountryService
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var countriesRecyclerView: RecyclerView
    private lateinit var countriesAdapter: CountriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.apicountries.com/countries/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

        countryAPI = retrofit.create(CountryService::class.java)
        Log.d("HomeActivity", "Retrofit initialized")

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        countriesRecyclerView = findViewById(R.id.countriesRecyclerView)
        countriesRecyclerView.layoutManager = LinearLayoutManager(this)
        countriesAdapter = CountriesAdapter()
        countriesRecyclerView.adapter = countriesAdapter

        searchButton.setOnClickListener {
            val countryName = searchEditText.text.toString()
            if (countryName.isNotBlank()) {
                searchCountry(countryName)
            } else {
                Toast.makeText(this, "Please enter a country name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchCountry(countryName: String) {
        countryAPI.getCountriesByName(countryName).enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    if (countries != null && countries.isNotEmpty()) {
                        countriesAdapter.setCountries(countries)
                    } else {
                        Toast.makeText(this@HomeActivity, "Country not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Response error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

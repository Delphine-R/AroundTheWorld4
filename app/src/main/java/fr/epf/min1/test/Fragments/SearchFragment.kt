package fr.epf.min1.test.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.epf.min1.test.R
import fr.epf.min1.test.api.CountriesAdapter
import fr.epf.min1.test.api.Country
import fr.epf.min1.test.api.CountryService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {

    private lateinit var countryAPI: CountryService
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var countriesRecyclerView: RecyclerView
    private lateinit var countriesAdapter: CountriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
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
        Log.d("SearchFragment", "Retrofit initialized")

        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)
        countriesRecyclerView = view.findViewById(R.id.countriesRecyclerView)
        countriesRecyclerView.layoutManager = LinearLayoutManager(context)
        countriesAdapter = CountriesAdapter(
            onItemClick = { country -> onCountryClicked(country) },
            onFavoriteClicked = { onFavoriteClicked() }
        )
        countriesRecyclerView.adapter = countriesAdapter

        searchButton.setOnClickListener {
            val countryName = searchEditText.text.toString()
            if (countryName.isNotBlank()) {
                searchCountry(countryName)
            } else {
                Toast.makeText(context, "Please enter a country name", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun searchCountry(countryName: String) {
        countryAPI.getCountriesByName(countryName).enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    if (countries != null && countries.isNotEmpty()) {
                        countriesAdapter.setCountries(countries)
                    } else {
                        Toast.makeText(context, "Country not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Response error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onCountryClicked(country: Country) {
        val detailsFragment = DetailsFragment.newInstance(country)
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onFavoriteClicked() {
        val favoritesFragment = FavoritesFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, favoritesFragment)
            .addToBackStack(null)
            .commit()
    }
}

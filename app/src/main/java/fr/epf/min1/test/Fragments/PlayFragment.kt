package fr.epf.min1.test.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import fr.epf.min1.test.R
import fr.epf.min1.test.api.Country
import fr.epf.min1.test.api.CountryService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class PlayFragment : Fragment() {

    private lateinit var countryAPI: CountryService

    private val countries = listOf(
        "brazil", "spain", "korea", "ireland", "china",
        "russia", "mexico", "france", "nigeria", "egypt"
    )

    private lateinit var countryNameTextView: TextView
    private lateinit var countryFlagImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_play, container, false)
        countryNameTextView = view.findViewById(R.id.country_name_text_view)
        countryFlagImageView = view.findViewById(R.id.country_flag_image_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        Log.d("PlayFragment", "Retrofit initialized")

        searchRandomCountry()
    }

    private fun searchRandomCountry() {
        val random = Random()
        val randomCountryName = countries[random.nextInt(countries.size)]
        searchCountry(randomCountryName)
    }

    private fun searchCountry(countryName: String) {
        countryAPI.getCountriesByName(countryName).enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    if (countries != null && countries.isNotEmpty()) {
                        displayCountryInfo(countries[0])
                    } else {
                        handleEmptyResponse()
                    }
                } else {
                    handleErrorResponse()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    private fun displayCountryInfo(country: Country) {
        countryNameTextView.text = country.name
        Picasso.get().load(country.flags.png).into(countryFlagImageView)
    }

    private fun handleEmptyResponse() {
        Toast.makeText(context, "Country not found", Toast.LENGTH_SHORT).show()
    }

    private fun handleErrorResponse() {
        Toast.makeText(context, "Response error", Toast.LENGTH_SHORT).show()
    }

    private fun handleFailure(t: Throwable) {
        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
    }
}

package fr.epf.min1.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var paysAPI: PaysService
    private lateinit var paysText: TextView
    private lateinit var drapeauImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.apicountries.com/countries/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

        paysAPI = retrofit.create(PaysService::class.java)
        Log.d("HomeActivity", "Retrofit initialized")

        // Initialisation des vues
        paysText = findViewById(R.id.nom)
        drapeauImageView = findViewById(R.id.drapeau)

        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            searchCountry("France") // Recherche du pays France
        }
    }

    private fun searchCountry(countryName: String) {
        paysAPI.getCountriesByName(countryName).enqueue(object : Callback<List<Pays>> {
            override fun onResponse(call: Call<List<Pays>>, response: Response<List<Pays>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    if (countries != null && countries.isNotEmpty()) {
                        val country = countries[0]
                        displayCountry(country)
                    } else {
                        Toast.makeText(this@HomeActivity, "Pays non trouvé", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Erreur de réponse", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pays>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Erreur : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayCountry(country: Pays) {
        val countryName = country.name
        val countryFlagUrl = country.flags.png

        // Afficher le nom du pays
        paysText.text = countryName

        // Charger et afficher le drapeau du pays
        Picasso.get().load(countryFlagUrl).into(drapeauImageView)
    }

}

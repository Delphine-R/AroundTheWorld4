package fr.epf.min1.test.favorites

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Types
import fr.epf.min1.test.api.Country

object FavoritesManager {
    private const val FAVORITES_KEY = "favorites"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val listType = Types.newParameterizedType(List::class.java, Country::class.java)
    private val jsonAdapter = moshi.adapter<List<Country>>(listType)

    fun saveFavoriteCountry(context: Context, country: Country) {
        try {
            val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
            val favorites = getFavoriteCountries(context).toMutableList()

            if (!favorites.contains(country)) {
                favorites.add(country)
                val json = jsonAdapter.toJson(favorites)
                sharedPreferences.edit().putString(FAVORITES_KEY, json).apply()
            } else {
                Log.d("FavoritesManager", "Country ${country.name} is already in favorites.")
            }

        } catch (e: Exception) {
            Log.e("FavoritesManager", "Error saving favorite country", e)
        }
    }

    fun getFavoriteCountries(context: Context): List<Country> {
        try {
            val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
            val json = sharedPreferences.getString(FAVORITES_KEY, null)
            return json?.let {
                jsonAdapter.fromJson(it) ?: emptyList()
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e("FavoritesManager", "Error getting favorite countries", e)
            return emptyList()
        }
    }

    fun removeFavoriteCountry(context: Context, country: Country) {
        try {
            val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
            val favorites = getFavoriteCountries(context).toMutableList()
            favorites.remove(country)
            val json = jsonAdapter.toJson(favorites)
            sharedPreferences.edit().putString(FAVORITES_KEY, json).apply()
        } catch (e: Exception) {
            Log.e("FavoritesManager", "Error removing favorite country", e)
        }
    }
}

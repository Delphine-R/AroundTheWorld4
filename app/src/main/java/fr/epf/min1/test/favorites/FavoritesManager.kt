package fr.epf.min1.test.favorites

import android.content.Context
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
        val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favorites = getFavoriteCountries(context).toMutableList()
        favorites.add(country)
        val json = jsonAdapter.toJson(favorites)
        sharedPreferences.edit().putString(FAVORITES_KEY, json).apply()
    }

    fun getFavoriteCountries(context: Context): List<Country> {
        val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(FAVORITES_KEY, null)
        return json?.let {
            jsonAdapter.fromJson(it) ?: emptyList()
        } ?: emptyList()
    }
}

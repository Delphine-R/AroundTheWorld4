package fr.epf.min1.test.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryService {
    @GET("name/{name}")
    fun getCountriesByName(@Path("name") name: String): Call<List<Country>>
}

@Parcelize
data class Country(
    val name: String,
    val capital: String?,
    val flags: Flags,
    val alpha3Code: String? = null,
    val region: String? = null,
    val population: Long? = null,
    val currencies: List<Currency>? = null,
    val languages: List<Language>? = null
) : Parcelable

@Parcelize
data class Flags(
    val svg: String,
    val png: String
) : Parcelable

@Parcelize
data class Currency(
    val code: String? = null,
    val name: String? = null,
    val symbol: String? = null
) : Parcelable

@Parcelize
data class Language(
    val iso639_1: String? = null,
    val iso639_2: String? = null,
    val name: String? = null,
    val nativeName: String? = null
) : Parcelable

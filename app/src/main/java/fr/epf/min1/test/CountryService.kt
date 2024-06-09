package fr.epf.min1.test

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryService {
    @GET("name/{name}")
    fun getCountriesByName(@Path("name") name: String): Call<List<Country>>
}

data class Country(
    val name: String,
    val alpha3Code: String,
    val region: String,
    val population: Long,
    val flags: Flags,
    val currencies: List<Currency>,
    val languages: List<Language>
)

data class Flags(
    val svg: String,
    val png: String
)

data class Currency(
    val code: String,
    val name: String,
    val symbol: String
)

data class Language(
    val iso639_1: String,
    val iso639_2: String,
    val name: String,
    val nativeName: String
)

package fr.epf.min1.test.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.epf.min1.test.R

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    private var countries: List<Country> = emptyList()

    fun setCountries(countries: List<Country>) {
        this.countries = countries
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(country)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val countryNameTextView: TextView = itemView.findViewById(R.id.countryNameTextView)
        private val countryFlagImageView: ImageView = itemView.findViewById(R.id.countryFlagImageView)

        fun bind(country: Country) {
            countryNameTextView.text = country.name
            Picasso.get().load(country.flags.png).into(countryFlagImageView)
        }
    }
}

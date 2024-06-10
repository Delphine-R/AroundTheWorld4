package fr.epf.min1.test.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.epf.min1.test.R
import fr.epf.min1.test.api.Country

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    private var favoriteCountries: List<Country> = emptyList()

    fun setFavoriteCountries(favoriteCountries: List<Country>) {
        this.favoriteCountries = favoriteCountries
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val country = favoriteCountries[position]
        holder.bind(country)
    }

    override fun getItemCount(): Int {
        return favoriteCountries.size
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val countryNameTextView: TextView = itemView.findViewById(R.id.countryNameTextView)

        fun bind(country: Country) {
            countryNameTextView.text = country.name
        }
    }
}

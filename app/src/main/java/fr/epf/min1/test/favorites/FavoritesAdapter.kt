package fr.epf.min1.test.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.epf.min1.test.R
import fr.epf.min1.test.api.Country

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    private var favoriteCountries: List<Country> = emptyList()
    private var onDeleteClickListener: ((Country) -> Unit)? = null

    fun setFavoriteCountries(favoriteCountries: List<Country>) {
        this.favoriteCountries = favoriteCountries
        notifyDataSetChanged()
    }

    fun setOnDeleteClickListener(listener: ((Country) -> Unit)?) {
        onDeleteClickListener = listener
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
        private val countryFlagImageView: ImageView = itemView.findViewById(R.id.countryFlagImageView)
        private val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)

        init {
            deleteButton.setOnClickListener {
                onDeleteClickListener?.invoke(favoriteCountries[adapterPosition])
            }
        }

        fun bind(country: Country) {
            countryNameTextView.text = country.name
            Picasso.get().load(country.flags.png).into(countryFlagImageView)
        }
    }
}




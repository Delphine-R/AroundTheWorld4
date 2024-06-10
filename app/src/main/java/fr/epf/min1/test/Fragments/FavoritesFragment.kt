package fr.epf.min1.test.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.min1.test.R
import fr.epf.min1.test.favorites.FavoritesManager
import fr.epf.min1.test.favorites.FavoritesAdapter

class FavoritesFragment : Fragment() {

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerView = view.findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        favoritesAdapter = FavoritesAdapter()
        recyclerView.adapter = favoritesAdapter
        return view
    }

    override fun onResume() {
        super.onResume()
        val favoriteCountries = FavoritesManager.getFavoriteCountries(requireContext())
        favoritesAdapter.setFavoriteCountries(favoriteCountries)
    }
}

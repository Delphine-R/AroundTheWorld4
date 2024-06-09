package fr.epf.min1.test

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import fr.epf.min1.test.Fragments.FavoritesFragment
import fr.epf.min1.test.Fragments.PlayFragment
import fr.epf.min1.test.Fragments.SearchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton: Button = findViewById(R.id.playButton)
        val searchButton: Button = findViewById(R.id.searchButton)
        val favoritesButton: Button = findViewById(R.id.favoritesButton)

        playButton.setOnClickListener { loadFragment(PlayFragment()) }
        searchButton.setOnClickListener { loadFragment(SearchFragment()) }
        favoritesButton.setOnClickListener { loadFragment(FavoritesFragment()) }

        if (savedInstanceState == null) {
            loadFragment(SearchFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}

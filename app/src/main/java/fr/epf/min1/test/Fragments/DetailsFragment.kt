package fr.epf.min1.test.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import fr.epf.min1.test.R
import fr.epf.min1.test.api.Country

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        val country = arguments?.getParcelable<Country>("country")

        country?.let {
            val countryNameTextView = view.findViewById<TextView>(R.id.countryNameTextView)
            val countryCapitalTextView = view.findViewById<TextView>(R.id.countryCapitalTextView)
            val countryFlagImageView = view.findViewById<ImageView>(R.id.countryFlagImageView)
            val countryRegionTextView = view.findViewById<TextView>(R.id.countryRegionTextView)
            val countryPopulationTextView = view.findViewById<TextView>(R.id.countryPopulationTextView)
            val countryCurrenciesTextView = view.findViewById<TextView>(R.id.countryCurrenciesTextView)
            val countryLanguagesTextView = view.findViewById<TextView>(R.id.countryLanguagesTextView)

            countryNameTextView.text = country.name
            countryCapitalTextView.text = "Capital: ${country.capital ?: "N/A"}"
            Picasso.get().load(country.flags.png).into(countryFlagImageView)
            countryRegionTextView.text = "Region: ${country.region ?: "N/A"}"
            countryPopulationTextView.text = "Population: ${country.population ?: "N/A"}"
            countryCurrenciesTextView.text = "Currencies: ${country.currencies?.joinToString { it.name ?: "" } ?: "N/A"}"
            countryLanguagesTextView.text = "Languages: ${country.languages?.joinToString { it.name ?: "" } ?: "N/A"}"
        }

        return view
    }

    companion object {
        fun newInstance(country: Country): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putParcelable("country", country)
            fragment.arguments = args
            return fragment
        }
    }
}

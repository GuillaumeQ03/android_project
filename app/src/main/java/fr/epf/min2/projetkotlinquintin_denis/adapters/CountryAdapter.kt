package fr.epf.min2.projetkotlinquintin_denis.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import fr.epf.min2.projetkotlinquintin_denis.models.Country
import fr.epf.min2.projetkotlinquintin_denis.R
import com.squareup.picasso.Picasso

class CountryAdapter(private val listener: OnCountryClickListener) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    private var countries = listOf<Country>()

    interface OnCountryClickListener {
        fun onCountryDetailsClick(country: Country)
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flagImageView: ImageView = itemView.findViewById(R.id.flagImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val capitalTextView: TextView = itemView.findViewById(R.id.capitalTextView)
        val detailsButton: Button = itemView.findViewById(R.id.detailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.nameTextView.text = country.name
        holder.capitalTextView.text = country.capital
        Picasso.get().load(country.flag).into(holder.flagImageView)

        holder.detailsButton.setOnClickListener {
            listener.onCountryDetailsClick(country)
        }
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Country>) {
        countries = list
        notifyDataSetChanged()
    }
}

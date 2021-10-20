package com.clay.weatherforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Clayton Hatathlie on 10/18/21
 **/
class WeatherAdapter(private val list: List<Daily>,
) : RecyclerView.Adapter<WeatherAdapter.WeatherHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return WeatherHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val cxt = holder.itemView.context
        holder.day.text = list[position].getDay()
        holder.rain.text = cxt.getString(R.string.rain_holder, list[position].getRainPercentage().toInt())
        holder.highlow.text = cxt.getString(
            R.string.high_low_holder, list[position].temp.max.toInt(), list[position].temp.min.toInt()
        )
        holder.forecastImage.setImageDrawable(
            ContextCompat.getDrawable(cxt, list[position].getWeatherImage())
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class WeatherHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView = view.findViewById(R.id.day)
        val rain: TextView = view.findViewById(R.id.rain)
        val highlow: TextView = view.findViewById(R.id.highlow)
        val forecastImage : ImageView = view.findViewById(R.id.forecastImage)
    }
}
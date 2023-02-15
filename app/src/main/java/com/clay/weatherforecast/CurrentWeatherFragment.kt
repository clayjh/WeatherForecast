package com.clay.weatherforecast

import android.app.AlertDialog
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.clay.weatherforecast.databinding.CurrentWeatherFragmentBinding
import java.lang.Exception
import java.util.*

class CurrentWeatherFragment : Fragment() {

    private val sharedWeatherViewModel: SharedWeatherViewModel by activityViewModels()
    //private val mainViewModel: MainViewModel by activityViewModels()
    private var binding: CurrentWeatherFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)

        binding?.let {
            it.forecastButton.setOnClickListener {
                findNavController().navigate(R.id.action_currentWeatherFragment_to_forecastFragment)
            }
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedWeatherViewModel.lastLocation.observe(viewLifecycleOwner) {
            it?.let {
                sharedWeatherViewModel.getCityWeather(it)
            }
        }

        sharedWeatherViewModel.weatherResult.observe(viewLifecycleOwner) {
            // set data
            when(it) {
                is WeatherResult.OnSuccess -> {
                    if (it.weatherData != null) {
                        setData(it.weatherData)
                    } else {
                        showWeatherError()
                    }
                }
                is WeatherResult.OnError -> {
                    showWeatherError()
                }
            }
        }
    }

    private fun setData(data: WeatherData) {
        binding?.let {
            it.weatherImage.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), data.current.getLargeWeatherImage())
            )
            it.degree.text = getString(R.string.degree_holder, data.current.temp.toInt())
            it.area.text = getCityState(data.lat, data.lon)
            it.status.text = data.current.weather[0].main
            it.high.text = getString(R.string.high_holder, getString(R.string.degree_holder, data.daily[0].temp.max.toInt()))
            it.low.text = getString(R.string.low_holder, getString(R.string.degree_holder, data.daily[0].temp.min.toInt()))
            it.humidity.text = getString(R.string.humidity_holder, data.current.humidity)
        }
    }

    private fun getCityState(lat: Double, lon: Double) : String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            val city = addresses[0].locality
            val state = addresses[0].adminArea
            "$city, $state"
        } catch (e: Exception) {
            ""
        }
    }

    private fun showWeatherError() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.error_weather_lookup))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                (activity as MainActivity).getLastLocation()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}
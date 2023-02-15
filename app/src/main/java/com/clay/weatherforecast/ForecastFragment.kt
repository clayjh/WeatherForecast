package com.clay.weatherforecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clay.weatherforecast.databinding.ForecastFragmentBinding

class ForecastFragment : Fragment() {

    private val sharedWeatherViewModel: SharedWeatherViewModel by activityViewModels()
    private var binding: ForecastFragmentBinding? = null
    private lateinit var weatherRecyclerView : RecyclerView
    private val weatherAdapter = WeatherAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ForecastFragmentBinding.inflate(inflater, container, false)
        setupView()
        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedWeatherViewModel.weatherResult.observe(viewLifecycleOwner) {
            it?.let {
                when(it) {
                    is WeatherResult.OnSuccess -> {
                        if (it.weatherData != null) {
                            weatherAdapter.setData(it.weatherData.daily)
                        } else {
                            leaveForecast()
                        }
                    }
                    is WeatherResult.OnError -> {
                        leaveForecast()
                    }
                }
            }
        }
    }

    private fun setupView() {
        binding?.let {
            weatherRecyclerView = it.forecastRecyclerView
            weatherRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            weatherRecyclerView.adapter = weatherAdapter
        }
    }

    private fun leaveForecast() = findNavController().navigateUp()
}
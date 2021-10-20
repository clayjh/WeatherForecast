package com.clay.weatherforecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clay.weatherforecast.databinding.ForecastFragmentBinding

class ForecastFragment : Fragment() {

    private val sharedWeatherViewModel: SharedWeatherViewModel by activityViewModels()
    //private val mainViewModel: MainViewModel by activityViewModels()

    private var binding: ForecastFragmentBinding? = null
    private lateinit var weatherRecyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ForecastFragmentBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedWeatherViewModel.liveWeatherResult().observe(viewLifecycleOwner) {
            // set data
            when(it) {
                is WeatherResult.OnSuccess -> {
                    if (it.weatherData != null) {
                        setData(it.weatherData)
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

    private fun setData(data: WeatherData) {
        binding?.let {
            weatherRecyclerView = it.forecastRecyclerView
            weatherRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            weatherRecyclerView.adapter = WeatherAdapter(data.daily)
        }
    }

    private fun leaveForecast() = findNavController().navigateUp()

}
package com.clay.weatherforecast

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Clayton Hatathlie on 10/18/21
 **/
class MainViewModel : ViewModel() {

    var lastLocation = MutableLiveData<Location>()
    fun liveLocation() : LiveData<Location> = lastLocation
}
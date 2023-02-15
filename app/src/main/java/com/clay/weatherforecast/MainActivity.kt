package com.clay.weatherforecast

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.clay.weatherforecast.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val sharedWeatherViewModel: SharedWeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupNavController()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = when (destination.id) {
                R.id.currentWeatherFragment -> getString(R.string.title_current_weather)
                R.id.forecastFragment -> getString(R.string.title_7_forecast)
                else -> getString(R.string.app_name)
            }
        }
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener { taskLocation ->
                if (taskLocation.isSuccessful && taskLocation.result != null) {
                    sharedWeatherViewModel.setLocation(taskLocation.result)
                } else {
                    val snackbar = Snackbar.make(
                        CoordinatorLayout(this),
                        getString(R.string.error_could_not_location),
                        LENGTH_INDEFINITE
                    )
                    snackbar.setAction(getString(R.string.try_again)) {
                        getLastLocation()
                    }
                    snackbar.show()
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkPermissions() =
        checkSelfPermission(ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    private fun startLocationPermissionRequest() {
        requestPermissions(
            arrayOf(ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        if (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("You're location is required to use this app")
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    startLocationPermissionRequest()
                }
                .create()
                .show()
        } else {
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PERMISSION_GRANTED) getLastLocation()
            else {
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Failed to get permission granted. Try Again?")
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        startLocationPermissionRequest()
                    }
                    .create()
                    .show()
            }
        }
    }

    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 143
    }
}
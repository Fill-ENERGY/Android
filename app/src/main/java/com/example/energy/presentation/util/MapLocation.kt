package com.example.energy.presentation.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices

class MapLocation {
    companion object {

        val MAPPERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val LOCATION_PERMISSION_REQUEST_CODE = 5000

        @SuppressLint("MissingPermission")
        fun getCurrentLocation(context : Context, fragment: Fragment, activity: Activity,
                               callback: (Location) -> Unit) {
            if (!hasPermission(context)) {
                fragment.requestPermissions(
                    MAPPERMISSIONS,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                val fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(activity)
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { success: Location? ->
                        success?.let { location ->
                            callback(location)
                        }
                    }
                    .addOnFailureListener { fail ->
                    }
            }

        }

        fun hasPermission(context : Context): Boolean {
            for (permission in MAPPERMISSIONS) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

    }

}
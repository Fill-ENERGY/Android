package com.example.energy.presentation.util

import ResultSearchKeyword
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.energy.BuildConfig
import com.example.energy.data.repository.map.MapInterface
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

const val KAKAO_KEY = BuildConfig.KAKAO_REST_KEY
class MapLocation {
    companion object {

        val MAPPERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val LOCATION_PERMISSION_REQUEST_CODE = 5000

        @SuppressLint("MissingPermission")
        fun getCurrentLocation(
            context: Context, fragment: Fragment, activity: Activity,
            callback: (Location) -> Unit
        ) {
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

        fun hasPermission(context: Context): Boolean {
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

        //위도 경도를 주소로 변환
        fun getGeoCoder(latitude:Double,longitude:Double, context: Context) : String{
            val geocoder: Geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null) {
                return if (addresses.isNotEmpty()) {
                    val cityName = addresses?.get(0)?.subLocality ?: "00구"
                    val neighborhoodName = addresses?.get(0)?.thoroughfare ?: "00동"
                    "$cityName $neighborhoodName"
                } else {
                    "00구 00동"
                }
            }
            return "00구 00동"
        }

    }

}
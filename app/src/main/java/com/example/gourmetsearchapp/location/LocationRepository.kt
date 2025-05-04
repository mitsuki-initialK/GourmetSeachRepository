package com.example.gourmetsearchapp.location

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


interface LocationRepository {
    suspend fun getCoordinates() : Location
    suspend fun getAddressFromCoordinate(lat : Double, lng : Double) : String
    fun requestLocationPermission()
}


class NetworkLocationRepository(
    private val activity: Activity
) : LocationRepository {


    init{
        requestLocationPermission()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCoordinates() : Location {
        return suspendCancellableCoroutine { continuation ->
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw SecurityException("Location permissions are not granted.")
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        continuation.resume(location){ e ->
                            continuation.resumeWithException(e) // 失敗時に例外をスロー
                        }
                    } else {
                        continuation.resumeWithException(NullPointerException("位置情報が取得できませんでした"))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e) // 失敗時に例外をスロー
                }
        }
    }


    override suspend fun getAddressFromCoordinate(lat : Double, lng : Double) : String {
        val geocoder = Geocoder(activity, Locale.getDefault())

        return suspendCancellableCoroutine { continuation ->
            geocoder.getFromLocation(lat, lng, 1, object : GeocodeListener{
                override fun onGeocode(addresses: MutableList<Address>) {
                    if(addresses.isNotEmpty()){
                        val address: Address = addresses[0]
                        if (address.getAddressLine(0) != null) {
                            val addressLine = address.getAddressLine(0)
                            continuation.resume(addressLine)
                        }
                    }
                }

                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    continuation.resumeWithException(Exception("ジオコードに失敗しました"))
                }
            })
        }
    }


    override fun requestLocationPermission() {
        val locationPermissionRequestCode = 1001

        // 位置情報の権限があるか確認する
        val isAccept = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!isAccept) {
            // 権限が許可されていない場合はリクエストする
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode
            )
        }
    }

}
package com.example.gourmetsearchapp.Location

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resumeWithException


interface LocationRepositoryInterface {
    suspend fun getCoordinates() : Location
    fun getAddressFromCoordinate(lat : Double, lng : Double) : String
    fun requestLocationPermission()
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class LocationRepository(private val activity: Activity) : LocationRepositoryInterface{

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


    override fun getAddressFromCoordinate(lat : Double, lng : Double) : String {
        val geocoder = Geocoder(activity, Locale.getDefault())

        var addressLine: String? = null

        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(lat, lng, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address: Address = addresses[0]
                    if (address.getAddressLine(0) != null) {
                        addressLine = address.getAddressLine(0)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return requireNotNull(addressLine) { "住所が取得できませんでした" }
    }


    override fun requestLocationPermission() {
        val LOCATION_PERMISSION_REQUEST_CODE = 1001

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
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

}
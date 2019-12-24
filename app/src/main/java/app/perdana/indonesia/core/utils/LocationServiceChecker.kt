package app.perdana.indonesia.core.utils

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import org.jetbrains.anko.alert


object LocationServiceChecker {

    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager?
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
    }

    fun showGpsSettingDialog(context: Context) {
        val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        context.alert {
            message = "GPS Belum di aktifkan."
            positiveButton("Aktifkan GPS") {
                context.startActivity(Intent(action))
                it.dismiss()
            }
            negativeButton("Keluar") {
                it.dismiss()
            }
        }.show()
    }

    fun checkGpsStatus(context: Context, action: () -> Unit) {
        if (isGpsEnabled(context)) action.invoke()
        else showGpsSettingDialog(context)
    }

    fun isLocationEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Settings.Secure.getInt(
                    context.contentResolver, Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF
                )
            } else {
                TODO("VERSION.SDK_INT < KITKAT")
            }
            (mode != Settings.Secure.LOCATION_MODE_OFF)
        }
    }
}
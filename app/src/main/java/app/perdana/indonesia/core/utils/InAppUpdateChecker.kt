package app.perdana.indonesia.core.utils

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

/**
 * Created by ebysofyan on 23/10/20.
 */
object InAppUpdateChecker {
    private const val IN_APP_UPDATE_REQUEST_CODE = 9999
    private lateinit var appUpdateManager: AppUpdateManager

    fun startInAppUpdateFlow(activity: Activity) {
        appUpdateManager = AppUpdateManagerFactory.create(activity)
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.FLEXIBLE,
                    activity,
                    IN_APP_UPDATE_REQUEST_CODE
                )
            }
        }
    }

    fun resumeInAppUpdateFlow(activity: Activity) {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener {
                if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        it,
                        AppUpdateType.IMMEDIATE,
                        activity,
                        IN_APP_UPDATE_REQUEST_CODE
                    )
                }
            }
    }

    fun onInAppActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == IN_APP_UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            appUpdateManager.completeUpdate()
        }
    }
}
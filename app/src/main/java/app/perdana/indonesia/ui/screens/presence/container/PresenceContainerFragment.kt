package app.perdana.indonesia.ui.screens.presence.container

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.ProgressDialogHelper
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.PresenceContainerRequest
import app.perdana.indonesia.data.remote.model.PresenceContainerResponse
import app.perdana.indonesia.ui.screens.presence.item.PresenceItemActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.presence_fragment.*
import org.jetbrains.anko.longToast
import retrofit2.Response

class PresenceContainerFragment : Fragment(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private const val PLAY_SERVICES_REQUEST = 900
        private const val REQUEST_CHECK_SETTINGS = 901

        fun newInstance() =
            PresenceContainerFragment()
    }

    private lateinit var viewModel: PresenceContainerViewModel
    private lateinit var fragmentActivity: AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.presence_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentActivity = activity as AppCompatActivity

        viewModel = ViewModelProvider(this).get(PresenceContainerViewModel::class.java)
        initializeUi()

        if (checkPlayServices()) {
            buildGoogleApiClient()
        }
        checkLocationPermission()
    }

    private fun initializeUi() {
        presence_button_add_new.setOnClickListener { showNewPresenceDialog() }
        initPresenceContainerRecyclerView()

        viewModel.dotsLoading.observe(this.viewLifecycleOwner, Observer { state ->
            showDotsLoading(state)
        })
        viewModel.progressLoading.observe(this.viewLifecycleOwner, Observer { state ->
            showProgressLoading(state.first, state.second)
        })

        viewModel.showDotsLoading(true)
        viewModel.fetchPresencesContainer(context?.formattedToken.toString())
            .observe(this.viewLifecycleOwner, Observer { response ->
                onPresenceContainerResponse(response)
            })
    }

    private fun onPresenceContainerResponse(response: Response<MutableList<PresenceContainerResponse>>?) {
        viewModel.showDotsLoading(false)
        if (response != null) {
            when (response.isSuccessful) {
                true -> response.body()?.let { adapter.addItems(it) }
                else -> context?.longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else context?.longToast(getString(R.string.no_internet_connection))
    }

    private lateinit var adapter: PresenceContainerRecyclerViewAdapter
    private fun initPresenceContainerRecyclerView() {
        adapter =
            PresenceContainerRecyclerViewAdapter { pc ->
                val bundle = bundleOf(Constants.PRESENCE_CONTAINER_RESPONSE_OBJ to pc)
                val intent = Intent(context, PresenceItemActivity::class.java).apply {
                    putExtras(bundle)
                }
                startActivity(intent)
            }
        presence_recycler_view.layoutManager = LinearLayoutManager(context)
        presence_recycler_view.adapter = adapter
    }

    private fun showDotsLoading(show: Boolean) {
        if (show) presence_loading.visible()
        else presence_loading.gone()
    }

    private var progressDialog: ProgressDialog? = null
    private fun showProgressLoading(show: Boolean, msg: String = "Loading") {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context!!)
        }
        progressDialog?.apply {
            setTitle("")
            setMessage(msg)
            isIndeterminate = true
        }
        if (show) progressDialog?.show()
        else progressDialog?.dismiss()
    }

    private var alertDialog : AlertDialog? = null
    private fun showNewPresenceDialog() {
        val dialogBuilder = AlertDialog.Builder(context!!)
        val inflatedView =
            LayoutInflater.from(context).inflate(R.layout.add_presence_dialog_view, null)
        dialogBuilder.setView(inflatedView)

        val textHeader = inflatedView.findViewById<TextView>(R.id.presence_dialog_header)
        val imageClose = inflatedView.findViewById<ImageView>(R.id.presence_dialog_close)
        val textDesc =
            inflatedView.findViewById<TextInputEditText>(R.id.presence_dialog_text_description)
        val buttonSave = inflatedView.findViewById<MaterialButton>(R.id.presence_dialog_button_save)

        alertDialog = dialogBuilder.create()
        alertDialog?.show()
        imageClose.setOnClickListener { alertDialog?.dismiss() }
        buttonSave.setOnClickListener {
            if (textDesc.text.isNullOrEmpty()) {
                textDesc.error = "Masukkan keterangan presensi"
                return@setOnClickListener
            }

            viewModel.showLoading(true to "Membuat presensi baru . . .")
            val presenceContainerRequest = PresenceContainerRequest(
                title = textDesc.text.toString(),
                latitude = lasLocation?.latitude.toString(),
                longitude = lasLocation?.longitude.toString()
            )
            viewModel.addNewPresenceContainer(
                inflatedView.context.formattedToken,
                presenceContainerRequest
            ).observe(this.viewLifecycleOwner, Observer { response ->
                viewModel.hideLoading()
                if (response != null) {
                    when (response.isSuccessful) {
                        true -> {
                            response.body()?.let { pcr -> adapter.addItemIndexed(pcr) }
                            textDesc.text?.clear()
                            context?.longToast("Presensi baru berhasil dibuat")
                            if (alertDialog?.isShowing == true){
                                alertDialog?.dismiss()
                            }
                        }
                        else -> context?.longToast(response.errorBody()?.getErrorDetail().toString())
                    }
                } else context?.longToast(getString(R.string.no_internet_connection))
            })
        }
    }

    private var lasLocation: Location? = null
    private lateinit var googleApiClient: GoogleApiClient
    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()

        val locationRequest = LocationRequest().apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback {
            when (it.status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    getLocation()
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    it.status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }

    private fun checkLocationPermission() {
        Dexter.withActivity(activity).withPermissions(
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ).withListener(object : MultiplePermissionsListener {

            @SuppressLint("MissingPermission")
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (checkPlayServices()) {
                    buildGoogleApiClient()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
            }
        }).check()
    }

    private fun checkPlayServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_REQUEST)
                    .show()
            } else {
                fragmentActivity.longToast("This device is not supported.")
            }
            return false
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        lasLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
    }

    override fun onConnected(p0: Bundle?) {
        getLocation()
    }

    override fun onConnectionSuspended(p0: Int) {
        googleApiClient.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onPause() {
        super.onPause()
        alertDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
    }
}

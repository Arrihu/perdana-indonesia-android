package app.perdana.indonesia.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.perdana.indonesia.R
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_scanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.*


class ScannerFragment : Fragment(R.layout.fragment_scanner), ZXingScannerView.ResultHandler {

    companion object {
        fun newInstance() = ScannerFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScannerWithPermission()
    }

    private fun setupScannerWithPermission() {
        Dexter.withActivity(requireActivity()).withPermissions(
            listOf(
                Manifest.permission.CAMERA
            )
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                scanner_view.setResultHandler(this@ScannerFragment)
                scanner_view.startCamera()

                switch_flashlight.setOnClickListener {
                    switchFlashLight()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {

            }
        }).check()
    }

    private fun switchFlashLight() {
        if (scanner_view.flash) {
            scanner_view.flash = false
            switch_flashlight.setImageResource(R.drawable.ic_flash_on)
        } else {
            scanner_view.flash = true
            switch_flashlight.setImageResource(R.drawable.ic_flash_off)
        }
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            scanner_view.setResultHandler(this)
            scanner_view.startCamera()
        }, 350L)
    }

    override fun onPause() {
        super.onPause()
        scanner_view.stopCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scanner_view?.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        BottomSheetDialogFragmentCheckMembership(rawResult?.text.toString()).show(
            childFragmentManager,
            this::class.java.simpleName
        )
        Handler(Looper.getMainLooper()).postDelayed({
            scanner_view.resumeCameraPreview(this)
        }, 350L)
    }

}
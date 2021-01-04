package app.perdana.indonesia.ui.scanner

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
import kotlinx.android.synthetic.main.fragment_scanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.*


class ScannerFragment : Fragment(R.layout.fragment_scanner), ZXingScannerView.ResultHandler {

    companion object {
        fun newInstance() = ScannerFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanner_view.setResultHandler(this)
        scanner_view.startCamera()

        switch_flashlight.setOnClickListener {
            switchFlashLight()
        }
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
//        Handler(Looper.getMainLooper()).postDelayed({
//            scanner_view.setResultHandler(this)
//            scanner_view.startCamera()
//        }, 1000)
    }

    override fun onPause() {
        super.onPause()
//        scanner_view.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        scanner_view.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        Toast.makeText(requireContext(), rawResult?.text, Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            scanner_view.resumeCameraPreview(this)
        }, 350L)
    }

}
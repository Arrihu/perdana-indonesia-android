package app.perdana.indonesia.ui.fragments.presence.scanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.core.utils.Constants
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.zxing.Result
import kotlinx.android.synthetic.main.presence_scanner_activity.*
import kotlinx.android.synthetic.main.toolbar_dark_theme.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * Created by ebysofyan on 12/25/19.
 */

class PresenceScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.presence_scanner_activity)
        setupActionBar()

        presence_scanner_flash.setOnClickListener {
            presence_scanner_view.flash = !presence_scanner_view.flash
            if (presence_scanner_view.flash) presence_scanner_flash.setImageResource(R.drawable.ic_flash_off)
            else presence_scanner_flash.setImageResource(R.drawable.ic_flash_on)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(_toolbar_dark)
        supportActionBar?.apply {
            title = "Scan Presensi"
            setDisplayHomeAsUpEnabled(true)
        }
        _toolbar_dark.setNavigationIcon(R.drawable.ic_close_white)
        _toolbar_dark.setNavigationOnClickListener {
            finish()
            Animatoo.animateSlideDown(this)
        }
    }

    override fun onResume() {
        super.onResume()
        presence_scanner_view.setResultHandler(this)
        presence_scanner_view.startCamera()
    }

    override fun onPause() {
        super.onPause()
        presence_scanner_view.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        presence_scanner_view.resumeCameraPreview(this)

        val intent = Intent().putExtra(Constants.SCANNER_RESULT, rawResult?.text)
        setResult(Activity.RESULT_OK, intent)
        finish()
        Animatoo.animateSlideDown(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideDown(this)
    }
}
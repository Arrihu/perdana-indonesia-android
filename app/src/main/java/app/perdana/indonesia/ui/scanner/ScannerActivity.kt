package app.perdana.indonesia.ui.scanner

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.setupActionbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.toolbar_dark_theme.*
import pl.aprilapps.easyphotopicker.EasyImage

class ScannerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        _toolbar_dark.setupActionbar(this, getString(R.string.cek_keanggotaan), true) {
            finish()
        }
        openScannerWithPermission()
    }

    private fun openScannerWithPermission() {
        Dexter.withActivity(this).withPermissions(
            listOf(
                Manifest.permission.CAMERA
            )
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.scanner_container,
                    ScannerFragment.newInstance(),
                    this::class.java.simpleName
                ).commit()
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {

            }
        }).check()
    }

}
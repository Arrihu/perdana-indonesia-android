package app.perdana.indonesia.ui.scanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.setupActionbar
import kotlinx.android.synthetic.main.toolbar_dark_theme.*

class ScannerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        _toolbar_dark.setupActionbar(this, getString(R.string.cek_keanggotaan), true) {
            finish()
        }

        supportFragmentManager.beginTransaction().replace(
            R.id.scanner_container,
            ScannerFragment.newInstance(),
            this::class.java.simpleName
        ).commit()
    }
}
package app.perdana.indonesia.ui.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.setupActionbar
import kotlinx.android.synthetic.main.button_primary.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by ebysofyan on 11/26/19.
 */
class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        initializeUi()
    }

    private fun initializeUi() {
        initActionBar()
        initActionListener()
    }

    private fun initActionBar() {
        _toolbar.setupActionbar(this, getString(R.string.register), true) {
            finish()
        }
        primary_button_dark.text = getString(R.string.register)
    }

    private fun initActionListener() {
        primary_button_dark.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when {
            v === primary_button_dark -> {

            }
        }
    }
}
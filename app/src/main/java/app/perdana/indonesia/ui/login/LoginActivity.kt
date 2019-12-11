package app.perdana.indonesia.ui.login

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
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        initializeUi()
    }

    private fun initializeUi() {
        initActionBar()
        initActionListener()
    }

    private fun initActionBar() {
        _toolbar.setupActionbar(this, getString(R.string.login), true) {
            finish()
        }
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
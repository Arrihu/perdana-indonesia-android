package app.perdana.indonesia.ui.intro.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.ui.login.LoginActivity
import app.perdana.indonesia.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.button_outlined_primary.*
import kotlinx.android.synthetic.main.button_primary.*

/**
 * Created by ebysofyan on 12/2/19.
 */
class AuthIntroActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_intro_activity)

        initializeUi()
    }

    private fun initializeUi() {

        initActionListener()
    }

    private fun initActionListener() {
        primary_button_dark.setOnClickListener(this)
        primary_button_outlined_dark.setOnClickListener(this)
    }

    private fun navigateToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun navigateToRegisterScreen() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun onClick(v: View?) {
        when {
            v === primary_button_dark -> {
                navigateToLoginScreen()
            }
            v == primary_button_outlined_dark -> {
                navigateToRegisterScreen()
            }
        }
    }
}
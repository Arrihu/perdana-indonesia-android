package app.perdana.indonesia.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.LocalStorage
import app.perdana.indonesia.ui.intro.auth.AuthIntroActivity
import app.perdana.indonesia.ui.intro.welcome.WelcomeIntroActivity
import app.perdana.indonesia.ui.main.MainActivity

/**
 * Created by ebysofyan on 12/2/19.
 */
class SplashScreenActivity : AppCompatActivity() {
    companion object {
        const val DELAY_TO_NAVIGATE = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        removeStatusBar()
        setContentView(R.layout.splash_screen_activity)

        Handler().postDelayed({
            navigateToLogin()
        }, DELAY_TO_NAVIGATE)
    }

    private fun navigateToLogin() {
        val isAuthenticated =
            LocalStorage.getString(this, Constants.TOKEN)?.isNotEmpty() ?: false

        val intent = if (isAuthenticated) Intent(this, MainActivity::class.java)
        else Intent(this, AuthIntroActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToWelcomeIntro() {
        val isIntroDisplayed =
            LocalStorage.getString(this, Constants.IS_INTRO_DISPLAYED)?.isNotEmpty() ?: false
        val intent = if (isIntroDisplayed) {
            val isAuthenticated =
                LocalStorage.getString(this, Constants.TOKEN)?.isNotEmpty() ?: false

            if (isAuthenticated) Intent(this, MainActivity::class.java)
            else Intent(this, AuthIntroActivity::class.java)
        } else {
            Intent(this, WelcomeIntroActivity::class.java)
        }

        startActivity(intent)
    }

    private fun removeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}
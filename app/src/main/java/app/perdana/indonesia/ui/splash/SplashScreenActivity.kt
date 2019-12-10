package app.perdana.indonesia.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.ui.intro.IntroActivity

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
            navigateTo()
        }, DELAY_TO_NAVIGATE)
    }

    private fun navigateTo() {
        startActivity(Intent(this, IntroActivity::class.java))
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
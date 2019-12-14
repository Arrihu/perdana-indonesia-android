package app.perdana.indonesia

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import pl.aprilapps.easyphotopicker.EasyImage

/**
 * Created by ebysofyan on 12/3/19.
 */
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        EasyImage.configuration(this).apply {
            setImagesFolderName("Perdana Image")
            setAllowMultiplePickInGallery(false)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}
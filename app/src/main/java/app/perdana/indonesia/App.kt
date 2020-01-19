package app.perdana.indonesia

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
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
        initFbFlipper()
    }

    private fun initFbFlipper(){
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(CrashReporterPlugin.getInstance())
            client.addPlugin(NetworkFlipperPlugin())
            client.start()
        }
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}
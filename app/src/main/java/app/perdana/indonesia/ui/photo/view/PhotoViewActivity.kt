package app.perdana.indonesia.ui.photo.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.core.extension.setupActionbar
import app.perdana.indonesia.core.utils.Constants
import kotlinx.android.synthetic.main.photo_view_activity.*

/**
 * Created by ebysofyan on 17/01/20.
 */
class PhotoViewActivity : AppCompatActivity() {
    private var photoUrl : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_view_activity)

        photoUrl = intent.getStringExtra(Constants.IMAGE_URL)
        photo_view_image.loadWithGlidePlaceholder(photoUrl.toString())

        photo_view_toolbar.setupActionbar(this, "Detail Photo", true){
            finish()
        }
    }
}
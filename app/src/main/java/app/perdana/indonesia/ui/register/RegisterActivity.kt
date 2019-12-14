package app.perdana.indonesia.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.setupActionbar
import kotlinx.android.synthetic.main.button_primary.*
import kotlinx.android.synthetic.main.register_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

/**
 * Created by ebysofyan on 11/26/19.
 */
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val ID_CARD_PHOTO_REQUEST_CODE = 101
    }

    private var selectedIdCardFile: File? = null

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
        register_card_photo_image.setOnClickListener(this)
        register_radio_group_org.setOnClickListener(this)
    }

    private fun validateRegisterForm(): Boolean {
        if (register_username_input_layout.editText?.text.toString().isEmpty()) {
            register_username_input_layout.editText?.error = "Username tidak boleh kosong"
            return false
        }

        if (register_password_input_layout.editText?.text.toString().isEmpty()) {
            register_password_input_layout.editText?.error = "Password tidak boleh kosong"
            return false
        }

        return true
    }

    override fun onClick(v: View?) {
        when {
            v === primary_button_dark -> {

            }

            v === register_card_photo_image -> {
                EasyImage.openChooserWithGallery(this, "Pilih Photo", ID_CARD_PHOTO_REQUEST_CODE)
            }

            v === register_radio_group_org -> {
                val radio =
                    (v as RadioGroup).findViewById<RadioButton>(register_radio_group_org.checkedRadioButtonId)
                register_club_input_layout.hint = "Pilih ${radio.text}"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onImagesPicked(
                    p0: MutableList<File>,
                    p1: EasyImage.ImageSource?,
                    p2: Int
                ) {
                    selectedIdCardFile = p0[0]
                }
            })
    }
}
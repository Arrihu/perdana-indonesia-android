package app.perdana.indonesia.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.setupActionbar
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.LocalStorage
import app.perdana.indonesia.data.remote.model.LoginRequest
import app.perdana.indonesia.data.remote.model.LoginResponse
import app.perdana.indonesia.ui.main.MainActivity
import app.perdana.indonesia.ui.register.RegisterActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.button_primary.*
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.toolbar_light_theme.*
import okhttp3.ResponseBody
import org.jetbrains.anko.longToast
import retrofit2.Response

/**
 * Created by ebysofyan on 11/26/19.
 */
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        initializeUi()
    }

    private fun initializeUi() {
        initActionBar()
        initActionListener()

        viewModel.getLoading().observe(this, Observer { loadingState ->
            showLoading(loadingState)
        })
    }

    private fun initActionBar() {
        _toolbar.setupActionbar(this, getString(R.string.login), true) {
            finish()
        }
    }

    private fun initActionListener() {
        primary_button_dark.setOnClickListener(this)
        login_text_register.setOnClickListener(this)
    }

    private fun validateLoginForm(): Boolean {
        if (login_username_input_layout.editText?.text.toString().isEmpty()) {
            login_username_input_layout.editText?.error = "Username tidak boleh kosong"
            return false
        }

        if (login_password_input_layout.editText?.text.toString().isEmpty()) {
            login_password_input_layout.editText?.error = "Password tidak boleh kosong"
            return false
        }

        return true
    }

    private fun submitLogin() {
        val isValid = validateLoginForm()
        if (isValid) {
            val payload = LoginRequest(
                login_username_input_layout.editText?.text.toString(),
                login_password_input_layout.editText?.text.toString()
            )
            viewModel.showLoading(true)
            viewModel.login(payload).observe(this, Observer { response ->
                viewModel.showLoading(false)
                onResponse(response)
            })
        } else return
    }

    private fun onResponse(response: Response<LoginResponse>?) {
        if (response != null) {
            when {
                response.isSuccessful -> {
                    onResponseSuccess(response.body())
                }
                else -> onResponseFailed(response.errorBody())
            }
        } else {
            longToast(getString(R.string.no_internet_connection))
        }
    }

    private fun onResponseSuccess(body: LoginResponse?) {
        LocalStorage.put(this, Constants.TOKEN, body?.token.toString())
        LocalStorage.put(this, Constants.USER_ROLE, body?.role.toString())
        LocalStorage.put(this, Constants.USER_ID, body?.member?.id.toString())
        LocalStorage.put(this, Constants.USER_PROFILE, Gson().toJson(body?.member))

        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun onResponseFailed(errorBody: ResponseBody?) {
        longToast(errorBody?.getErrorDetail().toString())
    }

    override fun onClick(v: View?) {
        when {
            v === primary_button_dark -> {
                submitLogin()
            }

//            v === login_text_no_account -> {
//                startActivity(Intent(this, RegisterActivity::class.java))
//            }

            v === login_text_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            primary_button_dark.apply {
                isEnabled = false
                text = ""
            }
            login_loading.visible()
        } else {
            primary_button_dark.apply {
                isEnabled = true
                text = getString(R.string.login)
            }
            login_loading.gone()
        }
    }
}
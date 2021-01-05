package app.perdana.indonesia.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.perdana.indonesia.R
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.InAppUpdateChecker
import app.perdana.indonesia.core.utils.LocalStorage
import app.perdana.indonesia.data.remote.model.LoginRequest
import app.perdana.indonesia.data.remote.model.LoginResponse
import app.perdana.indonesia.ui.main.MainActivity
import app.perdana.indonesia.ui.register.webview.RegisterWebViewActivity
import app.perdana.indonesia.ui.scanner.ScannerActivity
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.login_activity.*

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
        initActionListener()

        viewModel.getLoading().observe(this, Observer { loadingState ->
            showLoading(loadingState)
        })
        InAppUpdateChecker.startInAppUpdateFlow(this)
    }


    private fun initActionListener() {
        login_button_login.setOnClickListener(this)
        login_button_register.setOnClickListener(this)
        login_button_membership_check.setOnClickListener(this)
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
                handleSubmitLogin(response)
            })
        } else return
    }

    private fun handleSubmitLogin(response: ApiResponseModel<LoginResponse?>) {
        viewModel.showLoading(false)
        when (response) {
            is ApiResponseModel.Success -> {
                val body = response.data
                LocalStorage.put(this, Constants.TOKEN, body?.token.toString())
                LocalStorage.put(this, Constants.USER_ROLE, body?.user?.group.toString())
                LocalStorage.put(this, Constants.USER_PROFILE, Gson().toJson(body?.user))
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
            is ApiResponseModel.Failure -> {
                Toasty.error(this, response.detail).show()
            }
            is ApiResponseModel.Error -> Toasty.error(this, response.e.message.toString()).show()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            login_button_login -> {
                submitLogin()
            }

            login_button_register -> {
                startActivity(Intent(this, RegisterWebViewActivity::class.java))
            }
            login_button_membership_check -> {
                startActivity(Intent(this, ScannerActivity::class.java))
            }
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            login_button_login.apply {
                isEnabled = false
                text = ""
            }
            login_loading.visible()
        } else {
            login_button_login.apply {
                isEnabled = true
                text = getString(R.string.login)
            }
            login_loading.gone()
        }
    }

    override fun onResume() {
        super.onResume()
        InAppUpdateChecker.resumeInAppUpdateFlow(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        InAppUpdateChecker.onInAppActivityResult(requestCode, resultCode)
    }
}
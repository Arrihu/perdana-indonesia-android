package app.perdana.indonesia.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.data.remote.model.LoginRequest
import app.perdana.indonesia.data.repository.UserApiRepository
import com.google.gson.Gson

/**
 * Created by ebysofyan on 12/13/19.
 */
class LoginViewModel : ViewModel() {
    private val userApiRepository = UserApiRepository.getInstance()

    private val loading = MutableLiveData<Boolean>()

    fun showLoading(value: Boolean) {
        loading.value = value
    }

    fun getLoading() = this.loading

    fun login(loginRequest: LoginRequest) = liveData {
        val response = userApiRepository?.login(loginRequest)
        try {
            emit(response)
        } catch (e: Exception) {
            Log.e("Exception", Gson().toJson(e.message.toString()))
            emit(null)
        }
    }
}
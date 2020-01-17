package app.perdana.indonesia.ui.applicant.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.core.extension.fullDateFormat
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.setupActionbar
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.ProgressDialogHelper
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.ArcherMemberResponse
import app.perdana.indonesia.ui.applicant.detail.ApplicantDetailActivity
import kotlinx.android.synthetic.main.applicant_activity.*
import org.jetbrains.anko.longToast
import java.util.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class ApplicantActivity : AppCompatActivity() {

    private lateinit var viewModel: ApplicantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.applicant_activity)
        initializeUi()
    }

    private fun initializeUi() {
        viewModel = ViewModelProvider(this).get(ApplicantViewModel::class.java)
        initActionBar()
        initActionListener()
        initApplicantRecyclerView()

        viewModel.dotsLoading.observe(this, Observer { state ->
            showDotsLoading(state)
        })
        viewModel.progressLoading.observe(this, Observer { state ->
            showProgressLoading(state.first, state.second)
        })

        viewModel.showDotsLoading(true)
        viewModel.fetchMemberApplicants(formattedToken.toString())
            .observe(this, Observer { response ->
                onResponseHandler(response)
            })
    }

    private fun onResponseHandler(response: ApiResponseModel<List<ArcherMemberResponse>>?) {
        viewModel.showDotsLoading(false)
        when {
            response?.data != null -> response.data.let { adapter.addItems(it.toMutableList()) }
            response?.error != null -> longToast(response.error.detail)
            response?.exception != null -> longToast(response.exception.message.toString())
        }
    }


    private fun initActionListener() {

    }


    private fun initActionBar() {
        applicant_toolbar.setupActionbar(
            this,
            "Pendaftaran Anggota",
            true
        ) {
            finish()
        }
        applicant_toolbar.subtitle = Date().fullDateFormat()
    }

    private lateinit var adapter: ApplicantRecyclerViewAdapter
    private fun initApplicantRecyclerView() {
        adapter =
            ApplicantRecyclerViewAdapter { archerMemberResponse ->
                val intent = Intent(this, ApplicantDetailActivity::class.java)
                intent.putExtras(bundleOf(Constants.ARCHER_MEMBER_RESPONSE_OBJ to archerMemberResponse))
                startActivity(intent)
            }
        applicant_recycler_view.layoutManager = LinearLayoutManager(this)
        applicant_recycler_view.adapter = adapter
    }

    private fun showDotsLoading(show: Boolean) {
        if (show) applicant_loading.visible()
        else applicant_loading.gone()
    }

    private fun showProgressLoading(show: Boolean, msg: String = "Loading") {
        val progressDialog = ProgressDialogHelper.getInstance(this)
        progressDialog?.setMessage(msg)
        if (show) progressDialog?.show()
        else progressDialog?.dismiss()
    }
}
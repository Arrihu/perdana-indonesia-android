package app.perdana.indonesia.ui.fragments.scoring.practices

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.setupActionbar
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.ProgressDialogHelper
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.ArcherMemberResponse
import app.perdana.indonesia.data.remote.model.PracticeContainer
import kotlinx.android.synthetic.main.scoring_practice_container_activity.*
import org.jetbrains.anko.longToast
import retrofit2.Response

/**
 * Created by ebysofyan on 12/25/19.
 */
class ScoringPracticeContainerActivity : AppCompatActivity() {

    companion object {
        const val SCANNER_REQUEST_CODE = 1001
    }

    private var archerMemberResponse: ArcherMemberResponse? = null
    private lateinit var viewModel: ScoringPracticeContainerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scoring_practice_container_activity)
        initializeUi()
    }

    private fun initializeUi() {
        viewModel = ViewModelProvider(this).get(ScoringPracticeContainerViewModel::class.java)
        archerMemberResponse =
            intent.getParcelableExtra(Constants.ARCHER_MEMBER_RESPONSE_OBJ) as ArcherMemberResponse
        initActionBar()
        initPresenceItemRecyclerView()

        viewModel.dotsLoading.observe(this, Observer { state ->
            showDotsLoading(state)
        })
        viewModel.progressLoading.observe(this, Observer { state ->
            showProgressLoading(state.first, state.second)
        })

        viewModel.showDotsLoading(true)
        viewModel.fetchPracticesContainer(
            formattedToken.toString(),
            archerMemberResponse?.id.toString()
        )
            .observe(this, Observer { response ->
                onPresenceItemResponse(response)
            })
    }


    private fun onPresenceItemResponse(response: Response<MutableList<PracticeContainer>>?) {
        viewModel.showDotsLoading(false)
        if (response != null) {
            when (response.isSuccessful) {
                true -> response.body()?.let { adapter.addItems(it) }
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun initActionBar() {
        scoring_practice_toolbar.setupActionbar(
            this,
            archerMemberResponse?.full_name.toString(),
            true
        ) {
            finish()
        }
        scoring_practice_toolbar.subtitle =
            archerMemberResponse?.user?.username
    }

    private lateinit var adapter: ScoringPracticeContainerRecyclerViewAdapter
    private fun initPresenceItemRecyclerView() {
        adapter =
            ScoringPracticeContainerRecyclerViewAdapter { pc ->

            }
        scoring_practice_recycler_view.layoutManager = LinearLayoutManager(this)
        scoring_practice_recycler_view.adapter = adapter
    }

    private fun showDotsLoading(show: Boolean) {
        if (show) scoring_practice_loading.visible()
        else scoring_practice_loading.gone()
    }

    private fun showProgressLoading(show: Boolean, msg: String = "Loading") {
        val progressDialog = ProgressDialogHelper.getInstance(this)
        progressDialog?.setMessage(msg)
        if (show) progressDialog?.show()
        else progressDialog?.dismiss()
    }
}
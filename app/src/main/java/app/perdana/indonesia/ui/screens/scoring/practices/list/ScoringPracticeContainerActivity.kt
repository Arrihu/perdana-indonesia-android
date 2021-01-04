package app.perdana.indonesia.ui.screens.scoring.practices.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
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
import app.perdana.indonesia.ui.screens.scoring.practices.add.ScoringPracticeContainerAddActivity
import app.perdana.indonesia.ui.screens.scoring.practices.series.ScoringPracticeSeriesActivity
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
            intent.getParcelableExtra(Constants.ARCHER_MEMBER_RESPONSE_OBJ)
        initActionBar()
        initPresenceItemRecyclerView()
        initActionListener()

        viewModel.dotsLoading.observe(this, Observer { state ->
            showDotsLoading(state)
        })
        viewModel.progressLoading.observe(this, Observer { state ->
            showProgressLoading(state.first, state.second)
        })

        fetchScoringContainer()
    }

    private fun fetchScoringContainer() {
        viewModel.showDotsLoading(true)
        viewModel.fetchPracticesContainer(
            formattedToken.toString(),
            archerMemberResponse?.id.toString()
        )
            .observe(this, Observer { response ->
                onPresenceItemResponse(response)
            })
    }

    private val HAS_CHANGE_REQUEST = 100
    private fun initActionListener() {
        scoring_practice_button_add_new.setOnClickListener {
            val intent = Intent(this, ScoringPracticeContainerAddActivity::class.java)
            intent.putExtras(
                bundleOf(
                    Constants.ARCHER_MEMBER_RESPONSE_OBJ to archerMemberResponse,
                    Constants.ARCHER_MEMBER_ID to archerMemberResponse?.id.toString()
                )
            )
            startActivityForResult(intent, HAS_CHANGE_REQUEST)
        }

        scoring_practice_swipe_layout.setOnRefreshListener {
            fetchScoringContainer()
        }
    }


    private fun onPresenceItemResponse(response: Response<MutableList<PracticeContainer>>?) {
        scoring_practice_swipe_layout?.isRefreshing = false

        viewModel.showDotsLoading(false)
        if (response != null) {
            when (response.isSuccessful) {
                true -> response.body()?.let {
                    adapter.clear()
                    adapter.addItems(it)
                }
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
                val intent = Intent(this, ScoringPracticeSeriesActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        Constants.PRACTICE_CONTAINER_RESPONSE_OBJ to pc,
                        Constants.ARCHER_MEMBER_RESPONSE_OBJ to archerMemberResponse,
                        Constants.ARCHER_MEMBER_ID to archerMemberResponse?.id.toString()
                    )
                )
                startActivity(intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (HAS_CHANGE_REQUEST == requestCode && resultCode == Activity.RESULT_OK) {
            fetchScoringContainer()
        }
    }
}
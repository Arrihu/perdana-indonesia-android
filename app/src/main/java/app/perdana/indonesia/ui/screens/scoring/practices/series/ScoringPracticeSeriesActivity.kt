package app.perdana.indonesia.ui.screens.scoring.practices.series

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.setupActionbar
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.ProgressDialogHelper
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.PracticeContainer
import app.perdana.indonesia.data.remote.model.PracticeContainerSeries
import app.perdana.indonesia.data.remote.model.PracticeSeries
import app.perdana.indonesia.data.remote.model.PracticeSeriesScore
import kotlinx.android.synthetic.main.scoring_practice_series_activity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.longToast
import org.jetbrains.anko.okButton
import retrofit2.Response

/**
 * Created by ebysofyan on 12/25/19.
 */
class ScoringPracticeSeriesActivity : AppCompatActivity() {

    private var practiceContainer: PracticeContainer? = null
    private var archerMemberId: String? = null
    private lateinit var viewModel: ScoringPracticeSeriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scoring_practice_series_activity)
        initializeUi()
    }

    private fun initializeUi() {
        viewModel = ViewModelProvider(this).get(ScoringPracticeSeriesViewModel::class.java)
        practiceContainer =
            intent.getParcelableExtra(Constants.PRACTICE_CONTAINER_RESPONSE_OBJ) as PracticeContainer
        archerMemberId = intent.getStringExtra(Constants.ARCHER_MEMBER_ID)

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
        viewModel.getPracticesContainer(
            formattedToken.toString(),
            practiceContainer?.id.toString(),
            archerMemberId.toString()
        )
            .observe(this, Observer { response ->
                onPresenceItemResponse(response)
            })
    }

    private var HAS_CHANGE = false
    private fun initActionListener() {

    }


    private fun onPresenceItemResponse(response: Response<PracticeContainerSeries>?) {
        viewModel.showDotsLoading(false)
        if (response != null) {
            when (response.isSuccessful) {
                true -> response.body()?.let {
                    adapter.clear()
                    adapter.addItems(it.practice_series.toMutableList())
                }
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun initActionBar() {
        practice_scoring_series_toolbar.setupActionbar(
            this,
            "Skoring, ${practiceContainer?.arrow} Arrow ${practiceContainer?.series} Rambahan pada jarak ${practiceContainer?.distance} Meter",
            true
        ) {
            if (HAS_CHANGE) setResult(Activity.RESULT_OK, Intent())
            
            finish()
        }
    }

    private lateinit var adapter: ScoringPracticeSeriesRecyclerViewAdapter
    private fun initPresenceItemRecyclerView() {
        adapter =
            ScoringPracticeSeriesRecyclerViewAdapter { ps ->
                alert {
                    title = "Kirim data skoring rambahan ke ${ps.serie}?"
                    okButton {
                        viewModel.showLoading(true to "Mengirim data skoring rambahan ke ${ps.serie}")
                        viewModel.updateSeriesScore(
                            formattedToken,
                            ps.id.toString(),
                            PracticeSeriesScore(ps.scores.toMutableList())
                        ).observe(this@ScoringPracticeSeriesActivity, Observer { response ->
                            onUpdateSeriesScoreHandler(response)
                        })
                        it.dismiss()
                    }
                    cancelButton {
                        it.dismiss()
                    }
                }.show()
            }
        practice_scoring_practice_series_recycler_view.layoutManager = LinearLayoutManager(this)
        practice_scoring_practice_series_recycler_view.adapter = adapter
    }

    private fun onUpdateSeriesScoreHandler(response: ApiResponseModel<PracticeSeries>?) {
        viewModel.hideLoading()
        when {
            response?.data != null -> {
                HAS_CHANGE = true

                longToast("Data skoring rambahan ke ${response.data.serie} berhasil di si`mpan")
                adapter.updateDataPracticeSerie(response.data)
            }
            response?.error != null -> longToast(response.error.detail)
            response?.exception != null -> longToast(response.exception.message.toString())
        }
    }

    private fun showDotsLoading(show: Boolean) {
        if (show) practice_scoring_practice_series_loading.visible()
        else practice_scoring_practice_series_loading.gone()
    }

    private fun showProgressLoading(show: Boolean, msg: String = "Loading") {
        val progressDialog = ProgressDialogHelper.getInstance(this)
        progressDialog?.setMessage(msg)
        if (show) progressDialog?.show()
        else progressDialog?.dismiss()
    }

    override fun onBackPressed() {
        if (HAS_CHANGE) {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        } else {
            super.onBackPressed()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (HAS_CHANGE_REQUEST == requestCode && resultCode == Activity.RESULT_OK) {
//            fetchScoringContainer()
//        }
//    }
}
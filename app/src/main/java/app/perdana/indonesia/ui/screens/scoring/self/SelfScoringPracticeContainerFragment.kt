package app.perdana.indonesia.ui.screens.scoring.self

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.LocalStorage
import app.perdana.indonesia.core.utils.ProgressDialogHelper
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.PracticeContainer
import app.perdana.indonesia.ui.screens.scoring.practices.add.ScoringPracticeContainerAddActivity
import app.perdana.indonesia.ui.screens.scoring.practices.series.ScoringPracticeSeriesActivity
import kotlinx.android.synthetic.main.self_scoring_practice_container_fragment.*
import org.jetbrains.anko.longToast
import retrofit2.Response

/**
 * Created by ebysofyan on 12/25/19.
 */
class SelfScoringPracticeContainerFragment : Fragment() {

    companion object {
        fun newInstance() = SelfScoringPracticeContainerFragment()

        private const val HAS_CHANGE_REQUEST = 100
    }

    private lateinit var viewModel: SelfScoringPracticeContainerViewModel
    private var archerMemberId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.self_scoring_practice_container_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUi()
    }


    private fun initializeUi() {
        viewModel = ViewModelProvider(this).get(SelfScoringPracticeContainerViewModel::class.java)
        archerMemberId = LocalStorage.getString(context!!, Constants.USER_ID).toString()

        initPresenceItemRecyclerView()
        initActionListener()

        viewModel.dotsLoading.observe(this.viewLifecycleOwner, Observer { state ->
            showDotsLoading(state)
        })
        viewModel.progressLoading.observe(this.viewLifecycleOwner, Observer { state ->
            showProgressLoading(state.first, state.second)
        })

        fetchScoringContainer()
    }

    private fun fetchScoringContainer() {
        viewModel.showDotsLoading(true)
        viewModel.fetchPracticesContainer(
            context?.formattedToken.toString(),
            archerMemberId.toString()
        )
            .observe(this.viewLifecycleOwner, Observer { response ->
                onPresenceItemResponse(response)
            })
    }

    private fun initActionListener() {
        self_scoring_practice_button_add_new.setOnClickListener {
            val intent = Intent(context, ScoringPracticeContainerAddActivity::class.java)
            intent.putExtras(
                bundleOf(
                    Constants.ARCHER_MEMBER_ID to archerMemberId
                )
            )
            startActivityForResult(intent, HAS_CHANGE_REQUEST)
        }

        self_scoring_practice_swipe_layout.setOnRefreshListener {
            fetchScoringContainer()
        }
    }


    private fun onPresenceItemResponse(response: Response<MutableList<PracticeContainer>>?) {
        self_scoring_practice_swipe_layout?.isRefreshing = false

        viewModel.showDotsLoading(false)
        if (response != null) {
            when (response.isSuccessful) {
                true -> response.body()?.let {
                    adapter.clear()
                    adapter.addItems(it)
                }
                else -> context?.longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else context?.longToast(getString(R.string.no_internet_connection))
    }


    private lateinit var adapter: SelfScoringPracticeContainerRecyclerViewAdapter
    private fun initPresenceItemRecyclerView() {
        adapter =
            SelfScoringPracticeContainerRecyclerViewAdapter { pc ->
                val intent = Intent(context, ScoringPracticeSeriesActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        Constants.PRACTICE_CONTAINER_RESPONSE_OBJ to pc,
                        Constants.ARCHER_MEMBER_ID to archerMemberId
                    )
                )
                startActivity(intent)
            }
        self_scoring_practice_recycler_view.layoutManager = LinearLayoutManager(context)
        self_scoring_practice_recycler_view.adapter = adapter
    }

    private fun showDotsLoading(show: Boolean) {
        if (show) self_scoring_practice_loading.visible()
        else self_scoring_practice_loading.gone()
    }

    private fun showProgressLoading(show: Boolean, msg: String = "Loading") {
        val progressDialog = ProgressDialogHelper.getInstance(context!!)
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
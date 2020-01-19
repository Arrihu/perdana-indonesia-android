package app.perdana.indonesia.ui.screens.scoring.members

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.onRightDrawableClicked
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.ProgressDialogHelper
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.ArcherMemberResponse
import app.perdana.indonesia.ui.screens.scoring.practices.list.ScoringPracticeContainerActivity
import kotlinx.android.synthetic.main.scoring_member_fragment.*
import org.jetbrains.anko.longToast
import retrofit2.Response
import java.util.*

/**
 * Created by ebysofyan on 12/25/19.
 */

class ScoringMemberFragment : Fragment() {

    companion object {
        fun newInstance(): ScoringMemberFragment = ScoringMemberFragment()
    }

    private lateinit var fragmentActivity: AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.scoring_member_fragment, container, false)
    }

    private val tempList = mutableListOf<ArcherMemberResponse>()
    private lateinit var viewModel: ScoringMemberViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentActivity = activity as AppCompatActivity
        viewModel = ViewModelProvider(this).get(ScoringMemberViewModel::class.java)

        initializerUi()
    }

    private fun initializerUi() {
        initPresenceItemRecyclerView()
        initActionListener()

        viewModel.dotsLoading.observe(this.viewLifecycleOwner, Observer { state ->
            showDotsLoading(state)
        })
        viewModel.progressLoading.observe(this.viewLifecycleOwner, Observer { state ->
            showProgressLoading(state.first, state.second)
        })

        fetchMembers()
    }

    private fun fetchMembers() {
        viewModel.showDotsLoading(true)
        viewModel.fetchMembers(context?.formattedToken.toString())
            .observe(this.viewLifecycleOwner, Observer { response ->
                onPresenceItemResponse(response)
            })
    }

    private fun initActionListener() {
        scoring_member_search_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    adapter.clear()
                    adapter.addItems(tempList)
                    scoring_member_search_input.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_search,
                        0
                    )
                    scoring_member_search_input.removeCallbacks(null)
                } else {
                    searchLocalData(s.toString())
                    scoring_member_search_input.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_close_light,
                        0
                    )
                    scoring_member_search_input.onRightDrawableClicked {
                        it.text.clear()
                        it.clearFocus()
                    }
                }
            }

        })

        scoring_member_swipe_layout.setOnRefreshListener {
            fetchMembers()
        }
    }

    private fun onPresenceItemResponse(response: Response<List<ArcherMemberResponse>>?) {
        scoring_member_swipe_layout?.isRefreshing = false

        viewModel.showDotsLoading(false)
        if (response != null) {
            when (response.isSuccessful) {
                true -> response.body()?.let {
                    tempList.clear()
                    tempList.addAll(it.toMutableList())
                    adapter.addItems(it.toMutableList())
                }
                else -> context?.longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else context?.longToast(getString(R.string.no_internet_connection))
    }

    private lateinit var adapter: ScoringMemberRecyclerViewAdapter
    private fun initPresenceItemRecyclerView() {
        adapter =
            ScoringMemberRecyclerViewAdapter { pi ->
                val bundle = bundleOf(Constants.ARCHER_MEMBER_ID to pi.id)
                val intent = Intent(context, ScoringPracticeContainerActivity::class.java).apply {
                    putExtras(bundle)
                }
                startActivity(intent)
            }
        scoring_member_recycler_view.layoutManager = LinearLayoutManager(context)
        scoring_member_recycler_view.adapter = adapter
    }

    private fun searchLocalData(query: String) {
        val filteredList = mutableListOf<ArcherMemberResponse>()
        tempList.forEach {
            val queryPattern = "${it.full_name}${it.user.username}"
            if (queryPattern.toLowerCase(Locale.ROOT).contains(query)) {
                filteredList.add(it)
            }
        }
        adapter.clear()
        adapter.addItems(filteredList)
    }

    private fun showDotsLoading(show: Boolean) {
        if (show) scoring_member_loading.visible()
        else scoring_member_loading.gone()
    }

    private fun showProgressLoading(show: Boolean, msg: String = "Loading") {
        val progressDialog = ProgressDialogHelper.getInstance(fragmentActivity)
        progressDialog?.setMessage(msg)
        if (show) progressDialog?.show()
        else progressDialog?.dismiss()
    }
}
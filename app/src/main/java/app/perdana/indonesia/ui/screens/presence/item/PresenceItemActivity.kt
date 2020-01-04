package app.perdana.indonesia.ui.screens.presence.item

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.*
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.ProgressDialogHelper
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.PresenceContainerResponse
import app.perdana.indonesia.data.remote.model.PresenceItem
import app.perdana.indonesia.ui.screens.presence.scanner.PresenceScannerActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import kotlinx.android.synthetic.main.presence_item_activity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.longToast
import org.jetbrains.anko.okButton
import retrofit2.Response

/**
 * Created by ebysofyan on 12/25/19.
 */
class PresenceItemActivity : AppCompatActivity() {

    companion object {
        const val SCANNER_REQUEST_CODE = 1001
    }

    private var presenceContainerResponse: PresenceContainerResponse? = null
    private lateinit var viewModel: PresenceItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.presence_item_activity)
        initializeUi()
    }

    private fun initializeUi() {
        viewModel = ViewModelProvider(this).get(PresenceItemViewModel::class.java)
        presenceContainerResponse =
            intent.getParcelableExtra(Constants.PRESENCE_CONTAINER_RESPONSE_OBJ) as PresenceContainerResponse
        initActionBar()
        initActionListener()
        initPresenceItemRecyclerView()

        viewModel.dotsLoading.observe(this, Observer { state ->
            showDotsLoading(state)
        })
        viewModel.progressLoading.observe(this, Observer { state ->
            showProgressLoading(state.first, state.second)
        })

        viewModel.showDotsLoading(true)
        viewModel.getPresencesContainer(
            formattedToken.toString(),
            presenceContainerResponse?.id.toString()
        )
            .observe(this, Observer { response ->
                onPresenceItemResponse(response)
            })
    }


    private fun initActionListener() {
        presence_item_scan.setOnClickListener {
            startActivityForResult(
                Intent(this, PresenceScannerActivity::class.java),
                SCANNER_REQUEST_CODE
            )
            Animatoo.animateSlideUp(this)
        }
    }

    private fun onPresenceItemResponse(response: Response<PresenceContainerResponse>?) {
        viewModel.showDotsLoading(false)
        if (response != null) {
            when (response.isSuccessful) {
                true -> response.body()?.presence_items?.let { adapter.addItems(it) }
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun initActionBar() {
        presence_item_toolbar.setupActionbar(
            this,
            presenceContainerResponse?.title.toString(),
            true
        ) {
            finish()
        }
        presence_item_toolbar.subtitle =
            presenceContainerResponse?.created.toString().fullDateTimeFormat()
    }

    private lateinit var adapter: PresenceItemRecyclerViewAdapter
    private fun initPresenceItemRecyclerView() {
        adapter =
            PresenceItemRecyclerViewAdapter { pi ->

            }
        presence_item_recycler_view.layoutManager = LinearLayoutManager(this)
        presence_item_recycler_view.adapter = adapter
    }

    private fun showDotsLoading(show: Boolean) {
        if (show) presence_item_loading.visible()
        else presence_item_loading.gone()
    }

    private fun showProgressLoading(show: Boolean, msg: String = "Loading") {
        val progressDialog = ProgressDialogHelper.getInstance(this)
        progressDialog?.setMessage(msg)
        if (show) progressDialog?.show()
        else progressDialog?.dismiss()
    }

    private fun changeItemStatus(memberId: String) {
        viewModel.showLoading(true to "Mengubah status kehadiran untuk peserta $memberId")
        val queryMap = hashMapOf("user" to memberId, "status" to "1")
        viewModel.changeItemStatus(
            formattedToken,
            presenceContainerResponse?.id.toString(),
            queryMap
        ).observe(this, Observer { response ->
            viewModel.hideLoading()
            onchangeItemStatusResponse(response)
        })
    }

    private fun onchangeItemStatusResponse(response: Response<PresenceItem>?) {
        if (response != null) {
            when (response.isSuccessful) {
                true -> {
                    val obj = adapter.getItems().firstOrNull { it.id == response.body()?.id }
                    obj.let { it?.status = response.body()?.status.toString() }
                    adapter.notifyDataSetChanged()

                    longToast("Status presensi untuk peserta ${response.body()?.member?.full_name} telah diubah")
                }
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun showPresenceConfirmationDialog(memberId: String) {
        alert {
            message = "Lanjutkan menetapkan peserta dengan ID $memberId hadir di lokasi latihan?"
            okButton {
                it.dismiss()
                changeItemStatus(memberId)
            }
            cancelButton {
                it.dismiss()
            }
        }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCANNER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val scannerResult = data?.getStringExtra(Constants.SCANNER_RESULT)
            showPresenceConfirmationDialog(scannerResult.toString())
        }
    }

}
package app.perdana.indonesia.ui.fragments.presence

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.ProgressDialogHelper
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.PresenceContainerResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.presence_fragment.*
import org.jetbrains.anko.longToast
import retrofit2.Response

class PresenceFragment : Fragment() {

    companion object {
        fun newInstance() = PresenceFragment()
    }

    private lateinit var viewModel: PresenceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.presence_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PresenceViewModel::class.java)
        initializeUi()
    }

    private fun initializeUi() {
        presence_button_add_new.setOnClickListener { showNewPresenceDialog() }
        initPresenceContainerRecyclerView()

        viewModel.dotsLoading.observe(this.viewLifecycleOwner, Observer { state ->
            showDotsLoading(state)
        })
        viewModel.progressLoading.observe(this.viewLifecycleOwner, Observer { state ->
            showProgressLoading(state.first, state.second)
        })

        viewModel.showDotsLoading(true)
        viewModel.fetchPresencesContainer(context?.formattedToken.toString())
            .observe(this.viewLifecycleOwner, Observer { response ->
                onPresenceContainerResponse(response)
            })
    }

    private fun onPresenceContainerResponse(response: Response<MutableList<PresenceContainerResponse>>?) {
        viewModel.showDotsLoading(false)
        if (response != null) {
            when (response.isSuccessful) {
                true -> response.body()?.let { adapter.addItems(it) }
                else -> context?.longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else context?.longToast(getString(R.string.no_internet_connection))
    }

    private lateinit var adapter: PresenceContainerRecyclerVieAdapter
    private fun initPresenceContainerRecyclerView() {
        adapter = PresenceContainerRecyclerVieAdapter { pc ->

        }
        presence_recycler_view.layoutManager = LinearLayoutManager(context)
        presence_recycler_view.adapter = adapter
    }

    private fun showDotsLoading(show: Boolean) {
        if (show) presence_loading.visible()
        else presence_loading.gone()
    }

    private fun showProgressLoading(show: Boolean, msg: String = "Loading") {
        val progressDialog = ProgressDialogHelper.getInstance(context!!)
        progressDialog?.setMessage(msg)
        if (show) progressDialog?.show()
        else progressDialog?.dismiss()
    }

    private fun showNewPresenceDialog() {
        val dialogBuilder = AlertDialog.Builder(context!!)
        val inflatedView =
            LayoutInflater.from(context).inflate(R.layout.add_presence_dialog_view, null)
        dialogBuilder.setView(inflatedView)

        val textHeader = inflatedView.findViewById<TextView>(R.id.presence_dialog_header)
        val imageClose = inflatedView.findViewById<ImageView>(R.id.presence_dialog_close)
        val textDesc =
            inflatedView.findViewById<TextInputEditText>(R.id.presence_dialog_text_description)
        val buttonSave = inflatedView.findViewById<MaterialButton>(R.id.presence_dialog_button_save)

        val dialog = dialogBuilder.create().also { it.show() }
        imageClose.setOnClickListener { dialog.dismiss() }
        buttonSave.setOnClickListener {
            if (textDesc.text.isNullOrEmpty()) {
                textDesc.error = "Masukkan keterangan presensi"
                return@setOnClickListener
            }

            viewModel.showLoading(true to "Membuat presensi baru . . .")
            viewModel.addNewPresenceContainer(
                inflatedView.context.formattedToken,
                textDesc.text.toString()
            ).observe(this.viewLifecycleOwner, Observer { response ->
                viewModel.hideLoading()
                if (response != null) {
                    when (response.isSuccessful) {
                        true -> {
                            response.body()?.let { pcr -> adapter.addItemIndexed(pcr) }
                            textDesc.text?.clear()
                            context?.longToast("Presensi baru berhasil dibuat")
                            dialog.dismiss()
                        }
                        else -> context?.longToast(response.errorBody()?.getErrorDetail().toString())
                    }
                } else context?.longToast(getString(R.string.no_internet_connection))
            })
        }
    }
}

package app.perdana.indonesia.ui.applicant.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.perdana.indonesia.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_input_register_number.*

/**
 * Created by ebysofyan on 17/01/20.
 */
class BottomSheetDialogFragmentAccept : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = BottomSheetDialogFragmentAccept()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_input_register_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_number_button_accept.setOnClickListener {
            if (register_number_input.text.toString().isEmpty()) {
                register_number_input.error = "Nomor Registrasi tidak boleh kosong"
                return@setOnClickListener
            }

            if (register_number_input.text.toString().length < 6) {
                register_number_input.error = "Masukkan Nomor registrasi yang valid (Minimal 6)"
                return@setOnClickListener
            }
            (activity as ApplicantDetailActivity).onSubmitRegisterNumber(register_number_input.text.toString())
            this.dismiss()
        }
    }
}
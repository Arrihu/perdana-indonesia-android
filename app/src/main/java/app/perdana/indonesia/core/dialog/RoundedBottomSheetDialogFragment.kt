package app.perdana.indonesia.core.dialog

import android.app.Dialog
import android.os.Bundle
import app.perdana.indonesia.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by ebysofyan on 2020-02-15.
 */
open class RoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)
}
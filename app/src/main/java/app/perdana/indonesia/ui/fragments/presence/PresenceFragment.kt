package app.perdana.indonesia.ui.fragments.presence

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.perdana.indonesia.R

class PresenceFragment : Fragment() {

    companion object {
        fun newInstance() = PresenceFragment()
    }

    private lateinit var viewModel: PresenceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PresenceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

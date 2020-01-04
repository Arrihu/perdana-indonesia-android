package app.perdana.indonesia.ui.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.ui.ItemOffsetDecoration
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.currentUserRole
import app.perdana.indonesia.data.remote.model.Menu
import app.perdana.indonesia.data.remote.model.TopScoring
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private val mainMenus = mutableListOf(
            Menu("Pendaftar", R.drawable.ic_account_circle_outline_white_48dp, "9+", ""),
            Menu("Archery Range", R.drawable.ic_map_marker_radius_white_48dp, "", ""),
            Menu("Organisasi", R.drawable.ic_account_group_outline_white_48dp, "", ""),
            Menu("Pengaturan", R.drawable.ic_settings_outline_white_48dp, "", "")
        )
        private val topScorers = mutableListOf(
            TopScoring("1", "Eby Sofyan", "200", "30m", "Puta"),
            TopScoring("2", "L. Erfandi", "200", "30m", "Puta"),
            TopScoring("3", "L. ALgifari", "200", "30m", "Puta"),
            TopScoring("4", "M. Hamdanil", "200", "30m", "Puta"),
            TopScoring("5", "Rozali Izaq", "200", "30m", "Puta"),
            TopScoring("6", "Afif Azizi", "200", "30m", "Puta"),
            TopScoring("7", "Abdul Aziz", "200", "30m", "Puta"),
            TopScoring("8", "Muhardi", "200", "30m", "Puta")
        )
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TopScoreRecyclerViewAdapter
    private lateinit var mainMenuAdapter: MainMenuRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {
        initializeTopScoresRecyclerView()
        if (context?.currentUserRole == Constants.UserRole.CLUB_SATUAN_MANAGER) {
            initializeMainMenuRecyclerView()
        }
    }

    private fun initializeTopScoresRecyclerView() {
        adapter = TopScoreRecyclerViewAdapter()
        main_fragment_score_recycler_view.layoutManager = LinearLayoutManager(context)
        main_fragment_score_recycler_view.adapter = adapter

        adapter.addItems(topScorers)
    }

    private fun initializeMainMenuRecyclerView() {
        main_fragment_menu_recycler_view.visible()

        mainMenuAdapter = MainMenuRecyclerViewAdapter()
        main_fragment_menu_recycler_view.layoutManager = GridLayoutManager(context, 4)
        main_fragment_menu_recycler_view.addItemDecoration(
            ItemOffsetDecoration(
                context!!,
                R.dimen.item_offset
            )
        )
        main_fragment_menu_recycler_view.adapter = mainMenuAdapter

        mainMenuAdapter.addItems(mainMenus)
    }
}

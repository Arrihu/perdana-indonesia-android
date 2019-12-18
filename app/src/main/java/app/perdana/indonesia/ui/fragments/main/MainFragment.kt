package app.perdana.indonesia.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.data.remote.model.TopScoring
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
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
    }

    private fun initializeTopScoresRecyclerView() {
        adapter = TopScoreRecyclerViewAdapter()
        main_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
        main_fragment_recycler_view.adapter = adapter

        adapter.addItems(topScorers)
    }
}

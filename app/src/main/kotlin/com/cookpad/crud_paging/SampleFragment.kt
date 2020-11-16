package com.cookpad.crud_paging

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class SampleFragment : Fragment(R.layout.sample_fragment) {
    private val viewModel: SampleViewModel by viewModels()
    private val sampleAdapter by lazy { SampleAdapter(viewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvItems = view.findViewById<RecyclerView>(R.id.rvItems)
        rvItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sampleAdapter.withLoadStateFooter(DefaultLoadStateAdapter())
        }

        view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
            setOnRefreshListener {
                sampleAdapter.refresh()
                isRefreshing = false
            }
        }

        view.findViewById<View>(R.id.btInsertHeaderItem).setOnClickListener {
            viewModel.onViewEvent(SampleViewEvents.InsertItemHeader)
        }

        view.findViewById<View>(R.id.btInsertFooterItem).setOnClickListener {
            viewModel.onViewEvent(SampleViewEvents.InsertItemFooter)
        }

        viewModel.pagingDataViewStates.observe(viewLifecycleOwner, Observer { pagingData ->
            sampleAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        })
    }
}

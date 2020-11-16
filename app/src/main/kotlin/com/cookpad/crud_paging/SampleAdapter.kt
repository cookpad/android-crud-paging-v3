package com.cookpad.crud_paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class SampleAdapter(private val sampleViewModel: SampleViewModel) :
    PagingDataAdapter<SampleEntity, SampleAdapter.ViewHolder>(Comparator) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sampleEntity = getItem(position) ?: return
        holder.itemView.findViewById<TextView>(R.id.tvItem).text = sampleEntity.name
        holder.itemView.findViewById<View>(R.id.ivRemove).setOnClickListener {
            sampleViewModel.onViewEvent(SampleViewEvents.Remove(sampleEntity))
        }
        holder.itemView.findViewById<View>(R.id.ivEdit).setOnClickListener {
            sampleViewModel.onViewEvent(SampleViewEvents.Edit(sampleEntity))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.page_item, parent, false)
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    object Comparator : DiffUtil.ItemCallback<SampleEntity>() {
        override fun areItemsTheSame(oldItem: SampleEntity, newItem: SampleEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SampleEntity, newItem: SampleEntity): Boolean {
            return oldItem == newItem
        }
    }
}

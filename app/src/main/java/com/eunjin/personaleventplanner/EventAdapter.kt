package com.eunjin.personaleventplanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eunjin.personaleventplanner.data.Event
import com.eunjin.personaleventplanner.databinding.ItemEventBinding

class EventAdapter(
    private var events: List<Event>,
    private val onItemClick: (Event) -> Unit,        // 수정용 클릭
    private val onItemLongClick: (Event) -> Unit    // 삭제용 롱클릭
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = events[position]
        holder.binding.tvTitle.text = currentEvent.title
        holder.binding.tvCategory.text = currentEvent.category
        holder.binding.tvLocation.text = currentEvent.location

        // 클릭 시 수정 호출
        holder.itemView.setOnClickListener { onItemClick(currentEvent) }

        // 길게 누를 시 삭제 호출
        holder.itemView.setOnLongClickListener {
            onItemLongClick(currentEvent)
            true
        }
    }

    override fun getItemCount() = events.size

    fun submitList(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}
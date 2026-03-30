package com.eunjin.personaleventplanner

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eunjin.personaleventplanner.databinding.FragmentEventListBinding

class EventListFragment : Fragment(R.layout.fragment_event_list) {

    private val viewModel: EventViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentEventListBinding.bind(view)

        // 어댑터 설정
        val adapter = EventAdapter(
            events = emptyList(),
            onItemClick = { event ->
                // [수정 모드 진입] 클릭한 이벤트의 ID를 번들에 담아 전달
                val bundle = Bundle().apply {
                    putInt("eventId", event.id)
                }
                findNavController().navigate(R.id.addEventFragment, bundle)
            },
            onItemLongClick = { event ->
                // [삭제 기능] 길게 누르면 삭제 확인창 띄우기
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete '${event.title}'?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.delete(event)
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // DB 데이터 관찰하여 목록 갱신
        viewModel.allEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }
    }
}
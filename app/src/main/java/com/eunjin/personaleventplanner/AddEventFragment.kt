package com.eunjin.personaleventplanner

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eunjin.personaleventplanner.data.Event
import com.eunjin.personaleventplanner.databinding.FragmentAddEventBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEventFragment : Fragment(R.layout.fragment_add_event) {

    private val viewModel: EventViewModel by viewModels()
    private var eventId: Int = -1
    private var selectedTimestamp: Long = 0L // 선택된 날짜 저장용

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddEventBinding.bind(view)

        // 1. 수정 모드 확인
        arguments?.let {
            val receivedId = it.getInt("eventId", -1)
            if (receivedId != -1) {
                eventId = receivedId
                viewModel.getEventById(eventId).observe(viewLifecycleOwner) { event ->
                    event?.let { item ->
                        binding.etTitle.setText(item.title)
                        binding.etCategory.setText(item.category)
                        binding.etLocation.setText(item.location)
                        selectedTimestamp = item.dateTime
                        updateDateText(binding)
                        binding.btnSave.text = "Update Event"
                    }
                }
            }
        }

        // 2. 날짜 선택 버튼 클릭 (DatePicker)
        binding.btnDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day, 0, 0, 0)
                selectedCalendar.set(Calendar.MILLISECOND, 0)

                val today = Calendar.getInstance()
                today.set(Calendar.HOUR_OF_DAY, 0)
                today.set(Calendar.MINUTE, 0)
                today.set(Calendar.SECOND, 0)
                today.set(Calendar.MILLISECOND, 0)

                // [과제 필수 로직] 과거 날짜 선택 방지
                if (selectedCalendar.before(today)) {
                    Toast.makeText(context, "Cannot select a past date!", Toast.LENGTH_SHORT).show()
                } else {
                    selectedTimestamp = selectedCalendar.timeInMillis
                    updateDateText(binding)
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        // 3. 저장/수정 버튼 클릭
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val category = binding.etCategory.text.toString()
            val location = binding.etLocation.text.toString()

            // [과제 필수 로직] 제목과 날짜가 비어있는지 확인
            if (title.isEmpty()) {
                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedTimestamp == 0L) {
                Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val event = Event(
                id = if (eventId == -1) 0 else eventId, // 0이면 Room에서 자동 생성
                title = title,
                category = category,
                location = location,
                dateTime = selectedTimestamp
            )

            if (eventId == -1) {
                viewModel.insert(event)
                Toast.makeText(context, "Event Saved!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.update(event)
                Toast.makeText(context, "Event Updated!", Toast.LENGTH_SHORT).show()
            }
            findNavController().navigateUp()
        }
    }

    private fun updateDateText(binding: FragmentAddEventBinding) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvSelectedDate.text = "Selected Date: ${sdf.format(Date(selectedTimestamp))}"
    }
}
package com.attech.sms.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.adapters.AttendanceAdapter
import com.attech.sms.databinding.FragmentAttendanceBinding
import com.attech.sms.models.AttendanceData
import java.util.*


class AttendanceFragment : Fragment() {
    private var binding: FragmentAttendanceBinding? = null
    private lateinit var recyclerView: RecyclerView
    private var selectedDate: String = ""
    private var title: String = ""

    companion object {
        fun newInstance(title: String): AttendanceFragment {
            val fragment = AttendanceFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        recyclerView = binding!!.recyclerViewAttendance

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = arguments?.getString("title") ?: ""
        setRecyclerView()
        buttonClicks()

    }

    private fun buttonClicks() {
        binding!!.datePickerButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding!!.selectedDate.text = selectedDate
                filterRecyclerView(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun filterRecyclerView(selectedDate: String) {
        val filteredData = getFilteredData(selectedDate)
        val adapter = recyclerView.adapter as AttendanceAdapter
        adapter.filterByDate(selectedDate)

        if (filteredData.isEmpty()) {
            Toast.makeText(requireContext(), "No records found for $selectedDate", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFilteredData(selectedDate: String): List<AttendanceData> {
        return getDummyAttendanceData().filter { it.date == selectedDate }
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AttendanceAdapter(getDummyAttendanceData())
    }


    private fun getDummyAttendanceData(): List<AttendanceData> {
        val dummyData = mutableListOf<AttendanceData>()
        for (i in 1..30) {
            if (i%2==0)
                dummyData.add(AttendanceData("2023-9-$i", "Present"))
            else
                dummyData.add(AttendanceData("2023-9-$i", "Absent"))
        }
        return dummyData
    }
}
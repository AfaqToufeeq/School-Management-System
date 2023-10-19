package com.attech.sms.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.sms.R
import com.attech.sms.adapters.AttendanceAdapter
import com.attech.sms.adapters.CourseDetailsAdapter
import com.attech.sms.callbacks.OnItemClick
import com.attech.sms.databinding.FragmentAttendanceBinding
import com.attech.sms.models.AttendanceData
import com.attech.sms.models.CourseDetails
import com.attech.sms.utils.MAIN_MENU
import java.util.*


class AttendanceFragment : Fragment(), OnItemClick {
    private var binding: FragmentAttendanceBinding? = null
    private var selectedDate: String = ""
    private var title: String = ""

    companion object {
        fun newInstance(title: String): AttendanceFragment {
            val fragment = AttendanceFragment()
            val args = Bundle()
            args.putString(MAIN_MENU, title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(MAIN_MENU) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        fetchData()
        buttonClicks()

    }

    private fun init() {
        binding!!.toolbar.smsText.text = title
        binding!!.recyclerViewAttendance.apply {
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun setCourseAdapter(courseDetailsAdapter: CourseDetailsAdapter) {
        binding!!.recyclerViewAttendance.adapter = courseDetailsAdapter
    }

    private fun fetchData() {
        when (title) {
            "Attendance" -> {
                binding!!.parentLL.visibility = View.VISIBLE
                setAttendanceAdapter(AttendanceAdapter(getAttendanceData()))
            }
            "Courses" -> {
                binding!!.parentLL.visibility = View.GONE
                setCourseAdapter(CourseDetailsAdapter(getCourseDetailsList(), false,this))
            }
            "Marks" -> {
                binding!!.parentLL.visibility = View.GONE
                setCourseAdapter(CourseDetailsAdapter(getCourseDetailsList(), true,this))
            }
            "Fee Status" -> {
                binding!!.parentLL.visibility = View.GONE
                val studentName = "Jerry Axe"
                val feeStatus = "Paid"

                val dialog = FeeStatusDialogFragment.newInstance(studentName, feeStatus)
                dialog.show(requireFragmentManager(), "fee_status_dialog")

            }
        }
    }

    private fun buttonClicks() {
        binding!!.datePickerButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

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
        val adapter = binding!!.recyclerViewAttendance.adapter as AttendanceAdapter
        adapter.filterByDate(selectedDate)

        if (filteredData.isEmpty()) {
            Toast.makeText(requireContext(), "No records found for $selectedDate", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFilteredData(selectedDate: String): List<AttendanceData> {
        return getAttendanceData().filter { it.date == selectedDate }
    }

    private fun setAttendanceAdapter(attendanceData: AttendanceAdapter) {
        binding!!.recyclerViewAttendance.adapter = attendanceData
    }

    private fun getCourseDetailsList(): List<CourseDetails> {
        val courseDetailsList = mutableListOf<CourseDetails>()
        courseDetailsList.add(CourseDetails("001", "Mathematics", "John Doe", "Class A"))
        courseDetailsList.add(CourseDetails("002", "Science", "Jane Smith", "Class A"))
        courseDetailsList.add(CourseDetails("003", "English", "Alex Bhatti", "Class A"))
        return courseDetailsList
    }


    private fun getAttendanceData(): List<AttendanceData> {
        val dummyData = mutableListOf<AttendanceData>()
        for (i in 1..30) {
            if (i%2==0)
                dummyData.add(AttendanceData("2023-9-$i", "Present"))
            else
                dummyData.add(AttendanceData("2023-9-$i", "Absent"))
        }
        return dummyData
    }

    override fun clickListener(position: Int, value: String) {
        openFragment(value)
    }

    private fun openFragment(title: String) {
        val fragment = TestMarksFragment.newInstance(title)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
package com.attech.sms.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.sms.adapters.TestMarksAdapter
import com.attech.sms.databinding.FragmentTestMarksBinding
import com.attech.sms.models.TestMark
import com.attech.sms.utils.MAIN_MENU

class TestMarksFragment : Fragment() {

    private var _binding: FragmentTestMarksBinding? = null
    private val binding get() = _binding!!

    private var title: String = ""

    companion object {
        fun newInstance(title: String): TestMarksFragment {
            val fragment = TestMarksFragment()
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
        _binding = FragmentTestMarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.smsText.text = title
        val testMarks = createSampleTestMarks()
        val adapter = TestMarksAdapter(testMarks)
        binding.recyclerViewTestMarks.adapter = adapter
        binding.recyclerViewTestMarks.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createSampleTestMarks(): List<TestMark> {
        return listOf(
            TestMark("Math", "Midterm Exam", "Mr. Smith", 100, 85.0, "2023-10-15"),
            TestMark("Science", "Quiz", "Ms. Johnson", 20, 18.5, "2023-10-16")
        )
    }
}

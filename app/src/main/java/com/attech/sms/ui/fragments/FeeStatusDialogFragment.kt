package com.attech.sms.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class FeeStatusDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(studentName: String, feeStatus: String): FeeStatusDialogFragment {
            val fragment = FeeStatusDialogFragment()
            val args = Bundle()
            args.putString("studentName", studentName)
            args.putString("feeStatus", feeStatus)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val studentName = arguments?.getString("studentName") ?: ""
        val feeStatus = arguments?.getString("feeStatus") ?: ""

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Student Fee Status")
        builder.setMessage("Student Name: $studentName\nFee Status: $feeStatus")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        return builder.create()
    }
}

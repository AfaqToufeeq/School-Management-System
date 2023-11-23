package com.attech.teacher.utils

import android.app.ProgressDialog
import android.content.Context

class LoadingDialog(private val context: Context) {

    private var progressDialog: ProgressDialog? = null

    fun showLoadingDialog(message: String) {
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    fun dismissLoadingDialog() {
        progressDialog?.dismiss()
    }
}

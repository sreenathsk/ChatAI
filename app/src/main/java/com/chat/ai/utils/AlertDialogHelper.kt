package com.chat.ai.utils

import android.app.AlertDialog
import android.content.Context

class AlertDialogHelper(private val context: Context) {

    fun showAlertDialog(title: String, description: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(description)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}

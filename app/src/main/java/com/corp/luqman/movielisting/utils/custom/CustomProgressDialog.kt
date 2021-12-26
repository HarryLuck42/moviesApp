package com.corp.luqman.movielisting.utils.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.corp.luqman.movielisting.R

class CustomProgressDialog (val context: Context, title: String){
    private var dialog: Dialog = Dialog(context)

    init {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setContentView(R.layout.progress_dialog)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        val tv_dialog = dialog.findViewById<TextView>(R.id.tv_dialog)
        tv_dialog.setText(title)

    }

    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }



}
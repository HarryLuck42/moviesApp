package com.corp.luqman.movielisting.utils

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.corp.luqman.movielisting.R
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.dialog_general_ok.view.*

object Helpers {

    fun getDefaultMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    fun customViewDialog(context: Context, layout: Int, cancelAble: Boolean): MaterialDialog {
        val mDialog = MaterialDialog(context)
            .customView(layout, scrollable = false,
                noVerticalPadding = true,
                dialogWrapContent = true,
                horizontalPadding = false)
            .cornerRadius(8f)
            .cancelable(cancelAble)

        return mDialog

    }

    fun showGeneralOkDialog(mContext: Context, mTitle: String = "", mDesc: String = ""): MaterialDialog {
        val mDialog = MaterialDialog(mContext).customView(
            R.layout.dialog_general_ok,
            scrollable = false,
            noVerticalPadding = true,
            dialogWrapContent = true,
            horizontalPadding = false
        ).cornerRadius(8f)

        val customView = mDialog.getCustomView()

        customView.tv_dialog_title.text = mTitle.trim()
        customView.tv_dialog_desc.text = mDesc.trim()
        customView.btn_dialog_submit.text = mContext.getText(R.string.ok)
        customView.btn_dialog_submit.setOnClickListener{
            mDialog.dismiss()
        }

        return mDialog.show{
            cancelable(true)
        }
    }

    fun getStringMaxLenght(value: String, maxWord: Int): String{
        val delimiter = " ";
        val word = value.split(delimiter)
        var result = ""
        if(word.size > maxWord){
            for(i in word.indices){
                if(i < maxWord - 1){
                    result += word.get(i) + delimiter
                }else if(i == maxWord -1){
                    result += word.get(i)
                }
            }
        }else{
            result = value
        }

        return result
    }

}
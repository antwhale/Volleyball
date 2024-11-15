package org.techtown.volleyball.base.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.util.*

object DialogExt {

    fun AlertDialog.Builder.from(
            message: String,
            positive: String = "ok",
            positiveListener: DialogInterface.OnClickListener? = null,
            negative: String = "cancel",
            negativeListener: DialogInterface.OnClickListener? = null
    ): AlertDialog.Builder {
        setCancelable(false)
        setMessage(message)
        setPositiveButton(positive, positiveListener)
        setNegativeButton(negative, negativeListener)
        return this
    }

    fun Activity.showAlert(
            message: String,
            positive: String = "ok",
            isCancelable: Boolean = false,
            positiveListener: DialogInterface.OnClickListener? = null,
    ) {
        AlertDialog.Builder(this).apply {
            setCancelable(isCancelable)
            setMessage(message)
            setPositiveButton(positive, positiveListener)
        }.show()
    }

    fun Fragment.showAlert(
            message: String,
            positive: String = "ok",
            positiveListener: DialogInterface.OnClickListener? = null,
    ) {
        AlertDialog.Builder(requireActivity()).apply {
            setCancelable(false)
            setMessage(message)
            setPositiveButton(positive, positiveListener)
        }.show()
    }

    fun Activity.showConfirm(
            message: String,
            positive: String = "ok",
            positiveListener: DialogInterface.OnClickListener? = null,
            negative: String = "cancel",
            negativeListener: DialogInterface.OnClickListener? = null
    ) {
        AlertDialog.Builder(this).apply {
            setCancelable(false)
            setMessage(message)
            setPositiveButton(positive, positiveListener)
            setNegativeButton(negative, negativeListener)
        }.show()
    }

    fun Fragment.showConfirm(
            message: String,
            positive: String = "ok",
            positiveListener: DialogInterface.OnClickListener? = null,
            negative: String = "cancel",
            negativeListener: DialogInterface.OnClickListener? = null
    ) {
        AlertDialog.Builder(requireContext()).apply {
            setCancelable(false)
            setMessage(message)
            setPositiveButton(positive, positiveListener)
            setNegativeButton(negative, negativeListener)
        }.show()
    }

    fun Context.showAlert(
            message: String,
            positive: String = "ok",
            positiveListener: DialogInterface.OnClickListener? = null,
    ) {
        AlertDialog.Builder(this).apply {
            setCancelable(false)
            setMessage(message)
            setPositiveButton(positive, positiveListener)
        }.show()
    }

    //네트워크 알람은 자동으로 사라지게
    fun Activity.showSecAlert(
        message: String,
        positive: String = "ok",
        isCancelable: Boolean = false,
        positiveListener: DialogInterface.OnClickListener? = null,
    ) {
        val dialog = AlertDialog.Builder(this).create()

        dialog.apply {
            setCancelable(isCancelable)
            setMessage(message)
            setButton(DialogInterface.BUTTON_POSITIVE, positive, positiveListener)
        }.show()

        val task = object: TimerTask(){
            override fun run() {
                dialog.dismiss()
            }
        }

        val timer = Timer()
        timer.schedule(task, 1000)
    }
//

    fun Context.showConfirm(
            message: String,
            positive: String = "ok",
            positiveListener: DialogInterface.OnClickListener? = null,
            negative: String = "cancel",
            negativeListener: DialogInterface.OnClickListener? = null
    ) {
        AlertDialog.Builder(this).apply {
            setCancelable(false)
            setMessage(message)
            setPositiveButton(positive, positiveListener)
            setNegativeButton(negative, negativeListener)
        }.show()
    }
}
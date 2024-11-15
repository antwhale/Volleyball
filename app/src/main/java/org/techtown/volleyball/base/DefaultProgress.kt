package org.techtown.volleyball.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import org.techtown.volleyball.R

class DefaultProgress(
    context: Context
) : Dialog(context, R.style.BaseProgressTheme){
    private val TAG = "DefaultProgress"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.default_progress)
        setCancelable(false)
    }

    fun show(isShow: Boolean) {
        if(isShow){
            show()
        } else {
            dismiss()
        }
    }
}
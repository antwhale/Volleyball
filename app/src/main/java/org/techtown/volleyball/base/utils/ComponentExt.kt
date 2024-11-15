package org.techtown.volleyball.base.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ComponentExt {

    fun Context.dip2px(value: Int): Float = (value * resources.displayMetrics.density)

    fun Context.createCaptureUri(): Uri? {
        val imageName = SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.getDefault()).format(Date())
        val extension = ".jpg"
        val fileName = imageName + extension
        val values = ContentValues()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
        } else {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName)

            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            values.put("_data", file.absolutePath)
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    fun Activity.startWith(cls: Class<*>, bundle: Bundle? = null) {
        Intent(this, cls)
                .apply { bundle?.let { putExtras(it) } }
                .also {
                    startActivity(it)
                }
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
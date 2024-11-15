package org.techtown.volleyball.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.view.View
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

object CommonExt {

    @SuppressLint("HardwareIds")
    fun getAndroidID(context: Context): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    fun getQueryParameter(uri: Uri, name: String, withDecode: Boolean = false): String {
        return uri.runCatching {
            getQueryParameter(name)?.let {
                if (withDecode) URLDecoder.decode(it, "UTF-8") else it
            }
        }.getOrDefault("") ?: ""
    }

    fun isToday(dateString: String): Boolean {
        val target = SimpleDateFormat("yyyyMMdd", Locale.KOREA).runCatching {
            parse(dateString)?.let { date ->
                Calendar.getInstance().apply {
                    time = date
                }
            }
        }.getOrNull()

        return target?.let {
            val targetYear = target.get(Calendar.YEAR)
            val targetDay = target.get(Calendar.DAY_OF_YEAR)

            val now = Calendar.getInstance()
            val nowYear = now.get(Calendar.YEAR)
            val nowDay = now.get(Calendar.DAY_OF_YEAR)

            nowYear == targetYear && nowDay == targetDay
        } ?: false
    }

//    fun <T> T.postEvent() {
//        EventBus.getDefault().post(this)
//    }

    fun Boolean?.toInt(): Int =
            if (this == true) 1
            else 0
}

class OneClickListener(private val oneClick: (View) -> Unit) : View.OnClickListener {
    companion object {
        const val CLICK_INTERVAL = 500
    }

    private var lastClickedTime: Long = 0L

    private fun isSafe() : Boolean {
        return System.currentTimeMillis() - lastClickedTime > CLICK_INTERVAL
    }

    override fun onClick(view: View?) {
        if(isSafe() && view != null) {
            oneClick(view)
        }
        lastClickedTime = System.currentTimeMillis()
    }
}

fun View.setOneClickListener(oneClick: (view: View) -> Unit) {

    setOnClickListener(OneClickListener {
        oneClick(it)
    })
}
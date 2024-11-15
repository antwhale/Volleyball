package org.techtown.volleyball.base.utils

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

object IntentExt {
    fun createCaptureIntent(acceptTypes: Array<out String>?, block: (String) -> Uri?): Intent? {
        val mimeType = acceptTypes?.firstOrNull { it.startsWith("image") || it.startsWith("video") }
            ?: "image/*"

        val action = when {
            mimeType.startsWith("image") -> MediaStore.ACTION_IMAGE_CAPTURE
            mimeType.startsWith("video") -> MediaStore.ACTION_VIDEO_CAPTURE
            else -> MediaStore.ACTION_IMAGE_CAPTURE
        }

        val uri = block(mimeType) ?: return null

        return Intent(action).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
    }

    fun createShareIntent(data: String): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, data)
        }
    }
}
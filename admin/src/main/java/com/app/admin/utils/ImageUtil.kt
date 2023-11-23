package com.app.admin.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream


object ImageUtil {
    private const val PICK_IMAGE_REQUEST = 1

    fun selectImageFromGallery(fragment: Fragment) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        fragment.startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    fun handleImageSelectionResult(requestCode: Int, resultCode: Int, data: Intent?, callBack: (Uri?)->Unit) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            callBack(selectedImageUri)
        }
    }

    fun convertUriToBase64(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val bufferedInputStream = BufferedInputStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()

        val chunkSize = 1024 * 1024 // 1 MB chunks
        val buffer = ByteArray(chunkSize)
        var bytesRead: Int
        while (bufferedInputStream.read(buffer).also { bytesRead = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }

        bufferedInputStream.close()
        inputStream.close()
        byteArrayOutputStream.close()

        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val byteArray = baos.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        val bytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }


}

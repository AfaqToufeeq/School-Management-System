import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment


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
}

package SifaaServices

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

class SifaaStorageService {
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val shopItems = "flowerShopItems"
    fun getItemUrl(itemId: String) : String {
        val imgPath = "$shopItems/$itemId"
        val encoded = Uri.encode(imgPath)
        return "https://firebasestorage.googleapis.com/v0/b/${firebaseStorage.reference.bucket}/o/$encoded?alt=media"
    }
}
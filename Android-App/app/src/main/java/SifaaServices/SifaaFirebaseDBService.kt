package SifaaServices

import com.google.firebase.database.*
import SifaaDataModels.SifaaFloralItem
import interfaces.FloralApi
import interfaces.RequestType

class SifaaFirebaseDBService {
    private var databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val flowerShop = "flower_shop"

    fun readAllMenu(floralApi: FloralApi, requestType: RequestType) {
        val sifaaList = ArrayList<SifaaFloralItem>()

        val menuDbRef = databaseRef.child(flowerShop)
        menuDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val item = SifaaFloralItem(
                        itemID = snap.child("item_id").value.toString(),
                        imageUrl = snap.child("item_image_url").value.toString(),
                        itemName = snap.child("item_name").value.toString(),
                        itemPrice = snap.child("item_price").value.toString().toFloat(),
                        itemShortDesc = snap.child("item_desc").value.toString(),
                        itemTag = snap.child("item_category").value.toString(),
                        itemStars = snap.child("stars").value.toString().toInt()
                    )
                    sifaaList.add(item)
                }
                sifaaList.shuffle() //so that every time user can see different items on opening app
                floralApi.onFetchSuccessListener(sifaaList, requestType)
            }

            override fun onCancelled(error: DatabaseError) {
                // HANDLE ERROR
            }
        })
    }

    fun insertMenuItem(itemSifaa: SifaaFloralItem) {
        val menuRef = databaseRef.child(flowerShop)

        menuRef.setValue(itemSifaa)
    }
}
package com.pro.cafy_theofficecafeteria

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import SifaaDataModels.SifaaFloralItem
import interfaces.FloralApi
import interfaces.RequestType
import SifaaServices.DatabaseHandler
import SifaaServices.SifaaFirebaseDBService

class SifaaSettingsActivity : AppCompatActivity(), FloralApi {

    private lateinit var itemLoadLinearLayout: LinearLayout
    private lateinit var itemImageLoadTextView: TextView

    private lateinit var shopModeLinearLayout: LinearLayout
    private lateinit var shopModeTextView: TextView

    private lateinit var updateShopLinearLayout: LinearLayout
    private lateinit var deleteShopLinearLayout: LinearLayout

    private lateinit var deleteSavedCardsLinearLayout: LinearLayout
    private lateinit var deleteOrdersHistoryLinearLayout: LinearLayout

    private lateinit var sharedPref: SharedPreferences

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sifaa_activity_settings)

        itemLoadLinearLayout = findViewById(R.id.settings_load_item_images_ll)
        itemImageLoadTextView = findViewById(R.id.settings_load_item_images_tv)
        itemLoadLinearLayout.setOnClickListener { updateLoadItemImage() }

        shopModeLinearLayout = findViewById(R.id.settings_shop_mode_ll)
        shopModeTextView = findViewById(R.id.settings_shop_mode_tv)
        shopModeLinearLayout.setOnClickListener { updateMenuMode() }

        updateShopLinearLayout = findViewById(R.id.settings_update_shop_ll)
        updateShopLinearLayout.setOnClickListener { updateMenuForOffline() }

        deleteShopLinearLayout = findViewById(R.id.settings_delete_menu_ll)
        deleteShopLinearLayout.setOnClickListener { deleteOfflineMenu() }

        deleteSavedCardsLinearLayout = findViewById(R.id.settings_saved_cards_ll)
        deleteSavedCardsLinearLayout.setOnClickListener { deleteAllTheSavedCards() }

        deleteOrdersHistoryLinearLayout = findViewById(R.id.settings_delete_order_history_ll)
        deleteOrdersHistoryLinearLayout.setOnClickListener { deleteAllTheOrdersHistoryDetails() }

        sharedPref = getSharedPreferences("settings", MODE_PRIVATE)

        loadUserSettings()
        findViewById<ImageView>(R.id.settings_go_back_iv).setOnClickListener { onBackPressed() }
    }

    private fun deleteAllTheOrdersHistoryDetails() {
        val db = DatabaseHandler(this)
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete all the previous order details?")
            .setPositiveButton("Yes, Delete All", DialogInterface.OnClickListener {dialogInterface, _ ->
                db.dropOrderHistoryTable()
                db.close()
                dialogInterface.dismiss()
            })
            .create().show()
    }

    private fun deleteAllTheSavedCards() {
        val db = DatabaseHandler(this)
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete all the saved cards?")
            .setPositiveButton("Yes, Delete All", DialogInterface.OnClickListener {dialogInterface, _ ->
                db.clearSavedCards()
                db.close()
                dialogInterface.dismiss()
            })
            .create().show()
    }

    private fun updateLoadItemImage() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Load Item Images")

        val options = arrayOf("On", "Off")
        var checkedItem = sharedPref.getInt("loadItemImages", 0)
        dialog.setSingleChoiceItems(options, checkedItem, DialogInterface.OnClickListener {_, i ->
            checkedItem = i
        })
        dialog.setPositiveButton("Save", DialogInterface.OnClickListener {dialogInterface, _ ->
            when(checkedItem) {
                0 -> itemImageLoadTextView.text = "On"
                1 -> itemImageLoadTextView.text = "Off"
            }
            val editor = sharedPref.edit()
            editor.putInt("loadItemImages", checkedItem)
            editor.apply()
            dialogInterface.dismiss()
        })
        dialog.setCancelable(false)
        dialog.create()
        dialog.show()
    }

    private fun updateMenuMode() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Menu Mode")

        val options = arrayOf("Online", "Offline")
        var checkedItem = sharedPref.getInt("menuMode", 0)
        dialog.setSingleChoiceItems(options, checkedItem, DialogInterface.OnClickListener {_, i ->
            checkedItem = i
        })
        dialog.setPositiveButton("Save", DialogInterface.OnClickListener {dialogInterface, _ ->
            when(checkedItem) {
                0 -> shopModeTextView.text = "Online"
                1 -> shopModeTextView.text = "Offline"
            }
            val editor = sharedPref.edit()
            editor.putInt("menuMode", checkedItem)
            editor.apply()
            dialogInterface.dismiss()
        })
        dialog.setCancelable(false)
        dialog.create()
        dialog.show()
    }

    private fun updateMenuForOffline() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating...")
        progressDialog.setMessage("Offline Menu is preparing for you...")
        progressDialog.show()

        SifaaFirebaseDBService().readAllMenu(this, RequestType.OFFLINE_UPDATE)
    }

    override fun onFetchSuccessListener(list: ArrayList<SifaaFloralItem>, requestType: RequestType) {
        val db = DatabaseHandler(this)
        db.clearTheOfflineShopTable()

        if (requestType == RequestType.OFFLINE_UPDATE) {
            for (item in list) {
                db.insertOfflineShopData(item)
            }
            Toast.makeText(applicationContext, "Updated Offline Menu", Toast.LENGTH_SHORT).show()
        }

        progressDialog.dismiss()
    }

    private fun deleteOfflineMenu() {
        val db = DatabaseHandler(this)
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete the offline shop?")
            .setPositiveButton("Yes, Delete it", DialogInterface.OnClickListener {dialogInterface, _ ->
                db.clearTheOfflineShopTable()
                db.close()
                Toast.makeText(this, "Offline shop has been removed", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            })
            .create().show()
    }

    private fun loadUserSettings() {
        when(sharedPref.getInt("loadItemImages", 0)) {
            0 -> itemImageLoadTextView.text = "On"
            1 -> itemImageLoadTextView.text = "Off"
        }
        when(sharedPref.getInt("menuMode", 0)) {
            0 -> shopModeTextView.text = "Online"
            1 -> shopModeTextView.text = "Offline"
        }

        findViewById<TextView>(R.id.tvAppVersion).text = BuildConfig.VERSION_NAME
    }

}
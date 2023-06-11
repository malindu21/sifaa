package com.pro.cafy_theofficecafeteria

import SifaaAdapters.SifaaCurrentOrderAdapter
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import SifaaDataModels.SifaaCurrentOrderItem
import SifaaServices.DatabaseHandler

class SifaaMyCurrentOrdersActivity : AppCompatActivity(), SifaaCurrentOrderAdapter.OnItemClickListener {

    private val currentOrderList = ArrayList<SifaaCurrentOrderItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var sifaaAdapter: SifaaCurrentOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sifaa_activity_current_orders)

        recyclerView = findViewById(R.id.current_order_recycler_view)
        sifaaAdapter = SifaaCurrentOrderAdapter(this, currentOrderList, this)
        recyclerView.adapter = sifaaAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadCurrentOrdersFromDatabase()
    }

    private fun loadCurrentOrdersFromDatabase() {

        val db = DatabaseHandler(this)
        val data = db.readCurrentOrdersData()

        if(data.isEmpty()) {
            return
        }

        findViewById<LinearLayout>(R.id.current_order_empty_indicator_ll).visibility = ViewGroup.GONE
        for(i in 0 until data.size) {
            val currentOrderItem =  SifaaCurrentOrderItem()

            currentOrderItem.orderID = data[i].orderID
            currentOrderItem.takeAwayTime = data[i].takeAwayTime
            currentOrderItem.paymentStatus = data[i].paymentStatus
            currentOrderItem.orderItemNames = data[i].orderItemNames
            currentOrderItem.orderItemQuantities = data[i].orderItemQuantities
            currentOrderItem.totalItemPrice = data[i].totalItemPrice
            currentOrderItem.tax = data[i].tax
            currentOrderItem.subTotal = data[i].subTotal
            currentOrderList.add(currentOrderItem)
            currentOrderList.reverse()
            sifaaAdapter.notifyItemRangeInserted(0, data.size)
        }
    }

    override fun showQRCode(orderID: String) {
        //User have to just show the QR Code, and floral shop staff have to scan, so user don't have to wait more
        val bundle = Bundle()
        bundle.putString("orderID", orderID)

        val dialog = SifaaQRCodeFragment()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "QR Code Generator")
    }

    override fun cancelOrder(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Order Cancellation")
            .setMessage("Are you sure you want to cancel this order?")
            .setPositiveButton("Yes, Cancel Order", DialogInterface.OnClickListener {dialogInterface, _ ->
                val result = DatabaseHandler(this).deleteCurrentOrderRecord(currentOrderList[position].orderID)
                currentOrderList.removeAt(position)
                sifaaAdapter.notifyItemRemoved(position)
                sifaaAdapter.notifyItemRangeChanged(position, currentOrderList.size)
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()

                if(currentOrderList.isEmpty()) {
                    findViewById<LinearLayout>(R.id.current_order_empty_indicator_ll).visibility = ViewGroup.VISIBLE
                }

                dialogInterface.dismiss()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener {dialogInterface, _ ->
                dialogInterface.dismiss()
            })
            .create().show()
    }

    fun goBack(view: View) {onBackPressed()}
}
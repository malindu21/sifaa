package SifaaServices

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import SifaaDataModels.*

const val DATABASE_NAME = "FlowerShopDB" //Offline Database

const val COL_ID = "id"

const val OFFLINE_FLOWER_SHOP_TABLE_NAME = "offline_flower_shop"
const val COL_ITEM_ID = "item_id"
const val COL_ITEM_NAME = "item_name"
const val COL_ITEM_PRICE = "item_price"
const val COL_ITEM_DESC = "item_desc"
const val COL_ITEM_STAR = "item_star"
const val COL_ITEM_CATEGORY = "item_category"
const val COL_ITEM_IMAGE_URL = "item_image_url"

const val CART_TABLE_NAME = "current_cart"
const val CART_ITEM_ID = "item_id"
const val CART_IMAGE_URL = "image_url"
const val CART_ITEM_NAME = "item_name"
const val CART_ITEM_PRICE = "item_price"
const val CART_ITEM_SHORT_DESC = "item_short_desc"
const val CART_ITEM_STARS = "item_stars"
const val CART_ITEM_QTY = "item_qty"

const val ORDER_HISTORY_TABLE_NAME = "old_orders"
const val COL_ORDER_DATE = "order_date"
const val COL_ORDER_ID = "order_id"
const val COL_ORDER_STATUS = "order_status"
const val COL_ORDER_PAYMENT = "order_payment"
const val COL_ORDER_PRICE = "order_price"

const val CURRENT_ORDER_TABLE_NAME = "current_orders"
const val COL_CURRENT_ORDER_ID = "order_id"
const val COL_CURRENT_ORDER_TAKE_AWAY_TIME = "take_away_time"
const val COL_CURRENT_ORDER_PAYMENT_STATUS = "payment_status"
const val COL_CURRENT_ORDER_ITEM_NAMES = "item_names"
const val COL_CURRENT_ORDER_ITEM_QUANTITIES = "item_quantities"
const val COL_CURRENT_ORDER_TOTAL_ITEM_PRICE = "total_item_price"
const val COL_CURRENT_ORDER_TAX = "tax"
const val COL_CURRENT_ORDER_SUB_TOTAL = "sub_total"

const val SAVED_CARDS_TABLE_NAME = "saved_cards"
const val COL_SAVED_CARD_NUMBER = "card_number"
const val COL_SAVED_CARD_HOLDER_NAME = "card_holder_name"
const val COL_SAVED_CARD_EXPIRY_DATE = "expiry_date"

class DatabaseHandler(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createOfflineShopTable = "CREATE TABLE $OFFLINE_FLOWER_SHOP_TABLE_NAME (" +
                "$COL_ITEM_ID VARCHAR(256), " +
                "$COL_ITEM_NAME VARCHAR(256), " +
                "$COL_ITEM_PRICE REAL," +
                "$COL_ITEM_DESC VARCHAR(256)," +
                "$COL_ITEM_CATEGORY VARCHAR(256)," +
                "$COL_ITEM_STAR INTEGER," +
                "$COL_ITEM_IMAGE_URL VARCHAR(256)" +
                ");"

        val createCartTable = "CREATE TABLE $CART_TABLE_NAME (" +
                "$CART_ITEM_ID VARCHAR(256), " +
                "$CART_ITEM_NAME VARCHAR(256), " +
                "$CART_ITEM_PRICE REAL," +
                "$CART_ITEM_SHORT_DESC VARCHAR(256)," +
                "$CART_ITEM_QTY INTEGER," +
                "$CART_ITEM_STARS INTEGER," +
                "$CART_IMAGE_URL VARCHAR(256)" +
                ");"

        val createOrderHistoryTable = "CREATE TABLE $ORDER_HISTORY_TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_ORDER_DATE VARCHAR(256), " +
                "$COL_ORDER_ID VARCHAR(256)," +
                "$COL_ORDER_STATUS VARCHAR(256)," +
                "$COL_ORDER_PAYMENT VARCHAR(256)," +
                "$COL_ORDER_PRICE VARCHAR(256)" +
                ");"

        val createCurrentOrdersTable = "CREATE TABLE $CURRENT_ORDER_TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_CURRENT_ORDER_ID VARCHAR(256), " +
                "$COL_CURRENT_ORDER_TAKE_AWAY_TIME VARCHAR(256), " +
                "$COL_CURRENT_ORDER_PAYMENT_STATUS VARCHAR(256), " +
                "$COL_CURRENT_ORDER_ITEM_NAMES VARCHAR(256), " +
                "$COL_CURRENT_ORDER_ITEM_QUANTITIES VARCHAR(256), " +
                "$COL_CURRENT_ORDER_TOTAL_ITEM_PRICE VARCHAR(256), " +
                "$COL_CURRENT_ORDER_TAX VARCHAR(256), " +
                "$COL_CURRENT_ORDER_SUB_TOTAL VARCHAR(256)" +
                ");"

        val createSavedCardsTable = "CREATE TABLE $SAVED_CARDS_TABLE_NAME (" +
                "$COL_SAVED_CARD_NUMBER VARCHAR(16) PRIMARY KEY, " +
                "$COL_SAVED_CARD_HOLDER_NAME VARCHAR(50), " +
                "$COL_SAVED_CARD_EXPIRY_DATE VARCHAR(5)" +
                ");"

        db?.execSQL(createOfflineShopTable)
        db?.execSQL(createOrderHistoryTable)
        db?.execSQL(createCurrentOrdersTable)
        db?.execSQL(createSavedCardsTable)
        db?.execSQL(createCartTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    fun insertCartItem(itemSifaa: SifaaCartItem) {
        val db = this.writableDatabase

        val query = "SELECT * FROM $CART_TABLE_NAME WHERE $CART_ITEM_ID='${itemSifaa.itemID}';"
        val cursor = db.rawQuery(query, null);
        if (cursor.count > 0) {
            // Update qty, cart item is already added
            val cv = ContentValues()
            cv.put(CART_ITEM_QTY, itemSifaa.quantity)
            db.update(CART_TABLE_NAME, cv, "$CART_ITEM_ID = ?", Array(1) { itemSifaa.itemID })
            return
        }
        cursor.close();

        val cv = ContentValues()
        cv.put(CART_ITEM_ID, itemSifaa.itemID)
        cv.put(CART_ITEM_NAME, itemSifaa.itemName)
        cv.put(CART_ITEM_PRICE, itemSifaa.itemPrice)
        cv.put(CART_ITEM_SHORT_DESC, itemSifaa.itemShortDesc)
        cv.put(CART_IMAGE_URL, itemSifaa.imageUrl)
        cv.put(CART_ITEM_STARS, itemSifaa.itemStars)
        cv.put(CART_ITEM_QTY, itemSifaa.quantity)

        val result = db.insert(CART_TABLE_NAME, null, cv)
        if (result == (-1).toLong()) {
            Toast.makeText(context, "Failed to Insert Data", Toast.LENGTH_SHORT).show()
        }
    }

    fun readCartData(): MutableList<SifaaCartItem> {
        val list: MutableList<SifaaCartItem> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * from $CART_TABLE_NAME"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val item = SifaaCartItem()
                item.itemID = result.getString(result.getColumnIndex(CART_ITEM_ID)).toString()
                item.imageUrl =
                    result.getString(result.getColumnIndex(CART_IMAGE_URL)).toString()
                item.itemName = result.getString(result.getColumnIndex(CART_ITEM_NAME)).toString()
                item.itemPrice = result.getFloat(result.getColumnIndex(CART_ITEM_PRICE))
                item.itemShortDesc =
                    result.getString(result.getColumnIndex(CART_ITEM_SHORT_DESC)).toString()
                item.quantity = result.getInt(result.getColumnIndex(CART_ITEM_QTY))
                item.itemStars = result.getInt(result.getColumnIndex(CART_ITEM_STARS))

                list.add(item)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun deleteCartItem(itemSifaa: SifaaCartItem) {
        try {
            val db = this.writableDatabase
            db.delete(CART_TABLE_NAME, "$CART_ITEM_ID = ?", Array(1){itemSifaa.itemID})
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to Delete Cart Item\n$e", Toast.LENGTH_SHORT).show()
        }
    }

    fun clearCartTable() {
        try {
            val db = this.writableDatabase
            db.delete(CART_TABLE_NAME, null, null)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to delete the records", Toast.LENGTH_SHORT).show()
        }
    }

    fun insertOfflineShopData(itemSifaa: SifaaFloralItem) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ITEM_ID, itemSifaa.itemID)
        cv.put(COL_ITEM_NAME, itemSifaa.itemName)
        cv.put(COL_ITEM_PRICE, itemSifaa.itemPrice)
        cv.put(COL_ITEM_DESC, itemSifaa.itemShortDesc)
        cv.put(COL_ITEM_IMAGE_URL, itemSifaa.imageUrl)
        cv.put(COL_ITEM_CATEGORY, itemSifaa.itemTag)
        cv.put(COL_ITEM_STAR, itemSifaa.itemStars)
        val result = db.insert(OFFLINE_FLOWER_SHOP_TABLE_NAME, null, cv)
        if (result == (-1).toLong()) {
            Toast.makeText(context, "Failed to Insert Data", Toast.LENGTH_SHORT).show()
        }
    }

    fun readOfflineMenuData(): MutableList<SifaaFloralItem> {
        val list: MutableList<SifaaFloralItem> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * from $OFFLINE_FLOWER_SHOP_TABLE_NAME"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val item = SifaaFloralItem()
                item.itemID = result.getString(result.getColumnIndex(COL_ITEM_ID)).toString()
                item.imageUrl =
                    result.getString(result.getColumnIndex(COL_ITEM_IMAGE_URL)).toString()
                item.itemName = result.getString(result.getColumnIndex(COL_ITEM_NAME)).toString()
                item.itemPrice = result.getFloat(result.getColumnIndex(COL_ITEM_PRICE))
                item.itemShortDesc =
                    result.getString(result.getColumnIndex(COL_ITEM_DESC)).toString()
                item.itemTag = result.getString(result.getColumnIndex(COL_ITEM_CATEGORY)).toString()
                item.itemStars = result.getInt(result.getColumnIndex(COL_ITEM_STAR))

                list.add(item)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun clearTheOfflineShopTable() {
        try {
            val db = this.writableDatabase
            db.delete(OFFLINE_FLOWER_SHOP_TABLE_NAME, null, null)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to delete the records", Toast.LENGTH_SHORT).show()
        }
    }

    fun insertCurrentOrdersData(itemSifaa: SifaaCurrentOrderItem) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_CURRENT_ORDER_ID, itemSifaa.orderID)
        cv.put(COL_CURRENT_ORDER_TAKE_AWAY_TIME, itemSifaa.takeAwayTime)
        cv.put(COL_CURRENT_ORDER_PAYMENT_STATUS, itemSifaa.paymentStatus)
        cv.put(COL_CURRENT_ORDER_ITEM_NAMES, itemSifaa.orderItemNames)
        cv.put(COL_CURRENT_ORDER_ITEM_QUANTITIES, itemSifaa.orderItemQuantities)
        cv.put(COL_CURRENT_ORDER_TOTAL_ITEM_PRICE, itemSifaa.totalItemPrice)
        cv.put(COL_CURRENT_ORDER_TAX, itemSifaa.tax)
        cv.put(COL_CURRENT_ORDER_SUB_TOTAL, itemSifaa.subTotal)

        val result = db.insert(CURRENT_ORDER_TABLE_NAME, null, cv)
        if (result == (-1).toLong()) {
            Toast.makeText(context, "Failed to Insert Data", Toast.LENGTH_SHORT).show()
        }
    }

    fun readCurrentOrdersData(): MutableList<SifaaCurrentOrderItem> {
        val list: MutableList<SifaaCurrentOrderItem> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * from $CURRENT_ORDER_TABLE_NAME"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val item = SifaaCurrentOrderItem()
                item.orderID = result.getString(result.getColumnIndex(COL_CURRENT_ORDER_ID))
                item.takeAwayTime =
                    result.getString(result.getColumnIndex(COL_CURRENT_ORDER_TAKE_AWAY_TIME))
                item.paymentStatus =
                    result.getString(result.getColumnIndex(COL_CURRENT_ORDER_PAYMENT_STATUS))
                item.orderItemNames =
                    result.getString(result.getColumnIndex(COL_CURRENT_ORDER_ITEM_NAMES))
                item.orderItemQuantities =
                    result.getString(result.getColumnIndex(COL_CURRENT_ORDER_ITEM_QUANTITIES))
                item.totalItemPrice =
                    result.getString(result.getColumnIndex(COL_CURRENT_ORDER_TOTAL_ITEM_PRICE))
                item.tax = result.getString(result.getColumnIndex(COL_CURRENT_ORDER_TAX))
                item.subTotal = result.getString(result.getColumnIndex(COL_CURRENT_ORDER_SUB_TOTAL))
                list.add(item)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun deleteCurrentOrderRecord(orderId: String): String {
        val query = "DELETE FROM $CURRENT_ORDER_TABLE_NAME WHERE $COL_CURRENT_ORDER_ID='$orderId';"
        val db = this.writableDatabase
        db.execSQL(query)

        val updateOrderHistoryQuery =
            "UPDATE $ORDER_HISTORY_TABLE_NAME SET $COL_ORDER_STATUS='Order Cancelled' WHERE $COL_ORDER_ID='$orderId';"
        db.execSQL(updateOrderHistoryQuery)

        db.close()
        return "Order Cancelled"
    }

    fun dropCurrentOrdersTable() {
        try {
            val db = this.writableDatabase
            db.delete(CURRENT_ORDER_TABLE_NAME, null, null)
            Toast.makeText(context, "All records are deleted", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to delete the records", Toast.LENGTH_SHORT).show()
        }
    }

    fun insertOrderData(itemSifaa: SifaaOrderHistoryItem) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_ORDER_DATE, itemSifaa.date)
        cv.put(COL_ORDER_ID, itemSifaa.orderId)
        cv.put(COL_ORDER_STATUS, itemSifaa.orderStatus)
        cv.put(COL_ORDER_PAYMENT, itemSifaa.orderPayment)
        cv.put(COL_ORDER_PRICE, itemSifaa.price)

        val result = db.insert(ORDER_HISTORY_TABLE_NAME, null, cv)
        if (result == (-1).toLong()) {
            Toast.makeText(context, "Failed to Insert Data", Toast.LENGTH_SHORT).show()
        }
    }

    fun readOrderData(): MutableList<SifaaOrderHistoryItem> {
        val list: MutableList<SifaaOrderHistoryItem> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * from $ORDER_HISTORY_TABLE_NAME"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val item = SifaaOrderHistoryItem()
                item.date = result.getString(result.getColumnIndex(COL_ORDER_DATE))
                item.orderId = result.getString(result.getColumnIndex(COL_ORDER_ID))
                item.orderStatus = result.getString(result.getColumnIndex(COL_ORDER_STATUS))
                item.orderPayment = result.getString(result.getColumnIndex(COL_ORDER_PAYMENT))
                item.price = result.getString(result.getColumnIndex(COL_ORDER_PRICE))
                item.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                list.add(item)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun dropOrderHistoryTable() {
        try {
            val db = this.writableDatabase
            db.delete(ORDER_HISTORY_TABLE_NAME, null, null)
            Toast.makeText(context, "All records are deleted", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to delete the records", Toast.LENGTH_SHORT).show()
        }
    }

    fun insertSavedCardDetails(itemSifaa: SifaaSavedCardItem): Int {
        val db = this.writableDatabase

        val query =
            "SELECT * FROM $SAVED_CARDS_TABLE_NAME WHERE $COL_SAVED_CARD_NUMBER='${itemSifaa.cardNumber}';"
        val cursor = db.rawQuery(query, null);
        if (cursor.count > 0) { //card is already saved
            cursor.close();
            return -1;
        }
        cursor.close();

        val cv = ContentValues()
        cv.put(COL_SAVED_CARD_NUMBER, itemSifaa.cardNumber)
        cv.put(COL_SAVED_CARD_HOLDER_NAME, itemSifaa.cardHolderName)
        cv.put(COL_SAVED_CARD_EXPIRY_DATE, itemSifaa.cardExpiryDate)

        val result = db.insert(SAVED_CARDS_TABLE_NAME, null, cv)
        if (result == (-1).toLong()) {
            Toast.makeText(context, "Card Not Saved", Toast.LENGTH_SHORT).show()
        }
        return 1
    }

    fun readSavedCardsData(): MutableList<SifaaSavedCardItem> {
        val list: MutableList<SifaaSavedCardItem> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * from $SAVED_CARDS_TABLE_NAME"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val item = SifaaSavedCardItem()
                item.cardNumber = result.getString(result.getColumnIndex(COL_SAVED_CARD_NUMBER))
                item.cardHolderName =
                    result.getString(result.getColumnIndex(COL_SAVED_CARD_HOLDER_NAME))
                item.cardExpiryDate =
                    result.getString(result.getColumnIndex(COL_SAVED_CARD_EXPIRY_DATE))
                list.add(item)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun clearSavedCards() {
        try {
            val db = this.writableDatabase
            db.delete(SAVED_CARDS_TABLE_NAME, null, null)
            Toast.makeText(context, "All cards are removed", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to remove cards", Toast.LENGTH_SHORT).show()
        }
    }
}

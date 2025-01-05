package com.sagara.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "cart.db"
        private const val DATABASE_VERSION = 1

        // Table and columns
        private const val TABLE_CART = "cart"
        private const val COLUMN_PRODUCT_ID = "product_id"
        private const val COLUMN_COUNT = "count"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_CART (
                $COLUMN_PRODUCT_ID TEXT PRIMARY KEY,
                $COLUMN_COUNT INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    // Insert a product into the cart
    fun insertProduct(productId: String, count: Int): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PRODUCT_ID, productId)
            put(COLUMN_COUNT, count)
        }
        val result = db.insert(TABLE_CART, null, contentValues)
        db.close()
        return result != -1L
    }

    // Update the count of a product in the cart
    fun updateProductCount(productId: String, count: Int): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_COUNT, count)
        }
        val result = db.update(TABLE_CART, contentValues, "$COLUMN_PRODUCT_ID = ?", arrayOf(productId))
        db.close()
        return result > 0
    }

    // Retrieve all products from the cart
    fun getAllProducts(): List<Pair<String, Int>> {
        val cartList = mutableListOf<Pair<String, Int>>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CART, null, null, null, null, null, null)

        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val productId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID))
                    val count = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COUNT))
                    cartList.add(Pair(productId, count))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        db.close()
        return cartList
    }

    // Retrieve a single product by its ID
    fun getProductById(productId: String): Pair<String, Int>? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CART, // Table name
            null, // Select all columns
            "$COLUMN_PRODUCT_ID = ?", // WHERE clause
            arrayOf(productId), // WHERE arguments
            null, // Group by
            null, // Having
            null // Order by
        )

        var product: Pair<String, Int>? = null

        cursor?.let {
            if (cursor.moveToFirst()) {
                val productId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID))
                val count = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COUNT))
                product = Pair(productId, count)
            }
            cursor.close()
        }
        db.close()
        return product
    }


    // Delete a product from the cart
    fun deleteProduct(productId: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_CART, "$COLUMN_PRODUCT_ID = ?", arrayOf(productId))
        db.close()
        return result > 0
    }

    // Clear the entire cart
    fun clearCart(): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_CART, null, null)
        db.close()
        return result > 0
    }
}

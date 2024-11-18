package com.example.visionapi

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Product(var name: String, var kcal: String, var target: String, var type: String, var total: String)

class ProductDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_PRODUCT = "product"
        const val COLUMN_NAME = "name"
        const val COLUMN_KCAL = "kcal"
        const val COLUMN_TARGET = "target"
        const val COLUMN_TYPE  = "type"
        const val COLUMN_TOTAL = "total"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createProductTableQuery = ("CREATE TABLE $TABLE_PRODUCT (" +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_KCAL TEXT, " +
                "$COLUMN_TARGET TEXT, " +
                "$COLUMN_TYPE TEXT, " +
                "$COLUMN_TOTAL TEXT)"
                )
        db.execSQL(createProductTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        onCreate(db)
    }

    // 데이터 삽입 함수
    fun insertProduct(name: String, kcal: String, target: List<String>, type: String, total: String) {
        val db = writableDatabase

        // 배열 데이터를 쉼표로 구분된 문자열로 변환
        val targetString = target.joinToString(",")

        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_KCAL, kcal)
            put(COLUMN_TARGET, targetString)
            put(COLUMN_TYPE, type)
            put(COLUMN_TOTAL, total)
        }

        db.insert(TABLE_PRODUCT, null, values)
        db.close()
    }


    // 데이터 조회 함수
    fun getAllProducts(): List<Product> {
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, null)
        val products = mutableListOf<Product>()
        val productNames = mutableSetOf<String>() // 중복된 이름 확인용 Set

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val kcal = getString(getColumnIndexOrThrow(COLUMN_KCAL))

                // 문자열로 저장된 target 데이터를 배열로 변환
                val targetString = getString(getColumnIndexOrThrow(COLUMN_TARGET))
                val targetList = targetString.split(",")

                val type = getString(getColumnIndexOrThrow(COLUMN_TYPE))
                val total = getString(getColumnIndexOrThrow(COLUMN_TOTAL))

                // 중복되지 않은 제품만 추가
                if (!productNames.contains(name)) {
                    products.add(Product(name, kcal, targetList.joinToString(","), type, total))
                    productNames.add(name)
                }
            }
        }
        cursor.close()
        db.close()

        return products
    }


    fun getProductsName(): List<String> {
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, null)
        val products = mutableListOf<String>()

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                products.add(name)
            }
        }
        cursor.close()
        db.close()

        return products
    }

    fun getProduct(name: String): Product {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCT WHERE $COLUMN_NAME = ?", arrayOf(name))
        var product: Product? = null

        if (cursor != null && cursor.moveToFirst()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val kcal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KCAL))

            // target 데이터를 배열로 변환
            val targetString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TARGET))

            val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
            val total = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOTAL))
            product = Product(productName, kcal, targetString, type, total)
        }
        cursor.close()
        db.close()

        return product ?: Product("N/A", "N/A", "N/A", "N/A", "N/A")
    }

    // 데이터 삭제 함수
    fun deleteProduct(name: String) {
        val db = writableDatabase
        db.delete(TABLE_PRODUCT, "$COLUMN_NAME = ?", arrayOf(name))
        db.close()
    }
}

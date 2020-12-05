package com.example.suggestripapp.fav

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class FavDB(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}

    // create empty table
    fun insertEmpty() {
        val db = this.writableDatabase
        val cv = ContentValues()
        // enter your value
        for (x in 1..10) {
            cv.put(ID, x)
            cv.put(FAVORITE_STATUS, "0")
            db.insert(TABLE_NAME, null, cv)
        }
    }

    // insert data into database
    fun insertIntoTheDatabase(name: String, url_img: String, id: String?, fav_status: String) {
        val db: SQLiteDatabase
        db = this.writableDatabase
        val cv = ContentValues()
        cv.put(NAME, name)
        cv.put(IMG_URL, url_img)
        cv.put(ID, id)
        cv.put(FAVORITE_STATUS, fav_status)
        db.insert(TABLE_NAME, null, cv)
        Log.d("FavDB Status", "$name, favstatus - $fav_status - . $cv")
    }

    // read all data
    fun read_all_data(id: String): Cursor {
        val db = this.readableDatabase
        val sql = "select * from " + TABLE_NAME + " where " + ID + "=" + id + ""
        return db.rawQuery(sql, null, null)
    }

    // remove line from database
    fun remove_fav(id: String) {
        val db = this.writableDatabase
        val sql = "UPDATE " + TABLE_NAME + " SET  " + FAVORITE_STATUS + " ='0' WHERE " + ID + "=" + id + ""
        db.execSQL(sql)
        Log.d("remove", id)
    }

    // select all favorite list
    fun select_all_favorite_list(): Cursor {
        val db = this.readableDatabase
        val sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + FAVORITE_STATUS + " ='1'"
        return db.rawQuery(sql, null, null)
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DATABASE_NAME = "CityDB"
        private const val TABLE_NAME = "favoriteTable"
        var ID = "id"
        var NAME = "itemTitle"
        var IMG_URL = "itemImage"
        var FAVORITE_STATUS = "fStatus"

        // dont forget write this spaces
        private val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + " TEXT," + NAME + " TEXT,"
                + IMG_URL + " TEXT," + FAVORITE_STATUS + " TEXT)")
    }
}
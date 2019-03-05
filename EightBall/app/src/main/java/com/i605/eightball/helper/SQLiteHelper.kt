package com.i605.eightball.helper

import java.util.concurrent.atomic.AtomicInteger
import android.content.ContentValues
import android.database.sqlite.*
import com.i605.eightball.*

object SQLiteHelper : SQLiteOpenHelper(App.getAppContext(), "Data.db", null, 1) {
    private var count = AtomicInteger()
    private lateinit var sqlDatabase: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE ITEMS (CATEGORY NVARCHAR(50), NAME NVARCHAR(50))")

        val titleFortune = App.getAppContext().getString(R.string.fortune_telling)
        App.getAppContext().resources.getStringArray(R.array.fortune_telling).forEach { name ->
            val contentValues = ContentValues()
            contentValues.put("CATEGORY", titleFortune)
            contentValues.put("NAME", name)
            db.insert("ITEMS", null, contentValues)
        }

        val titleFood = App.getAppContext().getString(R.string.what_to_eat)
        App.getAppContext().resources.getStringArray(R.array.what_to_eat).forEach { name ->
            val contentValues = ContentValues()
            contentValues.put("CATEGORY", titleFood)
            contentValues.put("NAME", name)
            db.insert("ITEMS", null, contentValues)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    @Synchronized
    fun getDatabase(): SQLiteDatabase {
        if (count.incrementAndGet() == 1) {
            sqlDatabase = super.getReadableDatabase()
        }
        return sqlDatabase
    }

    @Synchronized
    fun closeDatabase() {
        if (count.decrementAndGet() == 0) {
            sqlDatabase.close()
        }
    }
}
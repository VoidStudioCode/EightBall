package com.i605.eightball.model

import android.content.ContentValues
import com.i605.eightball.helper.SQLiteHelper
import com.i605.eightball.bean.Item
import com.i605.eightball.*

object Manager {
    val list = mutableListOf<Item>()

    init {
        val temp = mutableListOf<List<String>>()
        val db = SQLiteHelper.readableDatabase
        val cursor = db.query("ITEMS", arrayOf("CATEGORY", "NAME"), null, null, null, null, null)
        while (cursor.moveToNext()) {
            val cat = cursor.getString(cursor.getColumnIndex("CATEGORY"))
            val name = cursor.getString(cursor.getColumnIndex("NAME"))
            temp.add(listOf(cat, name))
        }
        cursor.close()
        db.close()

        temp.groupBy { list -> list[0] }.forEach { k, v ->
            val item = Item(k)
            item.list.addAll(v.map { l -> l[1] })
            list.add(item)
        }
    }

    fun getList(cat: String): MutableList<String> {
        return list.first { item -> item.category == cat }.list
    }

    fun isCatExist(cat: String): Boolean {
        return list.any { item -> item.category == cat }
    }

    fun addCat(cat: String, name: String) {
        val item = Item(cat)
        item.list.add(name)
        list.add(item)

        val db = SQLiteHelper.readableDatabase
        val contentValues = ContentValues()
        contentValues.put("CATEGORY", cat)
        contentValues.put("NAME", name)
        db.insert("ITEMS", null, contentValues)
        db.close()
    }

    fun addItem(cat: String, name: String) {
        list.first { item -> item.category == cat }.list.add(name)

        val db = SQLiteHelper.readableDatabase
        val contentValues = ContentValues()
        contentValues.put("CATEGORY", cat)
        contentValues.put("NAME", name)
        db.insert("ITEMS", null, contentValues)
        db.close()
    }

    fun canDelete(cat: String): Boolean {
        val titleFortune = App.getAppContext().getString(R.string.fortune_telling)
        return if (cat == titleFortune) {
            false
        } else {
            list.first { item -> item.category != titleFortune }.list.size > 1
        }
    }

    fun deleteItem(cat: String, name: String): Boolean {
        val item = list.first { item -> item.category == cat }
        item.list.remove(name)
        val db = SQLiteHelper.readableDatabase
        db.delete("ITEMS", "CATEGORY = ? AND NAME = ?", arrayOf(cat, name))
        db.close()

        return if (item.list.isEmpty()) {
            list.remove(item)
            true
        } else {
            false
        }
    }
}
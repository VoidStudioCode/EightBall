package com.i605.eightball.presenter

import android.support.v7.app.AppCompatActivity
import com.i605.eightball.view.ActivitySettings
import com.i605.eightball.model.Manager
import com.i605.eightball.R

class PresenterSettings(private val activity: ActivitySettings) {
    val list by lazy {
        val temp = mutableListOf<String>()
        temp.addAll(Manager.list[0].list)
        temp
    }

    fun updateRv(cat: String) {
        list.clear()
        list.addAll(Manager.getList(cat))
    }

    fun getCategories(): MutableList<String> {
        val tempCat = Manager.list.map { item -> item.category }.toMutableList()
        tempCat.add(activity.getString(R.string.new_category))
        return tempCat
    }

    fun isCatExist(cat: String): Boolean {
        return Manager.isCatExist(cat)
    }

    fun isNameExist(name: String): Boolean {
        return list.contains(name)
    }

    fun addCat(cat: String, name: String) {
        Manager.addCat(cat, name)
    }

    fun addItem(cat: String, name: String) {
        list.add(name)
        Manager.addItem(cat, name)
    }

    fun canAdd(cat: String): Boolean {
        return cat != activity.getString(R.string.fortune_telling)
    }

    fun canDelete(cat: String): Boolean {
        return Manager.canDelete(cat)
    }

    fun deleteItem(cat: String, name: String): Boolean {
        list.remove(name)
        activity.setResult(AppCompatActivity.RESULT_OK)
        return Manager.deleteItem(cat, name)
    }
}
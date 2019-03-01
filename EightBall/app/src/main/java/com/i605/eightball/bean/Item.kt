package com.i605.eightball.bean

data class Item(val category: String) {
    val list: MutableList<String> = mutableListOf()
}
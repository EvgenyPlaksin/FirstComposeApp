package com.iplizi.firstcomposeapp

interface Paginator<Key, Item> {
   suspend fun loadNextItems()
    fun reset()
}
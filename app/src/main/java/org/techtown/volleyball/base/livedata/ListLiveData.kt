package org.techtown.volleyball.base.livedata

import androidx.lifecycle.MutableLiveData

class ListLiveData<T> : MutableLiveData<MutableList<T>>() {
    private val list = mutableListOf<T>()

    init {
        value = list
    }

    fun add(item: T){
        list.add(item)
        value = list
    }

    fun addAll(items: List<T>) {
        list.addAll(items)
        value = list
    }

    fun remove(item: T) {
        list.remove(item)
        value = list
    }

    fun clear() {
        list.clear()
        postValue(list)
    }

    fun setlist(items: List<T>) {
        list.clear()
        list.addAll(items)
        postValue(list)
    }

    fun get(position: Int): T? = list.getOrNull(position)
    fun getSize(): Int = list.size

    fun isListEmpty(): Boolean = list.isEmpty()
}
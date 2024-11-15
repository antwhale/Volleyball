package org.techtown.volleyball.base.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

inline fun <T> MediatorLiveData<T>.combineSourceData(
    vararg sources: LiveData<*>,
    crossinline observer: () -> T
) {
    sources.forEach {
        this.addSource(it){
            value = observer.invoke()
        }
    }
}
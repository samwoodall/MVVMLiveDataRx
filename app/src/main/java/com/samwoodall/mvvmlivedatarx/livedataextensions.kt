package com.samwoodall.mvvmlivedatarx

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <A, B> MediatorLiveData<A>.addOneTimeSource(source: LiveData<B>, onItemEmitted: MediatorLiveData<A>.(B) -> Unit) {
    addSource(source) {
        onItemEmitted(it)
        removeSource(source)
    }
}
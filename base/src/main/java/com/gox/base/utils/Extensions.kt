package com.gox.base.utils

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

inline fun <T> dependantObservableField(vararg dependencies: Observable, crossinline mapper: () -> T?) =
        object : ObservableField<T>(*dependencies) {
            override fun get(): T? {
                return mapper()
            }
        }

inline fun <T> dependantLiveData(vararg dependencies: LiveData<*>, crossinline mapper: () -> T?) =
        MediatorLiveData<T>().also { mediatorLiveData ->
            val observer = Observer<Any> { mediatorLiveData.value = mapper() }
            dependencies.forEach { dependencyLiveData ->
                mediatorLiveData.addSource(dependencyLiveData, observer)
            }
        }

inline fun <reified T : Observable> T.observe(crossinline observer: (T) -> Unit): Observable.OnPropertyChangedCallback =
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                observer(sender as T)
            }
        }.also { addOnPropertyChangedCallback(it) }
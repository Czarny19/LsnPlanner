package com.lczarny.lsnplanner.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun MutableStateFlow<Boolean>.updateIfChanged(condition: Boolean) {
    if (condition) {
        if (this.value.not()) this.update { true }
    } else {
        if (this.value) this.update { false }
    }
}

fun <T> MutableStateFlow<List<T>>.deleteItem(item: T, onFinished: () -> Unit) {
    this.value.indexOf(item).let {
        if (it >= 0) {
            val newList = this.value.toMutableList()

            newList[it].let { item -> newList.removeAt(it) }

            this.update { newList }
            onFinished.invoke()
        }
    }
}

fun <T> MutableStateFlow<List<T>>.updateItem(oldVal: T, newVal: T, onFinished: () -> Unit) {
    this.value.indexOf(oldVal).let {
        if (it >= 0) {
            val newList = this.value.toMutableList()
            newList[it] = newVal

            this.update { newList }
            onFinished.invoke()
        }
    }
}
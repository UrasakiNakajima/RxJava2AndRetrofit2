package com.phone.module_square.observer

import java.util.*

class MineObservable : Observable() {

    companion object {
        val TAG = MineObservable::class.java.simpleName
    }

    fun sendData(data: String) {
        setChanged()
        notifyObservers(data)
    }
}
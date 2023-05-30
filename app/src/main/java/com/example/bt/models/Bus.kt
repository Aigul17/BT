package com.example.bt.models

import com.example.bt.fragments.User


open class Bus : User() {
    val uid: true
    override var role = "driver"
    var online = false
    var subscribers = ArrayList<Any?>()
}

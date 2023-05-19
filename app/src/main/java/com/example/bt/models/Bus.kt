package com.example.bt.models

import com.example.bt.User


open class Bus : User() {
    override var role = "driver"
    var online = false
    var subscribers = ArrayList<Any?>()
}

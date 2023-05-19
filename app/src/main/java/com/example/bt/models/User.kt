package com.example.bt.models

open class User {
    var uid = "NA"
    var name = "Unknown"
    open var role = ""
    var identity = "Unknown"
    var approved = false
    override fun toString(): String {
        return name
    }
}

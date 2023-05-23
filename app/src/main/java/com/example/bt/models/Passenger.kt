package com.example.bt.models

open class Passenger : User() {
    override var role = "passenger"
    var buses: List<Bus> = ArrayList<Bus>()
}

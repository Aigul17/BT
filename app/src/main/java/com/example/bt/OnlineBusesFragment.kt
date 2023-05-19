package com.example.bt

import android.os.Bundle
import bt.bustracking.R

open class OnlineBusesFragment : ListFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.online_buses)
    }
}
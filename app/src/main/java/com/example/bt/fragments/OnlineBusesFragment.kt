package com.example.bt.fragments

import android.os.Bundle
import com.example.bt.R

open class OnlineBusesFragment : ListFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.online_buses)
    }
}
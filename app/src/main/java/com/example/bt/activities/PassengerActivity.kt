package com.example.bt.activities


import android.os.Bundle
//import android.support.design.widget.NavigationView
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import com.example.bt.mUser
import com.google.android.material.navigation.NavigationView
import com.example.bt.R
import com.example.bt.fragments.SelectBusesFragment
import kotlinx.android.synthetic.main.activity_home.*

class PassengerActivity : HomeActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var user = mUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nav_view.inflateMenu(R.menu.menu_passenger)
        nav_view.menu.findItem(R.id.menu_online_buses).actionView = TextView(this).also {
            it.gravity = Gravity.CENTER
        }

        if (savedInstanceState == null && mUser.approved) {
            showOnlineBuses()
            nav_view.menu.findItem(R.id.menu_online_buses).isChecked = true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update_subscription -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SelectBusesFragment()).commit()
                hideWaiting()
            }
            else -> if (!user.approved) {
                displayWaiting()
            }
        }

        return super.onNavigationItemSelected(item)
    }

    override fun showOnlineBuses() {
        if (!user.approved) displayWaiting()
        super.showOnlineBuses()
    }
}

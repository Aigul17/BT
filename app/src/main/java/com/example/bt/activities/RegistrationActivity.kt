package com.example.bt.activities



import android.os.Bundle
import com.example.bt.R
import com.example.bt.fragments.CreateProfileFragment
import com.example.bt.fragments.SelectBusesFragment


class RegistrationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CreateProfileFragment()).commit()
    }

    fun showBusSelection() =
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SelectBusesFragment()).addToBackStack("ShowBusSelection").commit()

}
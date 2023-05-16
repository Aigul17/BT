package com.example.bt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var phoneEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        nameEditText = view.findViewById(R.id.name_edit_text)
        surnameEditText = view.findViewById(R.id.surname_edit_text)
        birthdayEditText = view.findViewById(R.id.birthday_edit_text)
        phoneEditText = view.findViewById(R.id.phone_edit_text)

        val logoButton = view.findViewById<TextView>(R.id.logo_button)
        logoButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }
}

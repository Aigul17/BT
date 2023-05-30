package com.example.bt.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bt.R


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var phoneEditText: EditText



    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
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

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        val saveButton = view.findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            saveUserData()
        }

        return view
    }

    private fun saveUserData() {
        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val birthday = birthdayEditText.text.toString()
        val phone = phoneEditText.text.toString()

        val userId = UUID.randomUUID().toString() // Generate a unique ID for the user

        val user = User(userId, name, surname, birthday, phone)

        // Save user data to Firebase database
        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Ваши данные сохранены", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Не удалось сохранить ваши данные", Toast.LENGTH_SHORT).show()
            }
    }
}

data class User(
    val userId: String,
    val name: String,
    val surname: String,
    val birthday: String,
    val phone: String
)


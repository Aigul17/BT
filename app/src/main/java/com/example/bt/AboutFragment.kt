package com.example.bt



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        val logoButton = view.findViewById<ImageView>(R.id.logo_button)
        logoButton.setOnClickListener {
            // Handle logo button click event
        }

        return view
    }
}

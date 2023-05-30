package com.example.bt.adapters

import android.widget.TextView
import com.example.bt.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class PassengerListAdapter(options: FirestoreRecyclerOptions<Any>, private val listener: (item: Map<*, *>) -> Unit) : AnyAdapter(options, listener) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val passenger = getItem(position) as Map<*, *>
        holder.view.apply {
            findViewById<TextView>(R.id.textName).text = passenger["name"]?.toString()
            findViewById<TextView>(R.id.text2).text = passenger["identity"]?.toString()
            setOnClickListener { listener.invoke(passenger) }
        }
    }
}

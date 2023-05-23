package com.example.bt.adapters

import android.view.View
import android.widget.ImageButton
import com.example.bt.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class PendingBusAdapter(options: FirestoreRecyclerOptions<Any>, deleteListener: (item: Map<*, *>) -> Unit, private val confirmListener: (item: Map<*, *>) -> Unit) : SimpleBusAdapter(options, deleteListener) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.view.apply {
            findViewById<ImageButton>(R.id.secondButton).apply {
                this.visibility = View.VISIBLE
                this.setOnClickListener {
                    try {
                        confirmListener.invoke(getItem(position) as Map<*, *>)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        }
    }
}
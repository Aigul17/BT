package com.example.bt.adapters

import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.bt.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class PendingPassengerAdapter(options: FirestoreRecyclerOptions<Any>, private val actionListener: (item: Map<*, *>) -> Unit, private val listener: (item: Map<*, *>) -> Unit) : AnyAdapter(options, listener) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val teacher = getItem(position) as Map<*, *>
        holder.view.apply {
            findViewById<TextView>(R.id.textName).text = teacher["name"]?.toString()
            findViewById<TextView>(R.id.text2).text = teacher["identity"]?.toString()
            findViewById<ImageButton>(R.id.secondButton).apply {
                this.visibility = View.VISIBLE
                this.setImageResource(R.drawable.ic_account_plus)
                (this.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.ALIGN_PARENT_END)
                this.setOnClickListener { actionListener.invoke(teacher) }
            }
            this.setOnClickListener {
                listener.invoke(teacher)
            }
        }
    }
}


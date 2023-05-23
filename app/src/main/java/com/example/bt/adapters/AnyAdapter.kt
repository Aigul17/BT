package com.example.bt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bt.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


open class AnyAdapter(options: FirestoreRecyclerOptions<Any>, private val listener: (item: Map<*, *>) -> Unit) : FirestoreRecyclerAdapter<Any, AnyAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sample_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Any) {
        holder.view.setOnClickListener { listener.invoke(getItem(position) as Map<*, *>) }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}

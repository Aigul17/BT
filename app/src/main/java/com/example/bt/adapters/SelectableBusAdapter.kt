package com.example.bt.adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.bt.R
import com.example.bt.models.Bus
import com.example.bt.mUser


class SelectableBusAdapter(private val buses: List<Bus>, private val uid: String = mUser.uid) : RecyclerView.Adapter<SelectableBusAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return buses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.v.findViewById<CheckBox>(R.id.checkbox).apply {
            text = buses[position].name
            if (buses[position].subscribers.contains(uid))
                isChecked = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.selectable_bus, parent, false)!!)

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)
}
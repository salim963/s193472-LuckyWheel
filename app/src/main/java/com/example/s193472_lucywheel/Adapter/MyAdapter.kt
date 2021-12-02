package com.example.s193472_luckywheel.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s193472_lucywheel.Data.AnimalsName
import com.example.s193472_lucywheel.R

class MyAdapter(val data: List<AnimalsName>): RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sitem,parent,false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.MyItem.text=data[position].char.toString()

    }

    override fun getItemCount(): Int {
        return data.size
    }



    inner class ViewHolder
    internal constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        val MyItem: TextView = itemView.findViewById(R.id.MyItem)
    }




}








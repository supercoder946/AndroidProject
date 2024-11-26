package com.example.visionapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class AllergyAdapter(
    private val allergyList: List<String>,
    private val onCheckChange: (String, Boolean) -> Unit
) : RecyclerView.Adapter<AllergyAdapter.AllergyViewHolder>() {

    private var currentList: List<String> = allergyList

    inner class AllergyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_allergy, parent, false)
        return AllergyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllergyViewHolder, position: Int) {
        val allergy = currentList[position]
        holder.checkBox.text = allergy
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckChange(allergy, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun updateList(newList: List<String>) {
        currentList = newList
        notifyDataSetChanged()
    }
}

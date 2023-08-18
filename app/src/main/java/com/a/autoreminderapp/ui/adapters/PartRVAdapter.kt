package com.a.autoreminderapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a.autoreminderapp.R
import com.a.autoreminderapp.data.entities.Part

// класс-адаптер для вывода списка запчастей в RecyclerView
class PartRVAdapter(
    val context: Context,
    val partClickInterface: PartClickInterface,
    val partLongClickInterface: PartLongClickInterface
) : RecyclerView.Adapter<PartRVAdapter.ViewHolder>() {

    private val allParts = ArrayList<Part>()

    // вложенный класс для хранения связей с визуальными элементами
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
        val tvMaxServiceLife = itemView.findViewById<TextView>(R.id.tvMaxServiceLife)
        val tvCurrentServiceLife = itemView.findViewById<TextView>(R.id.tvCurrentServiceLife)
        val tvBrand = itemView.findViewById<TextView>(R.id.tvBrand)
        val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.part_rv_item, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return allParts.size
    }

    // привязка визуальных элементов к данным
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val part = allParts[position]

        holder.tvName.text = part.name
        holder.tvMaxServiceLife.text = "Срок максимальной эксплуатации: " + part.maxServiceLife.toString() + "км."
        holder.tvCurrentServiceLife.text = "Срок текущей эксплуатации: " + part.currentServiceLife.toString()  + "км."
        holder.tvBrand.text = part.brand
        holder.tvDescription.text = part.description

        holder.itemView.setOnClickListener{
            partClickInterface.onClick(part)
        }

        holder.itemView.setOnLongClickListener {
            partLongClickInterface.onLongClick(part)
            true
        }
    }

    // обновление элементов списка RecyclerView
    fun updateList(newList: List<Part>){
        allParts.clear()
        allParts.addAll(newList)
        notifyDataSetChanged()
    }
}

// интерфейс для обработки нажатия на элемент RecyclerView
interface PartClickInterface {
    // функция обработки нажатия
    fun onClick(part: Part)
}

// интерфейс для обработки длинного нажатия на элемент RecyclerView
interface PartLongClickInterface {
    // функция обработки длинного нажатия
    fun onLongClick(part: Part)
}
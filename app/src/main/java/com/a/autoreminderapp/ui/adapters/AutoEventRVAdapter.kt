package com.a.autoreminderapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a.autoreminderapp.R
import com.a.autoreminderapp.data.entities.AutoEvent
import com.a.autoreminderapp.data.entities.Category

// класс-адаптер для вывода списка событий в RecyclerView
class AutoEventRVAdapter(
    val context: Context,
    val autoEventClickInterface: AutoEventClickInterface,
    val autoEventLongClickInterface: AutoEventLongClickInterface
) : RecyclerView.Adapter<AutoEventRVAdapter.ViewHolder>() {
    // список объектов
    private val allAutoEvents = ArrayList<AutoEvent>()

    // вложенный класс для хранения связей с визуальными элементами
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAutoEventName = itemView.findViewById<TextView>(R.id.tvAutoEventName)
        val tvAutoEventDescription = itemView.findViewById<TextView>(R.id.tvAutoEventDescription)
        val tvDateTime = itemView.findViewById<TextView>(R.id.tvDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.auto_event_rv_item, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return allAutoEvents.size
    }

    // привязка визуальных элементов к данным
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val autoEvent = allAutoEvents[position]

        holder.tvAutoEventName.text = autoEvent.name
        holder.tvAutoEventDescription.text = autoEvent.description
        holder.tvDateTime.text = autoEvent.date + " " + autoEvent.time

        holder.itemView.setOnClickListener{
            autoEventClickInterface.onClick(autoEvent)
        }

        holder.itemView.setOnLongClickListener {
            autoEventLongClickInterface.onLongClick(autoEvent)
            true
        }
    }

    // обновление элементов списка RecyclerView
    fun updateList(newList: List<AutoEvent>){
        allAutoEvents.clear()
        allAutoEvents.addAll(newList)
        notifyDataSetChanged()
    }
}

// интерфейс для обработки нажатия на элемент RecyclerView
interface AutoEventClickInterface {
    // функция обработки нажатия
    fun onClick(autoEvent: AutoEvent)
}

// интерфейс для обработки длинного нажатия на элемент RecyclerView
interface AutoEventLongClickInterface {
    // функция обработки длинного нажатия
    fun onLongClick(autoEvent: AutoEvent)
}

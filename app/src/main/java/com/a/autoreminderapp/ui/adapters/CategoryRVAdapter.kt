package com.a.autoreminderapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a.autoreminderapp.R
import com.a.autoreminderapp.data.entities.Category

// класс-адаптер для вывода списка категорий в RecyclerView
class CategoryRVAdapter(
    val context: Context,
    val categoryClickInterface: CategoryClickInterface,
    val categoryLongClickInterface: CategoryLongClickInterface
) : RecyclerView.Adapter<CategoryRVAdapter.ViewHolder>() {
    // список объектов
    private val allCategories = ArrayList<Category>()

    // вложенный класс для хранения связей с визуальными элементами
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvCategoryName = itemView.findViewById<TextView>(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.category_rv_item, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return allCategories.size
    }

    // привязка визуальных элементов к данным
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = allCategories[position]

        holder.tvCategoryName.text = category.name
        holder.itemView.setOnClickListener{
            categoryClickInterface.onClick(category)
        }

        holder.itemView.setOnLongClickListener {
            categoryLongClickInterface.onLongClick(category)
            true
        }
    }

    // обновление элементов списка RecyclerView
    fun updateList(newList: List<Category>){
        allCategories.clear()
        allCategories.addAll(newList)
        notifyDataSetChanged()
    }
}

// интерфейс для обработки нажатия на элемент RecyclerView
interface CategoryClickInterface {
    // функция обработки нажатия
    fun onClick(category: Category)
}

// интерфейс для обработки длинного нажатия на элемент RecyclerView
interface CategoryLongClickInterface {
    // функция обработки длинного нажатия
    fun onLongClick(category: Category)
}

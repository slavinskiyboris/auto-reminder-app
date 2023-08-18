package com.a.autoreminderapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a.autoreminderapp.ui.activities.AddEditCategoryActivity
import com.a.autoreminderapp.ui.activities.CategoryPartsActivity
import com.a.autoreminderapp.R
import com.a.autoreminderapp.data.entities.Category
import com.a.autoreminderapp.ui.adapters.CategoryClickInterface
import com.a.autoreminderapp.ui.adapters.CategoryLongClickInterface
import com.a.autoreminderapp.ui.adapters.CategoryRVAdapter
import com.a.autoreminderapp.ui.viewmodels.CategoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Фрагмент отвечающий вывод списка категорий
// реализует интерфейс CategoryClickInterface для обработки нажатий на элементы списка RecyclerView с категориями
// реализует интерфейс CategoryLongClickInterface для обработки длинных нажатий на элементы списка RecyclerView с категориями
class CategoriesFragment : Fragment(), CategoryClickInterface, CategoryLongClickInterface {

    lateinit var vmCategory: CategoryViewModel
    lateinit var rvCategories: RecyclerView
    lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCategories = view.findViewById(R.id.rvCategories)
        fabAdd = view.findViewById(R.id.fabAddCategory)

        rvCategories.layoutManager = LinearLayoutManager(context)
        val categoryRVAdapter = context?.let { CategoryRVAdapter(it, this, this) }
        rvCategories.adapter = categoryRVAdapter

        vmCategory = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(CategoryViewModel::class.java)

        vmCategory.allCategories.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                if (categoryRVAdapter != null){
                    categoryRVAdapter.updateList(it)
                }
            }
        })

        fabAdd.setOnClickListener{
            val intent = Intent(context, AddEditCategoryActivity::class.java)
            startActivity(intent)
        }
    }

    // перегруженная функция для обработки нажатий на объекты RecyclerView
    override fun onClick(category: Category) {
        val intent = Intent(context, CategoryPartsActivity::class.java)
        intent.putExtra("id", category.id)
        intent.putExtra("name", category.name)
        startActivity(intent)
    }

    // перегруженная функция для обработки длинных нажатий на объекты RecyclerView
    override fun onLongClick(category: Category) {
        val intent = Intent(context, AddEditCategoryActivity::class.java)
        intent.putExtra("action", "edit")
        intent.putExtra("id", category.id)
        intent.putExtra("name", category.name)
        startActivity(intent)
    }
}
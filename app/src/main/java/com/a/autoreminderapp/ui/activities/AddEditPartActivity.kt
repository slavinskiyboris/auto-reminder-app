package com.a.autoreminderapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a.autoreminderapp.R
import com.a.autoreminderapp.data.entities.Category
import com.a.autoreminderapp.data.entities.Part
import com.a.autoreminderapp.ui.adapters.*
import com.a.autoreminderapp.ui.viewmodels.CategoryViewModel
import com.a.autoreminderapp.ui.viewmodels.PartViewModelFactory
import com.a.autoreminderapp.ui.viewmodels.PartViewModel

// активити для создания и редактирования запчасти
class AddEditPartActivity : AppCompatActivity(), CategoryClickInterface, CategoryLongClickInterface {

    lateinit var tvCategoryName: TextView
    lateinit var rvCategories: RecyclerView
    lateinit var etPartName: EditText
    lateinit var etMaxServiceLife: EditText
    lateinit var etCurrentServiceLife: EditText
    lateinit var etBrand: EditText
    lateinit var etDescription: EditText
    lateinit var btnSave: Button

    lateinit var vmCategory: CategoryViewModel
    lateinit var vmPart: PartViewModel

    var categoryId = -1
    var partId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_part)

        tvCategoryName = findViewById(R.id.tvCategoryName)
        rvCategories = findViewById(R.id.rvCategories)
        etPartName = findViewById(R.id.etPartName)
        etMaxServiceLife = findViewById(R.id.etMaxServiceLife)
        etCurrentServiceLife = findViewById(R.id.etCurrentServiceLife)
        etBrand = findViewById(R.id.etBrand)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSave)
        rvCategories = findViewById(R.id.rvCategories)

        rvCategories.layoutManager = LinearLayoutManager(this)
        val categoryRVAdapter = CategoryRVAdapter(this, this, this)
        rvCategories.adapter = categoryRVAdapter

        vmCategory = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CategoryViewModel::class.java)

        vmCategory.allCategories.observe(this, Observer { list ->
            list?.let {
                if (categoryRVAdapter != null){
                    categoryRVAdapter.updateList(it)
                }
            }
        })

        val vmPart: PartViewModel by viewModels {
            PartViewModelFactory(application, categoryId)
        }

        val action = intent.getStringExtra("action")
        if (action.equals("edit")){
            title = "Изменение детали"
            partId = intent.getIntExtra("id", 0)
            categoryId = intent.getIntExtra("category", 0)
            val categoryName = intent.getStringExtra("categoryName")
            val name = intent.getStringExtra("name")
            val maxServiceLife = intent.getIntExtra("maxServiceLife", 0)
            val currentServiceLife = intent.getIntExtra("currentServiceLife", 0)
            val brand = intent.getStringExtra("brand")
            val description = intent.getStringExtra("description")
            tvCategoryName.text = categoryName
            etPartName.setText(name)
            etMaxServiceLife.setText(maxServiceLife.toString())
            etCurrentServiceLife.setText(currentServiceLife.toString())
            etBrand.setText(brand)
            etDescription.setText(description)
        } else {
            title = "Добавление детали"
            categoryId = intent.getIntExtra("category", 0)
            val categoryName = intent.getStringExtra("categoryName")
            tvCategoryName.text = categoryName
            btnSave.text = "Сохранить"
        }

        btnSave.setOnClickListener{
            try {
                // проверка заполненности полей при нажатии на кнопку сохранить
                if (categoryId == -1){
                    throw java.lang.IllegalArgumentException("Необходимо выбрать категорию!")
                }
                if (etPartName.text.toString() == ""){
                    throw java.lang.IllegalArgumentException("Не указано название детали!")
                }
                if (etMaxServiceLife.text.toString() == ""){
                    throw java.lang.IllegalArgumentException("Не указан срок максимальной эксплуатации!")
                }
                if (etCurrentServiceLife.text.toString() == ""){
                    throw java.lang.IllegalArgumentException("Не указан срок текущей эксплуатации!")
                }
                if (etBrand.text.toString() == ""){
                    throw java.lang.IllegalArgumentException("Не указана марка!")
                }
                if (etDescription.text.toString() == ""){
                    throw java.lang.IllegalArgumentException("Не введено краткое описание!")
                }

                val name = etPartName.text.toString()
                val maxServiceLife = etMaxServiceLife.text.toString().toInt()
                val currentServiceLife = etCurrentServiceLife.text.toString().toInt()
                val brand = etBrand.text.toString()
                val description = etDescription.text.toString()

                if (action.equals("edit")) {
                    val updatedPart = Part(categoryId, name, maxServiceLife, currentServiceLife, brand, description)
                    updatedPart.id = partId
                    vmPart.updatePart(updatedPart)
                    Toast.makeText(this, "Деталь '$name' обновлена", Toast.LENGTH_LONG).show()
                } else {
                    vmPart.addPart(Part(categoryId, name, maxServiceLife, currentServiceLife, brand, description))
                    Toast.makeText(this, "Деталь '$name' добавлена", Toast.LENGTH_LONG).show()
                }
                val intent = Intent(this@AddEditPartActivity, CategoryPartsActivity::class.java)
                intent.putExtra("id", categoryId)
                intent.putExtra("name", tvCategoryName.text.toString())
                startActivity(intent)
                finish()
            } catch (e: java.lang.Exception){
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(category: Category) {
        categoryId = category.id
        tvCategoryName.text = category.name
    }

    override fun onLongClick(category: Category) {
        categoryId = category.id
        tvCategoryName.text = category.name
    }

}
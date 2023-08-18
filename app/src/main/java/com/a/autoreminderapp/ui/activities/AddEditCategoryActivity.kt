package com.a.autoreminderapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.a.autoreminderapp.R
import com.a.autoreminderapp.data.entities.Category
import com.a.autoreminderapp.ui.viewmodels.CategoryViewModel

// активити для создания и редактирования категории
class AddEditCategoryActivity : AppCompatActivity() {

    lateinit var etCategoryName: EditText
    lateinit var btnSave: Button
    lateinit var btnRemove: Button

    lateinit var vmCategory: CategoryViewModel

    var categoryId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_category)

        etCategoryName = findViewById(R.id.etCategoryName)
        btnSave = findViewById(R.id.btnSave)
        btnRemove = findViewById(R.id.btnRemove)

        vmCategory = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CategoryViewModel::class.java)

        val action = intent.getStringExtra("action")
        if (action.equals("edit")){
            title = "Изменение категории"
            val name = intent.getStringExtra("name")
            categoryId = intent.getIntExtra("id", -1)
            etCategoryName.setText(name)
            btnRemove.visibility = View.VISIBLE
            if (name != null){
                val category: Category = Category(name)
                category.id = categoryId
                btnRemove.setOnClickListener{
                    vmCategory.deleteCategory(category)
                    Toast.makeText(this, "Категория '$name' удалена", Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            }
        } else {
            title = "Добавление категории"
            btnSave.text = "Сохранить"
        }


        btnSave.setOnClickListener{
            try {
                // проверка заполненности полей при нажатии на кнопку сохранить
                if (etCategoryName.text.toString() == ""){
                    Toast.makeText(this, "Не указано название категории!", Toast.LENGTH_SHORT).show()
                    throw java.lang.IllegalArgumentException("Не указано название категории!")
                }
                val name = etCategoryName.text.toString()

                if (action.equals("edit")) {
                    val updatedCategory = Category(name)
                    updatedCategory.id = categoryId
                    vmCategory.updateCategory(updatedCategory)
                    Toast.makeText(this, "Категория обновлена", Toast.LENGTH_LONG).show()
                } else {
                    vmCategory.addCategory(Category(name))
                    Toast.makeText(this, "Категория $name добавлена", Toast.LENGTH_LONG).show()
                }
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } catch (e: java.lang.Exception){
                Log.d("Exception", e.message.toString())
            }
        }


    }
}
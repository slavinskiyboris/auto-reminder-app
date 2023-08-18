package com.a.autoreminderapp.data.repositories

import androidx.lifecycle.LiveData
import com.a.autoreminderapp.data.entities.Category
import com.a.autoreminderapp.data.dao.CategoryDao


// класс-репозиторий для выполнения операций над объектами класса Category (категория)
// в источнике данных
class CategoryRepository(private val categoriesDao: CategoryDao) {
    // все категории для Observer
    val allCategories: LiveData<List<Category>> = categoriesDao.getAll()

    // подвешенная функция вставки объекта
    suspend fun insert(category: Category){
        categoriesDao.insert(category)
    }

    // подвешенная функция обновления объекта
    suspend fun update(category: Category){
        categoriesDao.update(category)
    }

    // подвешенная функция удаления объекта
    suspend fun delete(category: Category){
        categoriesDao.delete(category)
    }
}
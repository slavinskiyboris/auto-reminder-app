package com.a.autoreminderapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.a.autoreminderapp.data.AppDatabase
import com.a.autoreminderapp.data.entities.Category
import com.a.autoreminderapp.data.repositories.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// связующее звено между объектами классов сущностей (Категория) и представлением
// Обеспечивает потоки данных для представления
class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    val allCategories: LiveData<List<Category>>
    val repository: CategoryRepository

    // инициализация ViewModel
    init {
        val dao = AppDatabase.getDatabase(application).getCategoryDao()
        repository = CategoryRepository(dao)
        allCategories = repository.allCategories
    }

    // добавление объекта внутри viewModelScope
    fun addCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(category)
    }

    // обновление объекта внутри viewModelScope
    fun updateCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(category)
    }

    // удаление объекта внутри viewModelScope
    fun deleteCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(category)
    }

}
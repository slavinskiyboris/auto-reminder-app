package com.a.autoreminderapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.a.autoreminderapp.data.AppDatabase
import com.a.autoreminderapp.data.entities.Part
import com.a.autoreminderapp.data.repositories.PartRepository
import com.a.autoreminderapp.notifications.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// связующее звено между объектами классов сущностей (Запчасть) и представлением
// Обеспечивает потоки данных для представления
class PartViewModel(application: Application, val categoryId: Int) :
    AndroidViewModel(application) {
    val allParts: LiveData<List<Part>>
    val repository: PartRepository

    // инициализация ViewModel
    init {
        val dao = AppDatabase.getDatabase(application).getPartDao()
        repository = PartRepository(dao, categoryId)
        allParts = repository.allParts
    }

    // добавление объекта внутри viewModelScope
    fun addPart(part: Part) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(part)
    }

    // обновление объекта внутри viewModelScope
    fun updatePart(part: Part) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(part)
    }

    // удаление объекта внутри viewModelScope
    fun deletePart(part: Part) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(part)
    }

    // обновление списка объектов запчастей внутри viewModelScope (новый пробег)
    fun updatePartsByMileage(previousMileage: Int, newMileage: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePartsByMileage(previousMileage, newMileage, getApplication())
        }
}
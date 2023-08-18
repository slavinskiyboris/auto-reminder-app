package com.a.autoreminderapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.a.autoreminderapp.data.AppDatabase
import com.a.autoreminderapp.data.entities.AutoEvent
import com.a.autoreminderapp.data.repositories.AutoEventRepository
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// связующее звено между объектами классов сущностей (Событие) и представлением
// Обеспечивает потоки данных для представления
class AutoEventViewModel(application: Application, val date: String) : AndroidViewModel(application) {
    val allAutoEvents: LiveData<List<AutoEvent>>
    val repository: AutoEventRepository

    // инициализация ViewModel
    init {
        val dao = AppDatabase.getDatabase(application).getAutoEventDao()
        repository = AutoEventRepository(dao, date)
        allAutoEvents = repository.allAutoEvents
    }

    // добавление объекта внутри viewModelScope
    fun addAutoEvent(autoEvent: AutoEvent) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(autoEvent)
    }

    // обновление объекта внутри viewModelScope
    fun updateAutoEvent(autoEvent: AutoEvent) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(autoEvent)
    }

    // удаление объекта внутри viewModelScope
    fun deleteAutoEvent(autoEvent: AutoEvent) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(autoEvent)
    }
}
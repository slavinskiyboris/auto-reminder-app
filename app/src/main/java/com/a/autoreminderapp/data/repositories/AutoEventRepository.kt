package com.a.autoreminderapp.data.repositories

import androidx.lifecycle.LiveData
import com.a.autoreminderapp.data.entities.AutoEvent
import com.a.autoreminderapp.data.dao.AutoEventDao

// класс-репозиторий для выполнения операций над объектами класса AutoEvent (событие)
// в источнике данных
class AutoEventRepository(private val autoEventDao: AutoEventDao, val date: String) {
    // все события для Observer
    val allAutoEvents: LiveData<List<AutoEvent>> = if (date == "") {
        autoEventDao.getAll()
    } else {
        autoEventDao.getAllByDate(date)
    }

    // подвешенная функция вставки объекта
    suspend fun insert(autoEvent: AutoEvent) {
        autoEventDao.insert(autoEvent)
    }

    // подвешенная функция обновления объекта
    suspend fun update(autoEvent: AutoEvent) {
        autoEventDao.update(autoEvent)
    }

    // подвешенная функция удаления объекта
    suspend fun delete(autoEvent: AutoEvent) {
        autoEventDao.delete(autoEvent)
    }
}
package com.a.autoreminderapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.a.autoreminderapp.data.entities.AutoEvent

// Dao интерфейс для выполнения операций над объектами класса AutoEvent (событие) в БД
@Dao
interface AutoEventDao {

    // вставка списка объектов
    @Insert
    fun insertList(data: List<AutoEvent>)

    // вставка объекта
    @Insert
    suspend fun insert(autoEvent: AutoEvent)

    // обновление объекта
    @Update
    suspend fun update(autoEvent: AutoEvent)

    // удаление объекта
    @Delete
    suspend fun delete(autoEvent: AutoEvent)

    // получение списка объектов для Observer отсортированных по созданию
    @Query("Select * from autoEventsTable order by id ASC")
    fun getAll(): LiveData<List<AutoEvent>>

    // получение списка объектов для Observer отсортированных по созданию и отфильтрованных по дате
    @Query("Select * from autoEventsTable where date = :date order by id ASC")
    fun getAllByDate(date: String): LiveData<List<AutoEvent>>
}
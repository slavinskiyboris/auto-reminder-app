package com.a.autoreminderapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.a.autoreminderapp.data.entities.Part

// Dao интерфейс для выполнения операций над объектами класса Part (запчасть) в БД
@Dao
interface PartDao {

    // вставка списка объектов
    @Insert
    fun insertList(data: List<Part>)

    // вставка объекта
    @Insert
    suspend fun insert(part: Part)

    // обновление объекта
    @Update
    suspend fun update(part: Part)

    // удаление объекта
    @Delete
    suspend fun delete(part: Part)

    // получение списка объектов для Observer отсортированных по созданию
    @Query("Select * from partsTable order by id ASC")
    fun getAll(): LiveData<List<Part>>


    // получение списка объектов  отсортированных по созданию
    @Query("Select * from partsTable order by id ASC")
    fun getPartsList(): List<Part>

    // получение списка объектов для Observer отсортированных по созданию и отфильтрованных по категории
    @Query("Select * from partsTable where category = :categoryId order by id ASC")
    fun getAllByCategory(categoryId: Int): LiveData<List<Part>>
}
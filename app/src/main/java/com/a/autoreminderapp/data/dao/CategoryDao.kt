package com.a.autoreminderapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.a.autoreminderapp.data.entities.Category

// Dao интерфейс для выполнения операций над объектами класса Category (категория) в БД
@Dao
interface CategoryDao {

    // вставка списка объектов
    @Insert
    fun insertList(data: List<Category>)

    // вставка объекта
    @Insert
    suspend fun insert(category: Category)

    // обновление объекта
    @Update
    suspend fun update(category: Category)

    // удаление объекта
    @Delete
    suspend fun delete(category: Category)

    // получение списка объектов для Observer отсортированных по созданию
    @Query("Select * from categoriesTable order by id ASC")
    fun getAll(): LiveData<List<Category>>
}
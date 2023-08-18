package com.a.autoreminderapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// класс-сущность (модель) описывающий Категорию
@Entity(tableName = "categoriesTable")
class Category(
    @ColumnInfo(name = "name") var name: String,
    ) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
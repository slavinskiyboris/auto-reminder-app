package com.a.autoreminderapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// класс-сущность (модель) описывающий событие
@Entity(tableName = "autoEventsTable")
class AutoEvent(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "time") var time: String,
    ) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
package com.a.autoreminderapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

// класс-сущность (модель) описывающий запчасть
@Entity(
    tableName = "partsTable",
    foreignKeys = [
        ForeignKey(
            onDelete = CASCADE,
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category"]
        )]
)
class Part(
    @ColumnInfo(name = "category") val category: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "maxServiceLife") var maxServiceLife: Int,
    @ColumnInfo(name = "currentServiceLife") var currentServiceLife: Int,
    @ColumnInfo(name = "brand") var brand: String,
    @ColumnInfo(name = "description") var description: String,
    ) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
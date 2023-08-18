package com.a.autoreminderapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.a.autoreminderapp.data.dao.AutoEventDao
import com.a.autoreminderapp.data.dao.CategoryDao
import com.a.autoreminderapp.data.dao.PartDao
import com.a.autoreminderapp.data.entities.AutoEvent
import com.a.autoreminderapp.data.entities.Category
import com.a.autoreminderapp.data.entities.Part
import com.a.autoreminderapp.utils.ioThread

// абстрактный класс для работы с Room БД приложения, на вход получает классы сущностей БД
@Database(entities = [Category::class, Part::class, AutoEvent::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    // абстрактные сущности для работы с объектами при помощи Dao интерфейсов
    abstract fun getAutoEventDao(): AutoEventDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getPartDao(): PartDao

    // объект компаньон
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        // функция для получения экземпляра БД
        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        // функция создания БД
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "autoreminder_database")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        ioThread {
                            getDatabase(context).getCategoryDao().insertList(PREPOPULATE_CATEGORIES)
//                            getDatabase(context).getPartDao().insertList(PREPOPULATE_PARTS)
//                            getDatabase(context).getAutoEventDao().insertList(PREPOPULATE_AUTO_EVENTS)
                        }
                    }
                })
                .build()

        // данные подготовленные для загрузки в БД при первом запуске приложения
        val PREPOPULATE_CATEGORIES = listOf(
            Category("Двигатель"),
            Category("Топливная система"),
            Category("Тормозная система"),
            Category("Подвеска"),
            Category("Салон"),
        )
//        val PREPOPULATE_PARTS = listOf()
//        val PREPOPULATE_AUTO_EVENTS = listOf()

    }
}
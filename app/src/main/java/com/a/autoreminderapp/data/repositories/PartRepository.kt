package com.a.autoreminderapp.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.a.autoreminderapp.data.entities.Part
import com.a.autoreminderapp.data.dao.PartDao
import com.a.autoreminderapp.notifications.NotificationHelper

// класс-репозиторий для выполнения операций над объектами класса Part (запчасть)
// в источнике данных
class PartRepository(private val partDao: PartDao, val categoryId: Int) {
    // все запчасти для Observer
    val allParts: LiveData<List<Part>> = if (categoryId == -1){
        partDao.getAll()
    } else {
        partDao.getAllByCategory(categoryId)
    }

    // подвешенная функция вставки объекта
    suspend fun insert(part: Part){
        partDao.insert(part)
    }

    // подвешенная функция обновления объекта
    suspend fun update(part: Part){
        partDao.update(part)
    }

    // подвешенная функция удаления объекта
    suspend fun delete(part: Part){
        partDao.delete(part)
    }

    // подвешенная функция обновления пробега в запчастях по пробегу в автомобиле
    suspend fun updatePartsByMileage(previousMileage: Int, newMileage: Int, context: Context){
        for (part: Part in partDao.getPartsList()) {
            part.currentServiceLife = newMileage - previousMileage + part.currentServiceLife
            partDao.update(part)
            // Если текущий пробег превышает определенное значение, создаем уведомление
            if (part.currentServiceLife > part.maxServiceLife){
                val text = "Превышен срок максимльной эксплуатации детали, немедленно замените"
                NotificationHelper.makeMileageNotification(context, part, text)
            }
            else if (part.currentServiceLife.toFloat() / part.maxServiceLife.toFloat() >= 0.9){
                val text = "Износ детали >= 90%, в скором времени потребуется замена"
                NotificationHelper.makeMileageNotification(context, part, text)
            }
            else if (part.currentServiceLife.toFloat() / part.maxServiceLife.toFloat() >= 0.8){
                val text = "Износ детали >= 80%, в скором времени потребуется замена"
                NotificationHelper.makeMileageNotification(context, part, text)
            }
            else if (part.currentServiceLife.toFloat() / part.maxServiceLife.toFloat() >= 0.75){
                val text = "Износ детали >= 75%, в скором времени потребуется замена"
                NotificationHelper.makeMileageNotification(context, part, text)
            }
            else if (part.currentServiceLife.toFloat() / part.maxServiceLife.toFloat() >= 0.6){
                val text = "Износ детали >= 60%, в скором времени потребуется замена"
                NotificationHelper.makeMileageNotification(context, part, text)
            }

        }
    }
}
package com.a.autoreminderapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// класс-фабрика для того чтобы создавать объекты PartViewModel с дополнительными параметрами
class PartViewModelFactory(
    private val application: Application,
    private val categoryId: Int
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PartViewModel(application, categoryId) as T
}
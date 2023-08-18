package com.a.autoreminderapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// класс-фабрика для того чтобы создавать объекты AutoEventViewModel с дополнительными параметрами
class AutoEventViewModelFactory(
    private val application: Application,
    private val date: String
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AutoEventViewModel(application, date) as T
}
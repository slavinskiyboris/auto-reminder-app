package com.a.autoreminderapp.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.a.autoreminderapp.R
import com.a.autoreminderapp.alarms.AlarmHelper
import com.a.autoreminderapp.ui.fragments.CategoriesFragment
import com.a.autoreminderapp.ui.fragments.EventsFragment
import com.a.autoreminderapp.ui.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

// главное активити приложения, включает фрагмент содержащий нижнюю навигацию и
// основной загружаемый фрагмент который содержит на выбор пользователя: Категории, События, Настройки
class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        loadFragment(CategoriesFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bmCategories -> {
                    loadFragment(CategoriesFragment())
                    title = "Категории"
                    true
                }
                R.id.bmCalendar -> {
                    loadFragment(EventsFragment())
                    title = "События"
                    true
                }
                R.id.bmSettings -> {
                    title = "Настройки"
                    loadFragment(SettingsFragment())
                    true
                }
                else -> throw AssertionError()
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}
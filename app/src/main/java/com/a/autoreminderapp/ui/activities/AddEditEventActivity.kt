package com.a.autoreminderapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.a.autoreminderapp.R
import com.a.autoreminderapp.alarms.AlarmHelper
import com.a.autoreminderapp.data.entities.AutoEvent
import com.a.autoreminderapp.ui.viewmodels.AutoEventViewModel
import com.a.autoreminderapp.ui.viewmodels.AutoEventViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

// активити для создания и редактирования события
class AddEditEventActivity : AppCompatActivity() {

    lateinit var etEventName: EditText
    lateinit var etDescription: EditText
    lateinit var tpTime: TimePicker
    lateinit var btnSave: Button

    var autoEventId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_event)

        etEventName = findViewById(R.id.etEventName)
        etDescription = findViewById(R.id.etDescription)
        tpTime = findViewById(R.id.tpTime)
        tpTime.setIs24HourView(true)
        btnSave = findViewById(R.id.btnSave)

        val dateStr = intent.getStringExtra("date")
        if (dateStr != null){
            val vmAutoEvent: AutoEventViewModel by viewModels {
                AutoEventViewModelFactory(application, dateStr)
            }
            val action = intent.getStringExtra("action")
            if (action.equals("edit")){
                title = "Изменение события"
                autoEventId = intent.getIntExtra("id", -1)
                val name = intent.getStringExtra("name")
                val timeStr = intent.getStringExtra("time")
                val description = intent.getStringExtra("description")
                etEventName.setText(name)
                etDescription.setText(description)

                val sdf = SimpleDateFormat("hh:mm", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.time = sdf.parse(timeStr)
                tpTime.hour = calendar.get(Calendar.HOUR_OF_DAY)
                tpTime.minute = calendar.get(Calendar.MINUTE)
            } else {
                title = "Добавление события"
                btnSave.text = "Сохранить"
            }


            btnSave.setOnClickListener{
                try {
                    // проверка заполненности полей при нажатии на кнопку сохранить
                    if (etEventName.text.toString() == ""){
                        throw java.lang.IllegalArgumentException("Необходимо указать название события!")
                    }
                    if (etDescription.text.toString() == ""){
                        throw java.lang.IllegalArgumentException("Необходимо указать краткое описание события!")
                    }
                    val name = etEventName.text.toString()
                    val description = etDescription.text.toString()
                    val hourStr = if (tpTime.hour.toString().length == 1){
                        "0" + tpTime.hour.toString()
                    } else {
                        tpTime.hour.toString()
                    }
                    val minuteStr = if (tpTime.minute.toString().length == 1){
                        "0" + tpTime.minute.toString()
                    } else {
                        tpTime.minute.toString()
                    }
                    val timeStr = "$hourStr:$minuteStr"

                    if (action.equals("edit")) {
                        val updatedAutoEvent = AutoEvent(name, description, dateStr, timeStr)
                        updatedAutoEvent.id = autoEventId
                        vmAutoEvent.updateAutoEvent(updatedAutoEvent)
                        Toast.makeText(this, "Событие '$name' обновлено", Toast.LENGTH_LONG).show()
                        AlarmHelper.makeAlarmsForEvent(this, updatedAutoEvent)
                    } else {
                        val autoEvent: AutoEvent = AutoEvent(name, description, dateStr, timeStr)
                        vmAutoEvent.addAutoEvent(autoEvent)
                        Toast.makeText(this, "Событие '$name' добавлено", Toast.LENGTH_LONG).show()
                        AlarmHelper.makeAlarmsForEvent(this, autoEvent)
                    }

                    val intent = Intent(this@AddEditEventActivity, DateEventsActivity::class.java)
                    intent.putExtra("dateTime", dateStr)
                    startActivity(intent)
                    finish()
                } catch (e: java.lang.Exception){
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
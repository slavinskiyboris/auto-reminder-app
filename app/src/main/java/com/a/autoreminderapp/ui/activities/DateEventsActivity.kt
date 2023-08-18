package com.a.autoreminderapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a.autoreminderapp.R
import com.a.autoreminderapp.alarms.AlarmHelper
import com.a.autoreminderapp.data.entities.AutoEvent
import com.a.autoreminderapp.ui.adapters.AutoEventClickInterface
import com.a.autoreminderapp.ui.adapters.AutoEventLongClickInterface
import com.a.autoreminderapp.ui.adapters.AutoEventRVAdapter
import com.a.autoreminderapp.ui.viewmodels.AutoEventViewModel
import com.a.autoreminderapp.ui.viewmodels.AutoEventViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*


// активити списка событий для даты
class DateEventsActivity : AppCompatActivity(), AutoEventClickInterface,
    AutoEventLongClickInterface {

    lateinit var fabAdd: FloatingActionButton
    lateinit var rvAutoEvents: RecyclerView
    lateinit var vmAutoEvent: AutoEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_events)

        fabAdd = findViewById(R.id.fabAddAutoEvent)
        rvAutoEvents = findViewById(R.id.rvEvents)

        val sdfSource = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfTitle = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        var dateTimeStr = intent.getStringExtra("dateTime")
        val dateTime = sdfSource.parse(dateTimeStr)

        title = "События на " + sdfTitle.format(dateTime)


        val autoEventRVAdapter = AutoEventRVAdapter(this, this, this)
        rvAutoEvents.layoutManager = LinearLayoutManager(this)
        rvAutoEvents.adapter = autoEventRVAdapter

        if (dateTimeStr == null){
            dateTimeStr = "";
        }
        vmAutoEvent = ViewModelProvider(
            this,
            AutoEventViewModelFactory(application, dateTimeStr)
        ).get(AutoEventViewModel::class.java)

        vmAutoEvent.allAutoEvents.observe(this, androidx.lifecycle.Observer { list ->
            list?.let {
                if (autoEventRVAdapter != null) {
                    autoEventRVAdapter.updateList(it)
                }
            }
        })

        fabAdd.setOnClickListener{
            val intent = Intent(this@DateEventsActivity, AddEditEventActivity::class.java)
            intent.putExtra("date", dateTimeStr)
            startActivity(intent)
        }
    }

    override fun onClick(autoEvent: AutoEvent) {
        val intent = Intent(this@DateEventsActivity, AddEditEventActivity::class.java)
        intent.putExtra("action", "edit")
        intent.putExtra("id", autoEvent.id)
        intent.putExtra("name", autoEvent.name)
        intent.putExtra("date", autoEvent.date)
        intent.putExtra("time", autoEvent.time)
        intent.putExtra("description", autoEvent.description)
        startActivity(intent)

    }

    override fun onLongClick(autoEvent: AutoEvent) {
        vmAutoEvent.deleteAutoEvent(autoEvent)
        AlarmHelper.removeAlarmsForEvent(this, autoEvent)
        Toast.makeText(this, "Событие '${autoEvent.name}' удалено", Toast.LENGTH_LONG).show()
    }
}
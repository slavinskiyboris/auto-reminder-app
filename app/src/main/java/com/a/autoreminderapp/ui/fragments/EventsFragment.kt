package com.a.autoreminderapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.a.autoreminderapp.ui.activities.DateEventsActivity
import com.a.autoreminderapp.R
import com.a.autoreminderapp.data.entities.AutoEvent
import com.a.autoreminderapp.ui.viewmodels.AutoEventViewModel
import com.a.autoreminderapp.ui.viewmodels.AutoEventViewModelFactory
import com.a.autoreminderapp.ui.viewmodels.CategoryViewModel
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import java.text.SimpleDateFormat
import java.util.*


// Фрагмент отвечающий за вывод календаря с днями событий
class EventsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        val vmAutoEvent: AutoEventViewModel by viewModels {
            AutoEventViewModelFactory(requireActivity().application, "")
        }

        val eventDayList: MutableList<EventDay> = mutableListOf<EventDay>()

        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())


        vmAutoEvent.allAutoEvents.observe(viewLifecycleOwner, Observer { list ->
            for (autoEvent: AutoEvent in list) {
                val calendar = Calendar.getInstance()
                calendar.time = sdf.parse(autoEvent.date + " " + autoEvent.time)
                eventDayList += EventDay(calendar, R.drawable.ic_repair)
            }
            calendarView.setEvents(eventDayList)
        })

        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
//                Toast.makeText(requireContext(), eventDay.calendar.time.toString(), Toast.LENGTH_SHORT).show()
                val intent = Intent(context, DateEventsActivity::class.java)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                intent.putExtra("dateTime", sdf.format(eventDay.calendar.time))
                startActivity(intent)
            }
        })
    }
}
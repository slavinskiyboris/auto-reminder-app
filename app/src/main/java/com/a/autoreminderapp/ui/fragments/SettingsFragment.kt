package com.a.autoreminderapp.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.a.autoreminderapp.R
import com.a.autoreminderapp.alarms.AlarmHelper
import com.a.autoreminderapp.ui.viewmodels.PartViewModel
import com.a.autoreminderapp.ui.viewmodels.PartViewModelFactory

// Фрагмент настроек, отвечает за хранение интервала между оповещениями о обновлении пробега и
// за хранение значения пробега
class SettingsFragment : PreferenceFragmentCompat() {

    val vmPart: PartViewModel by viewModels {
        PartViewModelFactory(requireActivity().application, -1)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val etPreferInterval : EditTextPreference? = findPreference("interval")
        val etPreferMileage: EditTextPreference? = findPreference("mileage")

        etPreferInterval?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        etPreferMileage?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        if (etPreferInterval != null) {
            etPreferInterval.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                val previousMileageStr = etPreferMileage?.text.toString()
                val previousMileage: Int = if (previousMileageStr != ""){
                    previousMileageStr.toInt()
                } else {
                    0
                }
                if (newValue.toString() != ""){
                    val interval = newValue.toString().toInt()
                    if (interval > 0){
                        AlarmHelper.makeAlarmForMileageRemind(requireContext(), interval)
                        Toast.makeText(requireContext(), "Интервал обновления пробега изменен!", Toast.LENGTH_SHORT).show()
                    }
                    else if (interval == 0){
                        AlarmHelper.removeAlarmForMileageRemind(requireContext())
                        Toast.makeText(requireContext(), "Напоминания о обновлении пробега отключены!", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }

        if (etPreferMileage != null){
            etPreferMileage.onPreferenceChangeListener = Preference.OnPreferenceChangeListener{ preference, newValue ->
                val previousMileage: Int = if (etPreferMileage.text.toString() != ""){
                    etPreferMileage.text.toString().toInt()
                } else {
                    0
                }
                val newMileage: Int = if (newValue.toString() != ""){
                    newValue.toString().toInt()
                } else {
                    0
                }
                vmPart.updatePartsByMileage(previousMileage, newMileage)
                Toast.makeText(requireContext(), "Пробег изменен!", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
}
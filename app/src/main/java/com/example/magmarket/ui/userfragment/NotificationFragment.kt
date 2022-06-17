package com.example.magmarket.ui.userfragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.magmarket.R
import com.example.magmarket.application.Constants
import com.example.magmarket.databinding.FragmentNotificationBinding
import com.example.magmarket.worker.NotificationWorker
import java.util.concurrent.TimeUnit

class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private var timePicker = 0
    private var custom = false
    private lateinit var sharePreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationBinding.bind(view)
        close()
        setTime()
        submit()


    }

    private fun setTime() = with(binding) {
        sharePreferences =
            requireContext().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        editor = sharePreferences.edit()
        val time = sharePreferences.getInt("time", 6)

        edCustomTime.setText(sharePreferences.getInt("custom", 0).toString())
        when (time) {
            0 -> offNotif.isChecked = true
            1 -> threeHours.isChecked = true
            2 -> fiveHours.isChecked = true
            3 -> eightHours.isChecked = true
            4 -> twelveHours.isChecked = true

        }

        notifRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.off_notif -> {

                    editor.putInt("time", 0)
                    timePicker = 0
                }
                R.id.three_hours -> {

                    editor.putInt("time", 1)
                    timePicker = 3
                }
                R.id.five_hours -> {
                    editor.putInt("time", 2)
                    timePicker = 5
                }
                R.id.eight_hours -> {
                    editor.putInt("time", 3)
                    timePicker = 8
                }
                R.id.twelve_hours -> {
                    editor.putInt("time", 4)
                    timePicker = 12
                }
                R.id.custom_notif -> {
                    editor.putInt("time", 5)
                    custom = true


                }
            }
            editor.apply()
        }

    }

    private fun submit() = with(binding) {
        binding.submitNotifi.setOnClickListener {
            Log.d("timepicker", "submit: $custom")
            if (custom) {
                timePicker = edCustomTime.text.toString().toInt()
                if (timePicker >= 15) {
                    editor.putInt("custom", timePicker)

                    Log.d("timepicker", "submit: $timePicker")
                    setNotification(timePicker, TimeUnit.MINUTES)
                    Toast.makeText(requireContext(), "نوتیفیکیشن تنظیم شد.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    edCustomTime.error = "TIME MUST BE MORE THAN 15 MIN"
                }


            } else {
                if (timePicker != 0) {
                    Log.d("timepicker", "submit: $timePicker")
                    setNotification(timePicker, TimeUnit.HOURS)
                    Toast.makeText(requireContext(), "نوتیفیکیشن تنظیم شد.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }

    }

    private fun setNotification(time: Int, timeUnit: TimeUnit) {

        val workManager = WorkManager.getInstance(requireContext())
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val work = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = time.toLong(),
            repeatIntervalTimeUnit = timeUnit
        ).addTag(Constants.TAG)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            Constants.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            work
        )
    }

    private fun close() {
        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
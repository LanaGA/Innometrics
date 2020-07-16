package com.rsf.innometrics

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
              return inflater.inflate(R.layout.fragment_settings, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email_info.text = arguments?.getString("login")
        startServiceButton.setOnClickListener {
            val startServiceIntent = Intent(
                    requireActivity(),
                    BackgroundService::class.java
            )
            activity?.startService(startServiceIntent)
        }
        stopServiceButton.setOnClickListener {
            val stopServiceIntent = Intent(
                    requireActivity(),
                    BackgroundService::class.java
            )
            stopServiceIntent.putExtra("collecting_interval", collecting_interval.text)
            stopServiceIntent.putExtra("sending_interval", sending_interval.text)
            activity?.stopService(stopServiceIntent)
        }
    }

}
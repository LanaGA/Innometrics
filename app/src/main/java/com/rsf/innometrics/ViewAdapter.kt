package com.rsf.innometrics

import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.rsf.innometrics.vo.Stats
import java.text.DateFormat
import java.util.*

class ViewAdapter internal constructor() : RecyclerView.Adapter<ViewAdapter.ViewHolder>() {
    private lateinit var mAppStatsList: List<Stats>

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val appName: TextView = v.findViewById(R.id.textview_app_name)
        val lastTimeUsed: TextView = v.findViewById(R.id.textview_total_time_used)
        val appIcon: ImageView = v.findViewById(R.id.app_icon)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.usage_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (_, name, time) = mAppStatsList[position]
        viewHolder.appName.text = name
        val seconds = (time / 1000).toInt() % 60
        val minutes = (time / (1000 * 60)).toInt() % 60
        val hours = (time / (1000 * 60 * 60)).toInt() % 24
        viewHolder.lastTimeUsed.text = String.format("%02d hours %02d min %02d sec", hours, minutes, seconds)
        viewHolder.appIcon.setImageResource(R.drawable.ic_android_black_24dp)
    }

    override fun getItemCount(): Int {
        return mAppStatsList.size
    }

    fun setCustomUsageStatsList(customUsageStats: List<Stats>) {
        mAppStatsList = customUsageStats
    }
}
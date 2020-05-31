package com.rsf.innometrics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*

class ViewAdapter internal constructor() : RecyclerView.Adapter<ViewAdapter.ViewHolder>() {
    private var mAppStatsList: List<AppStats> = ArrayList()
    private val mDateFormat = DateFormat.getDateTimeInstance()

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val appName: TextView = v.findViewById(R.id.textview_app_name)
        val lastTimeUsed: TextView = v.findViewById(R.id.textview_last_time_used)
        val appIcon: ImageView = v.findViewById(R.id.app_icon)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.usage_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (name, appIcon, time) = mAppStatsList[position]
        viewHolder.appName.text = name
        val seconds = (time / 1000).toInt() % 60
        val minutes = (time / (1000 * 60)).toInt() % 60
        val hours = (time / (1000 * 60 * 60)).toInt() % 24
        viewHolder.lastTimeUsed.text = String.format("%02d hours %02d min %02d sec", hours, minutes, seconds)
        viewHolder.appIcon.setImageDrawable(appIcon)
    }

    override fun getItemCount(): Int {
        return mAppStatsList.size
    }

    fun setCustomUsageStatsList(customUsageStats: List<AppStats>) {
        mAppStatsList = customUsageStats
    }
}
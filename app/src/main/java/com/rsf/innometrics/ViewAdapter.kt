package com.rsf.innometrics

import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.rsf.innometrics.vo.Stats
import java.text.DateFormat
import java.util.*

class ViewAdapter internal constructor() : RecyclerView.Adapter<ViewAdapter.ViewHolder>() {
    private var mAppStatsList: List<Stats> = emptyList()
    private lateinit var activity:FragmentActivity

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
        val (_, name, time_begin, time_end) = mAppStatsList[position]
        val time = (time_end?.minus(time_begin) ?: 0) + 1
        val seconds = (time / 1000).toInt() % 60
        val minutes = (time / (1000 * 60)).toInt() % 60
        val hours = (time / (1000 * 60 * 60)).toInt() % 24
        val label =
            activity.packageManager.getApplicationLabel(
                    activity.packageManager.getApplicationInfo(
                            name, PackageManager.GET_META_DATA))
        val icon = try {
            activity.packageManager.getApplicationIcon(name)
        } catch (e: PackageManager.NameNotFoundException) {
            activity.getDrawable(
                    R.drawable.ic_android_black_24dp)
        }
        viewHolder.appName.text = label
        viewHolder.appIcon.setImageDrawable(icon)
        viewHolder.lastTimeUsed.text = String.format("%02d hours %02d min %02d sec", hours, minutes, seconds)
    }

    override fun getItemCount(): Int {
        return mAppStatsList.size
    }

    fun updateStatsList(customUsageStats: List<Stats>, activity: FragmentActivity) {
        mAppStatsList = customUsageStats
        this.activity = activity
        notifyDataSetChanged()
    }
}
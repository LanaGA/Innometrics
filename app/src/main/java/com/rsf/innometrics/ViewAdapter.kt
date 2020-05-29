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
    private var mMappingAppList: List<MappingApp> = ArrayList()
    private val mDateFormat = DateFormat.getDateTimeInstance()

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val packageName: TextView = v.findViewById(R.id.textview_package_name)
        val lastTimeUsed: TextView = v.findViewById(R.id.textview_last_time_used)
        val appIcon: ImageView = v.findViewById(R.id.app_icon)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.usage_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (usageStats, appIcon) = mMappingAppList[position]
        viewHolder.packageName.text = usageStats.packageName
        viewHolder.lastTimeUsed.text = usageStats.lastTimeUsed.let { mDateFormat.format(Date(it)) }
        viewHolder.appIcon.setImageDrawable(appIcon)
    }

    override fun getItemCount(): Int {
        return mMappingAppList.size
    }

    fun setCustomUsageStatsList(customUsageStats: List<MappingApp>) {
        mMappingAppList = customUsageStats
    }
}
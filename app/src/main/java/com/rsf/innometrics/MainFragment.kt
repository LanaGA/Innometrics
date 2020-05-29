package com.rsf.innometrics

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_app_usage_statistics.*
import java.util.*

class MainFragment : Fragment() {
    private var manager: UsageStatsManager? = null
    private val viewAdapter: ViewAdapter = ViewAdapter()

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        manager = context?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    override fun onDetach() {
        super.onDetach()

        manager = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_usage_statistics, container, false)
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)

        recyclerview_app_usage.run {
            scrollToPosition(0)
            adapter = viewAdapter
        }
        val usageStatsList = usageStatistics
        Collections.sort(usageStatsList, LastTimeLaunchedComparatorDesc())
        updateAppsList(listOf(usageStatsList!![0]))
    }

    private val usageStatistics: List<UsageStats>?
        get() {
            val time = System.currentTimeMillis()
            val appList = (manager ?: return null)
                    .queryUsageStats(
                            UsageStatsManager.INTERVAL_DAILY, time - 10000 * 10000, time)
            if (appList != null && appList.size == 0) {
                Log.i("TAG", "The user may not allow the access to apps usage. ")
                Toast.makeText(activity,
                        getString(R.string.explanation_access_to_appusage_is_not_enabled),
                        Toast.LENGTH_LONG).show()
                button_open_usage_setting.run {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    }
                }
            }
            return appList
        }

    private fun updateAppsList(usageStatsList: List<UsageStats>) {
        val mappingAppList: MutableList<MappingApp> = ArrayList()
        for (i in usageStatsList.indices) {
            val stats = usageStatsList[i]
            val activity = activity ?: return
            val icon = try {
                activity.packageManager.getApplicationIcon(stats.packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                Log.w("TAG", String.format("App Icon is not found for %s",
                        stats.packageName))
                activity.getDrawable(R.drawable.ic_android_black_24dp)
            }
            val mappingApp = MappingApp(stats, icon)
            mappingAppList.add(mappingApp)
        }
        viewAdapter.run {
            setCustomUsageStatsList(mappingAppList)
            notifyDataSetChanged()
        }
        recyclerview_app_usage.scrollToPosition(0)
    }

    private class LastTimeLaunchedComparatorDesc : Comparator<UsageStats> {
        override fun compare(left: UsageStats, right: UsageStats): Int {
            return right.lastTimeUsed.compareTo(left.lastTimeUsed)
        }
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
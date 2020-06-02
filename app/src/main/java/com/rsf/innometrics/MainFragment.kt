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
import com.rsf.innometrics.db.StatsDao
import com.rsf.innometrics.vo.Stats
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_app_usage_statistics.*
import java.util.*
import javax.inject.Inject

class MainFragment : Fragment() {

    private var manager: UsageStatsManager? = null
    private val viewAdapter: ViewAdapter = ViewAdapter()

    @Inject
    lateinit var statsDao: StatsDao

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
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
        update()
    }

    private fun update() {
        val usageStatsList = usageStatistics
        Collections.sort(usageStatsList, LastTimeLaunchedComparatorDesc())
        if (usageStatsList != null) {
            updateAppsList(usageStatsList)
        }
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
        val appStatsList: MutableList<AppStats> = ArrayList()
        for (i in usageStatsList.indices) {
            val stats = usageStatsList[i]
            val activity = activity ?: return

            val totalTimeUsed = stats.totalTimeInForeground
            if(totalTimeUsed == 0L)
                continue

            val name = try {
                activity.packageManager.getApplicationLabel(
                        activity.packageManager.getApplicationInfo(
                                stats.packageName, PackageManager.GET_META_DATA))
            } catch (e: PackageManager.NameNotFoundException) {
                Log.w("TAG", String.format("App Name is not found for %s",
                        stats.packageName))
            }
            val icon = try {
                activity.packageManager.getApplicationIcon(stats.packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                Log.w("TAG", String.format("App Icon is not found for %s",
                        stats.packageName))
                activity.getDrawable(R.drawable.ic_android_black_24dp)
            }
            appStatsList.add(AppStats(name.toString(), icon, totalTimeUsed))
            statsDao.insert(Stats(0, name.toString(), totalTimeUsed, null))
        }
        viewAdapter.run {
            setCustomUsageStatsList(appStatsList)
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
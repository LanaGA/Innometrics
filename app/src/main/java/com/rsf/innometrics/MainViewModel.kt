package com.rsf.innometrics

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.rsf.innometrics.db.StatsDao
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainViewModel : ViewModel() {
    private var manager: UsageStatsManager? = null
    private val viewAdapter: ViewAdapter = ViewAdapter()

    @Inject
    lateinit var statsDao: StatsDao

    internal fun update(context: Context, usageStatsList: List<UsageStats>?, activity: FragmentActivity): List<AppStats>? {
        manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        Collections.sort(usageStatsList, LastTimeLaunchedComparatorDesc())
        if (usageStatsList != null) {
            return updateAppsList(usageStatsList, activity)
        }
        return null
    }


    private fun updateAppsList(usageStatsList: List<UsageStats>, activity: FragmentActivity): List<AppStats> {
        val appStatsList: MutableList<AppStats> = ArrayList()
        for (i in usageStatsList.indices) {
            val stats = usageStatsList[i]

            val totalTimeUsed = stats.totalTimeInForeground
            if (totalTimeUsed == 0L)
                continue

            val name = try {
                activity.packageManager.getApplicationLabel(
                        activity.packageManager.getApplicationInfo(
                                stats.packageName, PackageManager.GET_META_DATA))
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.e(String.format("App Name is not found for %s",
                        stats.packageName))
            }
            val icon = try {
                activity.packageManager.getApplicationIcon(stats.packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.e(String.format("App Icon is not found for %s",
                        stats.packageName))
                activity.getDrawable(R.drawable.ic_android_black_24dp)
            }
            appStatsList.add(AppStats(name.toString(), icon, totalTimeUsed))

        }

        return appStatsList
        //how put it to local db?
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

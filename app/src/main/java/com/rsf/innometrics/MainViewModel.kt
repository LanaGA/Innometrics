package com.rsf.innometrics

import android.app.usage.UsageStats
import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.rsf.innometrics.db.AppDb
import com.rsf.innometrics.vo.Stats
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.Comparator
import kotlin.collections.ArrayList

class MainViewModel @Inject constructor(var db: AppDb) : ViewModel() {


    internal fun update(usageStatsList: List<UsageStats>?, activity: FragmentActivity): List<AppStats>? {
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
                continue
            }
            val icon = try {
                activity.packageManager.getApplicationIcon(stats.packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                activity.getDrawable(
                        R.drawable.ic_android_black_24dp)
            }
            appStatsList.add(AppStats(name.toString(), icon, totalTimeUsed))
            db.statsDao()
                    .insert(Stats(0, name.toString(), totalTimeUsed, null))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
        return appStatsList
        //how put it to local db?
    }

    private class LastTimeLaunchedComparatorDesc : Comparator<UsageStats> {
        override fun compare(left: UsageStats, right: UsageStats): Int {
            return right.lastTimeUsed.compareTo(left.lastTimeUsed)
        }
    }

}

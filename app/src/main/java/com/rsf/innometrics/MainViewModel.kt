package com.rsf.innometrics

import android.app.usage.UsageStats
import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
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


    internal fun update(usageStatsList: List<UsageStats>?, activity: FragmentActivity) {
        if (usageStatsList != null) {
            Collections.sort(usageStatsList, LastTimeLaunchedComparatorDesc())
            updateAppsList(usageStatsList, activity)
        }

    }


    private fun updateAppsList(usageStatsList: List<UsageStats>, activity: FragmentActivity) {
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
          /*  val icon = try {
                activity.packageManager.getApplicationIcon(stats.packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                activity.getDrawable(
                        R.drawable.ic_android_black_24dp)
            }*/
            db.statsDao()
                    .insert(Stats(0, name.toString(), totalTimeUsed))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
    }

    private class LastTimeLaunchedComparatorDesc : Comparator<UsageStats> {
        override fun compare(left: UsageStats, right: UsageStats): Int {
            return right.lastTimeUsed.compareTo(left.lastTimeUsed)
        }
    }

}

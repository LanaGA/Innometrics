package com.rsf.innometrics

import android.app.usage.UsageStats
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.rsf.innometrics.data.db.AppDb
import com.rsf.innometrics.vo.Stats
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(val view: LifecycleOwner, var db: AppDb) : ViewModel() {


    internal fun update(usageStatsList: List<UsageStats>?) {
        if (usageStatsList != null) {
            Collections.sort(usageStatsList, LastTimeLaunchedComparatorDesc())
            updateAppsList(usageStatsList[0])
        }
    }


    private fun updateAppsList(stats: UsageStats) {
        db.statsDao()
                .getLast()
                .observe(view, androidx.lifecycle.Observer {
                    insertOrUpdate(it, stats)
                })
    }

    private fun insertOrUpdate(last: Stats?, stats: UsageStats) {
        val lastTimeUsed = stats.lastTimeUsed
        if (lastTimeUsed == 0L)
            return

        val name = try {
            stats.packageName
        } catch (e: PackageManager.NameNotFoundException) {
            return
        }

        if (last== null || last.app_name != name.toString())
            db.statsDao()
                    .insert(Stats(0, name.toString(), lastTimeUsed, lastTimeUsed))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        else
            db.statsDao()
                    .insert(Stats(last.id, name.toString(), last.time_begin, lastTimeUsed))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

    }

    private class LastTimeLaunchedComparatorDesc : Comparator<UsageStats> {
        override fun compare(left: UsageStats, right: UsageStats): Int {
            return right.lastTimeUsed.compareTo(left.lastTimeUsed)
        }
    }

}

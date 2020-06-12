package com.rsf.innometrics

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.rsf.innometrics.db.AppDb
import com.rsf.innometrics.vo.Stats
import kotlinx.android.synthetic.main.fragment_app_usage_statistics.*
import javax.inject.Inject
import com.rsf.innometrics.MainViewModel as MainViewModel

class MainFragment @Inject constructor(var db: AppDb) : Fragment() {

    private var manager: UsageStatsManager? = null
    private val viewAdapter: ViewAdapter = ViewAdapter()
    lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance(db: AppDb): MainFragment {
            return MainFragment(db)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
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
        updateAppsList(usageStatistics)
    }


    private val usageStatistics: List<UsageStats>?
        get() {
            val time = System.currentTimeMillis()
            val appList = (manager ?: return null)
                    .queryUsageStats(
                            UsageStatsManager.INTERVAL_DAILY, time - 10000 * 10000, time)
            if (appList == null || appList.size == 0) {
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

    private fun updateAppsList(usageStatsList: List<UsageStats>?) {
        viewModel = MainViewModel(db)
        viewModel.update(usageStatsList, requireActivity())
        db.statsDao()
                .getAll()
                .observe(viewLifecycleOwner, Observer {
                    viewAdapter.updateStatsList(it, requireActivity())
                })
        recyclerview_app_usage.scrollToPosition(0)
    }
}

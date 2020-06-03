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
import com.rsf.innometrics.db.AppDb
import kotlinx.android.synthetic.main.fragment_app_usage_statistics.*
import timber.log.Timber
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
        //AndroidSupportInjection.inject(this)
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

        //viewModel = MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
        recyclerview_app_usage.run {
            scrollToPosition(0)
            adapter = viewAdapter
        }
        update()
    }

    private fun update() {
        updateAppsList(usageStatistics)
    }

    private val usageStatistics: List<UsageStats>?
        get() {
            val time = System.currentTimeMillis()
            val appList = (manager ?: return null)
                    .queryUsageStats(
                            UsageStatsManager.INTERVAL_DAILY, time - 10000 * 10000, time)
            if (appList != null && appList.size == 0) {
                Timber.e("The user may not allow the access to apps usage. ")
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
        val appStatsList = viewModel.update(usageStatsList, requireActivity())
        viewAdapter.run {
            if (appStatsList != null) {
                setCustomUsageStatsList(appStatsList)
            }
            notifyDataSetChanged()
        }
        recyclerview_app_usage.scrollToPosition(0)
    }

}

package com.rsf.innometrics

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.net.MacAddress
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rsf.innometrics.data.RegistrationResponse
import com.rsf.innometrics.data.RestClient
import com.rsf.innometrics.data.db.AppDb
import com.rsf.innometrics.vo.Stats
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_app_usage_statistics.*
import okhttp3.RequestBody
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import java.net.InetAddress
import javax.inject.Singleton


class MainFragment @Singleton constructor(var db: AppDb) : Fragment() {

    private val restClient: RestClient by inject()
    private var manager: UsageStatsManager? = null
    private val viewAdapter: ViewAdapter = ViewAdapter()
    lateinit var viewModel: MainViewModel
    private var osVersion = Build.VERSION.SDK_INT
    private lateinit var ip: String
    private var mac: String? = ""

    companion object {
        fun newInstance(db: AppDb): MainFragment {
            return MainFragment(db)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        inet()
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
        usageStatistics()
        updateView()
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    override fun onStart() {
        super.onStart()
        button_send_setting.setOnClickListener {
           sendingProccess()
        }

    }

    fun sendingProccess(){
        inet()
        val credentials = (db.statsDao().getAll().observe(viewLifecycleOwner, Observer {
            jsonStats(it)
        }))
        restClient
                .getApiService(requireActivity().applicationContext)
                .addReport(credentials)
                .enqueue(
                        object : Callback<RegistrationResponse> {
                            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                                Toast.makeText(
                                        activity,
                                        "Error while sending statistics", Toast.LENGTH_LONG
                                ).show()
                            }

                            override fun onResponse(
                                    call: Call<RegistrationResponse>,
                                    response: retrofit2.Response<RegistrationResponse>
                            ) {
                                Toast.makeText(
                                        activity,
                                        "Sent", Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                )
        db.statsDao()
                .erase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun createJsonRequestBody(vararg params: Pair<String, String>) =
            RequestBody.create(
                    okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    JSONObject(mapOf(*params)).toString()
            )


    private fun jsonStats(all: List<Stats>) {
        var login = arguments?.getString("login")
        all.forEach { current ->
            createJsonRequestBody(
                    "activityID" to "0",
                    "activityType" to "Android",
                    "browser_title" to "",
                    "browser_url" to "",
                    "end_time" to current.time_end.toString(), //suffer
                    "executable_name" to current.app_name,
                    "idle_activity" to "false",
                    "ip_address" to ip,
                    "mac_address" to mac!!,
                    "osversion" to osVersion.toString(),
                    "pid" to "string",  //
                    "start_time" to current.time_begin.toString(), //suffer
                    "userID" to login!!)
        }
    }


    private fun inet() {
        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val address: InetAddress = InetAddress.getLocalHost()
        ip = address.hostAddress
        mac = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            val wifiManager: WifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wInfo: WifiInfo = wifiManager.connectionInfo
            wInfo.macAddress.toString()
        } else {
            MacAddress.BROADCAST_ADDRESS.toString()
        }
    }


    fun usageStatistics() {
        val time = System.currentTimeMillis()
        val appList = manager
                ?.queryUsageStats(
                        UsageStatsManager.INTERVAL_DAILY, time - 10000 * 10000, time)
        if (appList == null || appList.size == 0) {
            Toast.makeText(activity,
                    getString(R.string.explanation_access_to_appusage_is_not_enabled),
                    Toast.LENGTH_LONG).show()
            button_send_setting.run { visibility = View.GONE }
            button_open_usage_setting.run {
                visibility = View.VISIBLE
                setOnClickListener {
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }
            }
        }
        viewModel = MainViewModel(viewLifecycleOwner, db)
        viewModel.update(appList)
    }

    private fun updateView() {
        db.statsDao()
                .getAll()
                .observe(viewLifecycleOwner, Observer {
                    viewAdapter.updateStatsList(it, requireActivity())
                })
        recyclerview_app_usage.scrollToPosition(0)
    }
}

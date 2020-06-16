package com.rsf.innometrics

import android.Manifest
import android.annotation.SuppressLint
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.MacAddress
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.rsf.innometrics.data.RegistrationResponse
import com.rsf.innometrics.data.RestClient
import com.rsf.innometrics.data.db.AppDb
import com.rsf.innometrics.vo.Stats
import kotlinx.android.synthetic.main.fragment_app_usage_statistics.*
import kotlinx.android.synthetic.main.fragment_app_usage_statistics.button_send_setting
import kotlinx.android.synthetic.main.fragment_app_usage_statistics.view.*
import kotlinx.android.synthetic.main.sign_in_fragment.*
import okhttp3.RequestBody
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber
import java.net.InetAddress
import javax.inject.Inject


class MainFragment @Inject constructor(var db: AppDb) : Fragment() {

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
        //inet()
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

    override fun onStart() {
        super.onStart()
        button_send_setting.setOnClickListener {
            val credentials = jsonStats(db.statsDao().getAll())
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
                                            response.message(), Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    )
        }
    }


    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_CONTACTS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Timber.e("Permission denied")
        }
    }


    private fun createJsonRequestBody(vararg params: Pair<String, String>) =
            RequestBody.create(
                    okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    JSONObject(mapOf(*params)).toString()
            )


    private fun jsonStats(all: LiveData<List<Stats>>) {
        all.observe(viewLifecycleOwner, Observer { it ->
            it.forEach { current ->
                createJsonRequestBody(
                        "activityID" to "0",
                        "activityType" to "Android",
                        "browser_title" to "",
                        "browser_url" to "",
                        "end_time" to "2020-06-13T18:24:16.146Z", //suffer
                        "executable_name" to current.app_name,
                        "idle_activity" to "false", // true for waiting
                        "ip_address" to ip,
                        "mac_address" to mac!!,
                        "osversion" to osVersion.toString(),
                        "pid" to "string",  //wtf?!
                        "start_time" to "2020-06-13T18:24:16.146Z", //suffer
                        "userID" to login.text.toString())
            }
        })
    }


    private fun inet() {
        try {
            val address: InetAddress = InetAddress.getLocalHost()
            ip = address.hostAddress
            mac = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                val wifiManager: WifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wInfo: WifiInfo = wifiManager.connectionInfo
                wInfo.macAddress.toString()
            } else {
                MacAddress.BROADCAST_ADDRESS.toString()
            }
        } catch (e: Exception) {
            startActivity(Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS))
        }
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
                button_send_setting.run { visibility = View.GONE}
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
        viewModel.update(usageStatsList)
        db.statsDao()
                .getAll()
                .observe(viewLifecycleOwner, Observer {
                    viewAdapter.updateStatsList(it, requireActivity())
                })
        recyclerview_app_usage.scrollToPosition(0)
    }
}

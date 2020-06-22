package com.rsf.innometrics

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.rsf.innometrics.data.db.AppDb
import java.util.*

class BackgroundService : Service() {
    private lateinit var db: AppDb
    private val timer = Timer()
       override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }


    override fun onCreate() {
        db = Room.databaseBuilder(applicationContext, AppDb::class.java, "stats")
                .fallbackToDestructiveMigration()
                .build()
        super.onCreate()
    }

    private fun callAsynchronousTask() {
        val handler = Handler()
        val doAsynchronousTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post(Runnable {
                    try {
                        Log.d("###########", "Working")
                        MainFragment.newInstance(db).usageStatistics()

                    } catch (e: Exception) {
                    }
                })
            }
        }
        timer.schedule(doAsynchronousTask, 0, 1000) //execute in every 1000 ms

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        callAsynchronousTask()
        Toast.makeText(this, "Start.", Toast.LENGTH_LONG)
                .show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun bindService(service: Intent?, conn: ServiceConnection, flags: Int): Boolean {
        return super.bindService(service, conn, flags)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        Toast.makeText(this, "Stop.", Toast.LENGTH_LONG)
                .show()
    }
}
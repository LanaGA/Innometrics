package com.rsf.innometrics

import android.content.Context
import com.rsf.innometrics.data.RestClient
import com.rsf.innometrics.data.SessionManager
import org.koin.dsl.module

fun createAppModule() = module {
    single { RestClient() }
    single { MainFragment(get()) }
    single { SessionManager(get()) }
}

package com.rsf.innometrics.di

import android.Manifest
import android.app.Application
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.rsf.innometrics.InnometricsApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import io.reactivex.annotations.NonNull
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class
       // MainActivityModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: InnometricsApp)
}


package com.appboxo.paymaya.test

import android.app.Application
import com.appboxo.sdk.Appboxo
import com.appboxo.sdk.Config

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Appboxo.init(this)
            .setConfig(
                Config.Builder()
                    .sandboxMode(true)
                    .setClientId("911311")
                    .multitaskMode(true)
                    .permissionsPage(false)
                    .build()
            )
    }
}
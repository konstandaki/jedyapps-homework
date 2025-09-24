package com.konstandaki.jedyapps

import android.app.Application
import com.konstandaki.jedyapps.sdk.ads.AdsSdk
import com.konstandaki.jedyapps.sdk.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JahApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AdsSdk.init(
            app = this,
            config = AdsSdk.Config(
                interstitialUnitId = getString(R.string.admob_interstitial_unit),
                nativeUnitId = getString(R.string.admob_native_unit),
                testDeviceIds = emptyList()
            )
        )
    }
}
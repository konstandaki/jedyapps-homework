package com.konstandaki.jedyapps.sdk.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.lang.ref.WeakReference

object AdsSdk {

    data class Config(
        val appIdMetaInManifest: Boolean = true,
        val interstitialUnitId: String,
        val nativeUnitId: String,
        val testDeviceIds: List<String> = emptyList()
    )

    private lateinit var ctx: Application
    private lateinit var cfg: Config
    private var currentActivityRef = WeakReference<Activity?>(null)
    private var appStarts = 0
    private var interstitialShownOnce = false

    private lateinit var interstitial: InterstitialProvider
    private lateinit var nativeFactory: () -> NativeAdComposable

    fun init(app: Application, config: Config) {
        ctx = app
        cfg = config

        MobileAds.initialize(app)

        val testIds = mutableListOf(
            AdRequest.DEVICE_ID_EMULATOR,
            "DBB04135507CF71234E012674886BA96"
        ).apply {
            addAll(config.testDeviceIds)
        }
        if (testIds.isNotEmpty()) {
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder()
                    .setTestDeviceIds(testIds)
                    .build()
            )
        }

        if (config.testDeviceIds.isNotEmpty()) {
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder().setTestDeviceIds(config.testDeviceIds).build()
            )
        }

        interstitial = AdMobInterstitial(app, cfg.interstitialUnitId)
        nativeFactory = { AdMobNative(cfg.nativeUnitId) }

        interstitial.preload()
        trackCurrentActivity(app)
        registerSecondStartShow()
    }

    private fun trackCurrentActivity(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                currentActivityRef = WeakReference(activity)
            }
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityCreated(a: Activity, b: Bundle?) {}
            override fun onActivityStarted(a: Activity) {}
            override fun onActivityStopped(a: Activity) {}
            override fun onActivitySaveInstanceState(a: Activity, b: Bundle) {}
            override fun onActivityDestroyed(a: Activity) {}
        })
    }

    private fun registerSecondStartShow() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                appStarts++
                if (appStarts == 2 && !interstitialShownOnce) {
                    currentActivityRef.get()?.let { act ->
                        if (interstitial.showIfReady(act)) {
                            interstitialShownOnce = true
                        }
                    }
                }
            }
        })
    }

    fun native(): NativeAdComposable = nativeFactory()
}
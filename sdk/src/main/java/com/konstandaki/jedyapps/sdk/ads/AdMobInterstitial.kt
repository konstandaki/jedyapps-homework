package com.konstandaki.jedyapps.sdk.ads

import android.app.Activity
import android.app.Application
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

internal class AdMobInterstitial(
    private val app: Application,
    private val unitId: String
) : InterstitialProvider {

    private var ad: InterstitialAd? = null

    override fun preload() {
        InterstitialAd.load(
            app,
            unitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) { ad = interstitialAd }
                override fun onAdFailedToLoad(error: LoadAdError) { ad = null }
            }
        )
    }

    override fun showIfReady(activity: Activity): Boolean {
        val ready = ad != null
        ad?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                ad = null
                preload()
            }
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                ad = null; preload()
            }
        }
        ad?.show(activity)
        ad = null
        return ready
    }
}
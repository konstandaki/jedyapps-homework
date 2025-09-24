package com.konstandaki.jedyapps.sdk.ads

import android.app.Activity

interface InterstitialProvider {
    fun preload()
    fun showIfReady(activity: Activity): Boolean
}

interface NativeAdComposable {
    @androidx.compose.runtime.Composable
    fun Render(modifier: androidx.compose.ui.Modifier)
}
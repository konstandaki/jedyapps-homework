package com.konstandaki.jedyapps.sdk.ads

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.konstandaki.jedyapps.sdk.R

internal class AdMobNative(
    private val unitId: String
) : NativeAdComposable {

    @Composable
    override fun Render(modifier: Modifier) {
        val context = LocalContext.current
        var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

        DisposableEffect(Unit) {
            val loader = AdLoader.Builder(context, unitId)
                .forNativeAd { ad ->
                    nativeAd?.destroy()
                    nativeAd = ad
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        nativeAd?.destroy(); nativeAd = null
                    }
                })
                .withNativeAdOptions(NativeAdOptions.Builder().build())
                .build()

            loader.loadAd(AdRequest.Builder().build())

            onDispose { nativeAd?.destroy(); nativeAd = null }
        }

        AndroidView(
            modifier = modifier,
            factory = { ctx ->
                val view = LayoutInflater.from(ctx)
                    .inflate(
                        R.layout.view_native_ad,
                        FrameLayout(ctx),
                        false
                    ) as NativeAdView
                nativeAd?.let { bind(it, view) }
                view
            },
            update = { view ->
                nativeAd?.let { bind(it, view) }
            }
        )
    }

    private fun bind(ad: NativeAd, view: NativeAdView) {
        val headline = view.findViewById<TextView>(R.id.ad_headline)
        val body     = view.findViewById<TextView>(R.id.ad_body)
        val media    = view.findViewById<MediaView>(R.id.ad_media)
        val cta      = view.findViewById<Button>(R.id.ad_cta)
        val choices  = view.findViewById<AdChoicesView>(R.id.ad_choices)

        view.headlineView = headline
        view.bodyView = body
        view.mediaView = media
        view.callToActionView = cta
        view.adChoicesView = choices

        headline.text = ad.headline
        if (ad.body.isNullOrBlank()) { body.visibility = View.GONE } else { body.text = ad.body; body.visibility = View.VISIBLE }

        media.mediaContent = ad.mediaContent
        media.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

        if (ad.callToAction.isNullOrBlank()) { cta.visibility = View.GONE } else { cta.text = ad.callToAction; cta.visibility = View.VISIBLE }

        view.setNativeAd(ad)
    }
}
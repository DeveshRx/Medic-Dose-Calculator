package devesh.medic.dose


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdMobBannerNormal() {
    val mContext= LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            // on below line specifying ad view.
            AdView(context).apply {
                // on below line specifying ad size
                //adSize = AdSize.BANNER
                // on below line specifying ad unit id
                // currently added a test ad unit id.
                setAdSize(AdSize.BANNER)
                adUnitId = mContext.getString(R.string.admob_ad_banner_id_normal)
                // calling load ad to load our ad.
                loadAd(AdRequest.Builder().build())
            }

        }
    )
}


@Composable
fun AdMobBannerMediumRec() {
    val mContext= LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            // on below line specifying ad view.
            AdView(context).apply {
                // on below line specifying ad size
                //adSize = AdSize.BANNER
                // on below line specifying ad unit id
                // currently added a test ad unit id.
                setAdSize(AdSize.MEDIUM_RECTANGLE)
                adUnitId = mContext.getString(R.string.admob_ad_banner_id_med_rect)
                // calling load ad to load our ad.
                loadAd(AdRequest.Builder().build())
            }

        }
    )

}


@Composable
fun AdMobBannerLarge() {
    val mContext= LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            // on below line specifying ad view.
            AdView(context).apply {
                // on below line specifying ad size
                //adSize = AdSize.BANNER
                // on below line specifying ad unit id
                // currently added a test ad unit id.
                setAdSize(AdSize.LARGE_BANNER)
                adUnitId = mContext.getString(R.string.admob_ad_banner_id_large_rect)
                // calling load ad to load our ad.
                loadAd(AdRequest.Builder().build())
            }

        }
    )
}




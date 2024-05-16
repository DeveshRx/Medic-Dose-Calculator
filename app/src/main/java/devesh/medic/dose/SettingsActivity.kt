package devesh.medic.dose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import devesh.medic.dose.ui.theme.MedicDoseCalculatorTheme
import deveshrx.apps.AppItem
import deveshrx.apps.DeveshRxAppsList
import deveshrx.apps.TAG

class SettingsActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SettingsScreen()
        }



        if(getString(R.string.app_flavour).equals("free")) {

            val callback = onBackPressedDispatcher.addCallback(this) {
                // Handle the back button event
                Log.d(TAG, "onCreate: BACK BUTTON EVENT")
                GoBackEvent();
            }
            callback.isEnabled = true

            loadIntAds();

        }


    }

    private var mInterstitialAd: InterstitialAd? = null
    fun loadIntAds(){
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,getString(R.string.admob_ad_interstitial_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd

                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.")
                        mInterstitialAd=null;


                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        Log.d(TAG, "Ad dismissed fullscreen content.")
                        mInterstitialAd = null
                        GoBackEvent();

                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        // Called when ad fails to show.
                        Log.e(TAG, "Ad failed to show fullscreen content.")
                        mInterstitialAd = null


                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Ad showed fullscreen content.")
                        GoBackEvent();
                    }
                }
            }
        })

    }

    fun showIntAd(){
        mInterstitialAd?.show(this)

    }

    fun GoBackEvent(){
        if(getString(R.string.app_flavour).equals("free")) {
            if(mInterstitialAd != null){
                showIntAd()
            }else{
                this@SettingsActivity.finish()
            }
        }else{
            this@SettingsActivity.finish()
        }


    }

    var appsList = mutableStateListOf<AppItem>()

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun SettingsScreen() {
        val allAppList= remember { appsList }

        var app_version: String = ""
        var app_build_no: String = ""

        if (LocalInspectionMode.current) {
            app_version = "2.0.0"
            app_build_no = "00"
        } else {
            app_version = getText(R.string.app_version).toString()
            app_build_no = getText(R.string.app_build_no).toString()
        }





        MedicDoseCalculatorTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.shadow(
                            elevation = 5.dp,
                            //    spotColor = Color.DarkGray,
                        ),
                        navigationIcon = {
                            IconButton(onClick = {
                                GoBackEvent();


                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = "back button"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),

                        title = {
                            Text("About")
                        },


                        )
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {

                    if(!LocalInspectionMode.current){
                        if(getString(R.string.app_flavour).equals("free")) {
                            Spacer(modifier = Modifier.height(10.dp))
                            AdMobBannerNormal()
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                    }
                    HorizontalDivider(modifier = Modifier.padding(top = 5.dp))
                    if(getString(R.string.app_flavour).equals("free")){
                        SettingOption(text = "Disable Ads", subtext = "Get ad-free version ", click = {

                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(
                                    "https://play.google.com/store/apps/details?id=devesh.medic.dose.calc")
                                setPackage("com.android.vending")
                            }
                            startActivity(intent)
                        })
                    }


                    SettingOption(text = "Version", subtext = "${app_version}", click = {})

                    SettingOption(text = "Build no", subtext = "${app_build_no}", click = {})

                    SettingOption(
                        text = "Official Website",
                        subtext = "https://DeveshRx.com",
                        click = {
                            val url = "https://DeveshRx.com"
                            val urlIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(url)
                            )
                            startActivity(urlIntent)
                        })


Spacer(modifier = Modifier.height(10.dp))

                    DeveshRxAppsList()
HorizontalDivider(modifier = Modifier.padding(top = 5.dp))

                    if(!LocalInspectionMode.current){

                        Spacer(modifier = Modifier.height(10.dp))
                        AdMobBannerLarge()
                        Spacer(modifier = Modifier.height(10.dp))
                    }



                }

            }
        }
    }

    @Composable
    fun SettingOption(text: String, subtext: String, click: () -> Unit) {
        Surface(
            onClick = click
        ) {
            Column {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(text = text)
                    if (subtext.isNotEmpty() || subtext.isNotBlank()) {
                        Text(text = subtext)
                    }

                }

                HorizontalDivider()
            }
        }
    }


}



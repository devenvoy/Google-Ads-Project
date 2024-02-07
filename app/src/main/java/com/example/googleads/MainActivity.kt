package com.example.googleads

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.googleads.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mInterstitialAd: InterstitialAd
    lateinit var adRequest: AdRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // intialize instance
        MobileAds.initialize(this) {}
        // intialize adrequest builder
        adRequest = AdRequest.Builder().build()

        // Banner Ad
        //      show ad in Google view
        binding.adView.loadAd(adRequest)

        //      Show ad in Custom view
        val adView = AdView(this)
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        adView.loadAd(adRequest)
        binding.CustomAdView.addView(adView)

        // Interstitial Ad
        loadAD()
        binding.AdOnButton.setOnClickListener {
            // show ad if ad available
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
                // load ad again
                loadAD()
                startActivity(Intent(this@MainActivity, RecyclerViewAd::class.java))
            } else {
                Log.d("===", "The interstitial ad wasn't ready yet.")
            }
        }
    }

    fun loadAD() {
        // this method get an ad to load
        InterstitialAd.load(this@MainActivity, "ca-app-pub-3940256099942544/1033173712",
            adRequest, object : InterstitialAdLoadCallback() {

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Log.d("===", "The interstitial ad wasn't ready yet.")
                    Toast.makeText(this@MainActivity, adError.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    Log.d("===", "ADD LOaded")
                    mInterstitialAd = interstitialAd
                }

            })

    }
}
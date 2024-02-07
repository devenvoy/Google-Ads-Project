package com.example.googleads

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.googleads.databinding.ActivityRecyclerViewAdBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class RecyclerViewAd : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerViewAdBinding
    lateinit var myAdapter: MyAdapter2
    private val mListItems = ArrayList<Any>()

    val ITEMS_PER_AD = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewAdBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initAdMobAdsSDK();
        addCountriesData();
        addAdMobBannerAds();
        myAdapter = MyAdapter2(mListItems)
        binding.myRecyclerView.adapter = myAdapter
        loadBannerAds();


    }


    private fun addCountriesData() {
        mListItems.add(Country("Canada", "Ottawa", "Canadian Dollar", "North America"))
        mListItems.add(Country("Norway", "Oslo", "Norwegian Krone", "Europe"))
        mListItems.add(Country("Malaysia", "Kuala Lumpur", "Malaysian Ringgit", "Asia"))
        mListItems.add(Country("Singapore", "Singapore", "Singapore Dollar", "Asia"))
        mListItems.add(
            Country(
                "United States of America",
                "Washington, D.C.",
                "United States Dollar",
                "North America"
            )
        )
        mListItems.add(Country("India", "New Delhi", "Indian Rupee", "Asia"))
        mListItems.add(Country("Brazil", "Brasilia", " Brazilian Real", "South America"))
        mListItems.add(Country("Australia", "Canberra", "Australian Dollar", "Oceania"))
        mListItems.add(Country("Russia", "Moscow", "Russian Ruble", "Europe, Asia"))
        mListItems.add(Country("Japan", "Tokyo", "Japanese Yen", "Asia"))

        //adding extra dummy data
        for (i in 0..79) {
            mListItems.add(Country("Country$i", "Capital$i", "dummy", "dummy"))
        }
    }

    private fun addAdMobBannerAds() {
        var i: Int = ITEMS_PER_AD
        while (i <= mListItems.size) {
            val adView = AdView(this@RecyclerViewAd)
            adView.setAdSize(AdSize.BANNER)
            adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
            mListItems.add(i, adView)
            i += ITEMS_PER_AD
        }
        loadBannerAds()
    }

    private fun loadBannerAds() {
        //Load the first banner ad in the items list (subsequent ads will be loaded automatically in sequence).
        loadBannerAd(ITEMS_PER_AD)
    }

    private fun loadBannerAd(index: Int) {
        if (index >= mListItems.size) {
            return
        }
        val item: AdView = mListItems[index] as? AdView
            ?: throw ClassCastException("Expected item at index $index to be a banner ad ad.")
        val adView = item as AdView

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                // The previous banner ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadBannerAd(index + ITEMS_PER_AD)
            }

            fun onAdFailedToLoad(errorCode: Int) {
                // The previous banner ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e(
                    "MainActivity", "The previous banner ad failed to load. Attempting to"
                            + " load the next banner ad in the items list."
                )
                loadBannerAd(index + ITEMS_PER_AD)
            }
        }

        // Load the banner ad.
        adView.loadAd(AdRequest.Builder().build())
    }

    private fun initAdMobAdsSDK() {
        MobileAds.initialize(this) {}
    }

    override fun onResume() {
        for (item in mListItems) {
            if (item is AdView) {
                val adView = item as AdView
                adView.resume()
            }
        }
        super.onResume()
    }

    override fun onPause() {
        for (item in mListItems) {
            if (item is AdView) {
                val adView = item as AdView
                adView.pause()
            }
        }
        super.onPause()
    }

    override fun onDestroy() {
        for (item in mListItems) {
            if (item is AdView) {
                val adView = item as AdView
                adView.destroy()
            }
        }
        super.onDestroy()
    }
}
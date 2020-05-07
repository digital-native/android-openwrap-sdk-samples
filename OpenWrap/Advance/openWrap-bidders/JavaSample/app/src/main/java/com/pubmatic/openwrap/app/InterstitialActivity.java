package com.pubmatic.openwrap.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.pubmatic.sdk.common.OpenWrapSDK;
import com.pubmatic.sdk.common.POBAdSize;
import com.pubmatic.sdk.common.POBError;
import com.pubmatic.sdk.common.models.POBApplicationInfo;
import com.pubmatic.sdk.fanbidder.POBFANBidderConstants;
import com.pubmatic.sdk.openwrap.interstitial.POBInterstitial;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class InterstitialActivity extends AppCompatActivity {

    private static final String OPENWRAP_AD_UNIT_ONE = "OpenWrapInterstitialAdUnit";
    private static final String PUB_ID = "156276";
    private static final int PROFILE_ID = 1165;

    private static final String appId = "2526468451010379";
    private static final String placementId = "IMG_16_9_APP_INSTALL#2526468451010379_2529646540692570";

    private POBInterstitial interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        // A valid Play Store Url of an Android application is required.
        POBApplicationInfo appInfo = new POBApplicationInfo();
        try {
            appInfo.setStoreURL(new URL("https://play.google.com/store/apps/details?id=com.example.android&hl=en"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // This app information is a global configuration & you
        // Need not set this for every ad request(of any ad type)
        OpenWrapSDK.setApplicationInfo(appInfo);

        // Create  interstitial instance by passing activity context and
        interstitial = new POBInterstitial(this, PUB_ID,
                PROFILE_ID,
                OPENWRAP_AD_UNIT_ONE);

        // Set Optional listener
        interstitial.setListener(new InterstitialActivity.POBInterstitialListener());
        interstitial.getAdRequest().enableTestMode(true);


        // Load Ad button
        findViewById(R.id.loadAdBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.showAdBtn).setEnabled(false);
                interstitial.loadAd();

            }
        });

        //Create slot info map of facebook ad details like app id and placement id
        Map<String, Object> slotInfo = new HashMap<>();
        slotInfo.put(POBFANBidderConstants.POB_BIDDER_KEY_FB_APP_ID, appId);
        slotInfo.put(POBFANBidderConstants.POB_BIDDER_KEY_FB_PLACEMENT_ID, placementId);

        // Pass slot info to interstitial instance
        interstitial.addBidderSlotInfo(POBFANBidderConstants.POB_FAN_BIDDER_ID_FAN, slotInfo);

        // Show button
        findViewById(R.id.showAdBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitialAd();
            }
        });


    }

    /**
     * To show interstitial ad call this method
     **/
    private void showInterstitialAd() {
        // check if the interstitial is ready
        if (null != interstitial) {
            // Call show on interstitial
            interstitial.show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != interstitial) {
            interstitial.destroy();
        }

    }

    // Interstitial Ad listener callbacks
    class POBInterstitialListener extends POBInterstitial.POBInterstitialListener {
        private final String TAG = "POBInterstitialListener";
        // Callback method notifies that an ad has been received successfully.
        @Override
        public void onAdReceived(POBInterstitial ad) {
            Log.d(TAG, "onAdReceived");
            //Method gets called when ad gets loaded in container
            //Here, you can show interstitial ad to user
            findViewById(R.id.showAdBtn).setEnabled(true);
        }

        // Callback method notifies an error encountered while loading or rendering an ad.
        @Override
        public void onAdFailed(POBInterstitial ad, POBError error) {
            Log.e(TAG, "onAdFailed : Ad failed with error - " + error.toString());
            //Method gets called when loadAd fails to load ad
            //Here, you can put logger and see why ad failed to load
        }

        // Callback method notifies that the interstitial ad will be presented as a modal on top of the current view controller
        @Override
        public void onAdOpened(POBInterstitial ad) {
            Log.d(TAG, "onAdOpened");
        }

        // Callback method notifies that the interstitial ad has been animated off the screen.
        @Override
        public void onAdClosed(POBInterstitial ad) {
            Log.d(TAG, "onAdClosed");
        }

        // Callback method notifies ad click
        @Override
        public void onAdClicked(POBInterstitial ad) {
            Log.d(TAG, "onAdClicked");
        }

        @Override
        public void onAppLeaving(POBInterstitial ad) {
            // Implement your custom logic
            Log.d(TAG, "Interstitial : App Leaving");
        }
    }
}
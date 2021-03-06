//Copyright (c) 2016
//License: GNU GENERAL PUBLIC LICENSE
package com.revolution.cordova.plugin.ad.vungle;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;
//
import com.vungle.publisher.AdConfig;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.Orientation;
import com.vungle.publisher.VunglePub;
//md5
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//Util
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Surface;
//
import java.util.*;//Random

class Util {

	//ex) Util.alert(cordova.getActivity(),"message");
	public static void alert(Activity activity, String message) {
		AlertDialog ad = new AlertDialog.Builder(activity).create();  
		ad.setCancelable(false); // This blocks the 'BACK' button  
		ad.setMessage(message);  
		ad.setButton("OK", new DialogInterface.OnClickListener() {  
			@Override  
			public void onClick(DialogInterface dialog, int which) {  
				dialog.dismiss();                      
			}  
		});  
		ad.show(); 		
	}
	
	//https://gitshell.com/lvxudong/A530_packages_app_Camera/blob/master/src/com/android/camera/Util.java
	public static int getDisplayRotation(Activity activity) {
	    int rotation = activity.getWindowManager().getDefaultDisplay()
	            .getRotation();
	    switch (rotation) {
	        case Surface.ROTATION_0: return 0;
	        case Surface.ROTATION_90: return 90;
	        case Surface.ROTATION_180: return 180;
	        case Surface.ROTATION_270: return 270;
	    }
	    return 0;
	}

	public static final String md5(final String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }
}

public class Vungle extends CordovaPlugin {
	private final String LOG_TAG = "Vungle";
	private CallbackContext callbackContextKeepCallback;
	//
	
	
	// get the VunglePub instance
	final VunglePub vunglePub = VunglePub.getInstance();
	
    @Override
	public void pluginInitialize() {
		super.pluginInitialize();
		//
    }
	
	//@Override
	//public void onCreate(Bundle savedInstanceState) {//build error
	//	super.onCreate(savedInstanceState);
	//	//
	//}
	
	//@Override
	//public void onStart() {//build error
	//	super.onStart();
	//	//
	//}
	
	@Override
	public void onPause(boolean multitasking) {
		super.onPause(multitasking);
		vunglePub.onPause();
	}
	
	@Override
	public void onResume(boolean multitasking) {
		super.onResume(multitasking);
		vunglePub.onResume();
	}
	
	//@Override
	//public void onStop() {//build error
	//	super.onStop();
	//	//
	//}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		 
		
		return true; // Returning false results in a "MethodNotFound" error.
	}
	
	private void setUp(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		//Activity activity=cordova.getActivity();
		//webView
		//args.length()
		//args.getString(0)
		//args.getString(1)
		//args.getInt(0)
		//args.getInt(1)
		//args.getBoolean(0)
		//args.getBoolean(1)
		//JSONObject json = args.optJSONObject(0);
		//json.optString("adUnitBanner")
		//json.optString("adUnitFullScreen")
		//JSONObject inJson = json.optJSONObject("inJson");
		//final String adUnitBanner = args.getString(0);
		//final String adUnitFullScreen = args.getString(1);				
		//final boolean isOverlap = args.getBoolean(2);				
		//final boolean isTest = args.getBoolean(3);
		//final String[] zoneIds = new String[args.getJSONArray(4).length()];
		//for (int i = 0; i < args.getJSONArray(4).length(); i++) {
		//	zoneIds[i] = args.getJSONArray(4).getString(i);
		//}			
		//Log.d(LOG_TAG, String.format("%s", adUnitBanner));			
		//Log.d(LOG_TAG, String.format("%s", adUnitFullScreen));
		//Log.d(LOG_TAG, String.format("%b", isOverlap));
		//Log.d(LOG_TAG, String.format("%b", isTest));	
		final String appId = args.getString(0);
		Log.d(LOG_TAG, String.format("%s", appId));			
		
		callbackContextKeepCallback = callbackContext;
			
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				_setUp(appId);
			}
		});
	}

	private void showRewardedVideoAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_showRewardedVideoAd();
			}
		});
	}
			
		
	private void _setUp(String appId) {
		this.appId = appId;
		
		
		vunglePub.init(cordova.getActivity(), appId);
		vunglePub.setEventListeners(new MyEventListener());//listener needs to come after init on android vunlge sdk
		
		final AdConfig config = vunglePub.getGlobalAdConfig();
		config.setOrientation(Orientation.autoRotate);//for android
		//config.setOrientation(Orientation.matchVideo);
	}
	
	private void _showRewardedVideoAd() {
		vunglePub.playAd();
		//final AdConfig overrideConfig = new AdConfig();			
        //overrideConfig.setIncentivized(true);
        //overrideConfig.setSoundEnabled(false);
		//vunglePub.playAd(overrideConfig);		
	}

	class MyEventListener implements EventListener {	
	
		@Override
		public void onAdPlayableChanged(boolean isAdPlayable) {
			Log.d(LOG_TAG, "onAdPlayableChanged");
			
			if (isAdPlayable) {
				PluginResult pr = new PluginResult(PluginResult.Status.OK, "onRewardedVideoAdLoaded");
				pr.setKeepCallback(true);
				callbackContextKeepCallback.sendPluginResult(pr);
				//PluginResult pr = new PluginResult(PluginResult.Status.ERROR);
				//pr.setKeepCallback(true);
				//callbackContextKeepCallback.sendPluginResult(pr);	
			}
		}
		
		@Override
		public void onAdUnavailable(String arg0) {
			Log.d(LOG_TAG, "onAdUnavailable");
		}
		
		@Override
		public void onAdStart() {
			// Called before playing an ad
			Log.d(LOG_TAG, "onAdStart");
			
			PluginResult pr = new PluginResult(PluginResult.Status.OK, "onRewardedVideoAdShown");
			pr.setKeepCallback(true);
			callbackContextKeepCallback.sendPluginResult(pr);
			//PluginResult pr = new PluginResult(PluginResult.Status.ERROR);
			//pr.setKeepCallback(true);
			//callbackContextKeepCallback.sendPluginResult(pr);					
		}

		@Override
		public void onAdEnd(boolean wasCallToActionClicked) {
			// Called when the user leaves the ad and control is returned to your application
			Log.d(LOG_TAG, "onAdEnd");
			
			PluginResult pr = new PluginResult(PluginResult.Status.OK, "onRewardedVideoAdHidden");
			pr.setKeepCallback(true);
			callbackContextKeepCallback.sendPluginResult(pr);
			//PluginResult pr = new PluginResult(PluginResult.Status.ERROR);
			//pr.setKeepCallback(true);
			//callbackContextKeepCallback.sendPluginResult(pr);					
		}
		
		@Override
		public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
			// Called each time an ad completes. isCompletedView is true if at least  
			// 80% of the video was watched, which constitutes a completed view.  
			// watchedMillis is for the longest video view (if the user replayed the 
			// video).
			if (isCompletedView) {
				Log.d(LOG_TAG, "onVideoView: completed");

				PluginResult pr = new PluginResult(PluginResult.Status.OK, "onRewardedVideoAdCompleted");
				pr.setKeepCallback(true);
				callbackContextKeepCallback.sendPluginResult(pr);
				//PluginResult pr = new PluginResult(PluginResult.Status.ERROR);
				//pr.setKeepCallback(true);
				//callbackContextKeepCallback.sendPluginResult(pr);
			}
			else {
				Log.d(LOG_TAG, "onVideoView: not completed");
/*				
				PluginResult pr = new PluginResult(PluginResult.Status.OK, "onRewardedVideoAdNotCompleted");
				pr.setKeepCallback(true);
				callbackContextKeepCallback.sendPluginResult(pr);
				//PluginResult pr = new PluginResult(PluginResult.Status.ERROR);
				//pr.setKeepCallback(true);
				//callbackContextKeepCallback.sendPluginResult(pr);
*/				
			}
		}
	}	
} 
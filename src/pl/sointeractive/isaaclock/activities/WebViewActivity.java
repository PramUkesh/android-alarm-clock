package pl.sointeractive.isaaclock.activities;

import pl.sointeractive.isaaclock.R;
import pl.sointeractive.isaaclock.config.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Activity for viewing web pages. Used to logging into IsaaClock accounts.
 * 
 * WARNING: This class in not ready. The production API does not cover this
 * functionality yet. Do not use this class until further improvements are done.
 * 
 * @author Mateusz Renes
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity {

	private static final String TAG = "WebViewActivity";
	
	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		webView = (WebView) findViewById(R.id.webview);
		webView.setBackgroundColor(Color.BLACK);
		webView.setWebViewClient(new CustomWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(Settings.loginUrl);
	}
	
	private class CustomWebViewClient extends WebViewClient {
	    
	    @Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirect: " + url);
			boolean shouldOverride = false;
			if (url.startsWith("https://")) {
				// do something
			}
			return shouldOverride;
		}
	}

}
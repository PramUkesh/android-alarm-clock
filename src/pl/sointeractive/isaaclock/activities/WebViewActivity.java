package pl.sointeractive.isaaclock.activities;

import pl.sointeractive.isaaclock.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity {

	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		webView = (WebView) findViewById(R.id.webview);
		webView.setBackgroundColor(Color.BLACK);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				boolean shouldOverride = false;
				if (url.startsWith("https://")) { // NON-NLS
					// DO SOMETHING
				}
				return shouldOverride;
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("https://");

	}

}
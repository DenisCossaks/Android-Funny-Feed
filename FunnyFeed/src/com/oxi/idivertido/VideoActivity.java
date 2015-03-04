package com.oxi.idivertido;


import com.mycom.customcontrol.CustomButtonTouchListener;
import com.mycom.data.Global;
import com.parse.Parse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class VideoActivity extends BaseActivity {

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		
		
		setContentView(R.layout.view_youtube);
		
		Button btnClose = (Button) findViewById(R.id.btnClose);
		btnClose.setOnTouchListener(CustomButtonTouchListener.getInstance());
		btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		String url = getIntent().getStringExtra("youtube_url");
		final WebView mWebView = (WebView) findViewById(R.id.webview);

    	WebSettings settings = mWebView.getSettings();
//    	settings.setBuiltInZoomControls(true);
//    	settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//    	settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);

    	
    	mWebView.setWebChromeClient(new WebChromeClient() {
//    		@Override
//            public void onShowCustomView(View view, CustomViewCallback callback) {
//                customComponenet.addView(view);
//                mWebView.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onHideCustomView() {
//                if (customComponenet == null)
//                    return;
//                // Hide the custom view.
//                customComponenet.setVisibility(View.GONE);
//
//                mWebView.setVisibility(View.VISIBLE);
//            }
    	});
    	

    	final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
    	
              
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
            	return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            	// TODO Auto-generated method stub
            	
            	progressBar.setVisibility(View.VISIBLE);
            	
            	super.onPageStarted(view, url, favicon);
            }
            
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(VideoActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        
        mWebView.loadUrl(url);
	}
	
}

package com.basic.views;


import java.net.URL;
import java.util.HashMap;


import com.custom.items.Item;
import com.custom.items.ItemFeed;
import com.custom.items.ItemFeedGif;

import com.custom.items.RowType;
import com.mycom.customcontrol.CustomButtonTouchListener;
import com.mycom.data.Global;
import com.mycom.lib.gesture.imageview.GestureImageView;
import com.mycom.lib.gesture.imageview.PanAndZoomListener;
import com.mycom.lib.gesture.imageview.PanAndZoomListener.Anchor;
import com.mycom.lib.gesture.imageview.PanZoomView;
import com.mycom.lib.gifimageview.GifImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.oxi.idivertido.HomeActivity;
import com.oxi.idivertido.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;




public class PhotoDetailView extends BaseView {
	
	public HomeActivity m_context;
	public static PhotoDetailView g_PhotoDetailView = null;
				
	ProgressDialog progress = null;

	boolean m_isFullMode = false;
	private FrameLayout layoutTitle = null;
	
	GestureImageView ivPhoto = null;
	GifImageView	gifPhoto = null;
	
	TextView tvTitle = null;
	
	boolean isGifImg = false;
	

	public PhotoDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        
        initView(context);
    }

    public PhotoDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initView(context);
    }

    public PhotoDetailView(Context context) {
        super(context);
        
        initView(context);
    }
    
    private void initView(Context context){

    	m_context = (HomeActivity)context;
    	
    	View.inflate(context, R.layout.view_photo_detail, this);
    }
    
    
    public void init(ParseObject feed) {
		g_PhotoDetailView = this;
		
		m_context.addHistory(getClass().getSimpleName(), this);

		final Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnTouchListener(CustomButtonTouchListener.getInstance());
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnBack.setEnabled(false);
				onBack();
			}
		});
    	
    	layoutTitle = (FrameLayout) findViewById(R.id.titleLayout);
    	
    	ivPhoto = (GestureImageView) findViewById(R.id.imagePhoto);
    	gifPhoto = (GifImageView) findViewById(R.id.gifPhoto);
    	
    	
    	
    	final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressPhoto);
    	
    	final ParseFile imageFile = (ParseFile)feed.get("picture");
    	String imgUrl = imageFile.getUrl();
		if (imgUrl.contains(".gif")) {
			isGifImg = true;
			ivPhoto.setVisibility(View.GONE);
			gifPhoto.setVisibility(View.VISIBLE);
		}
		else {
			isGifImg = false;
			ivPhoto.setVisibility(View.VISIBLE);
			gifPhoto.setVisibility(View.GONE);
		}    	


    	imageFile.getDataInBackground(new GetDataCallback() {
			
			@Override
			public void done(byte[] data, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
		       	      // data has the bytes for the image
	       			// Decode the Byte[] into
                        // Bitmap
					
					Bitmap bmp = BitmapFactory
                            .decodeByteArray(
                                    data, 0,
                                    data.length);
					
			    	progressBar.setVisibility(View.GONE);

			    	if (isGifImg) {
			        	gifPhoto.setBytes(data);
			        	gifPhoto.startAnimation();
			    	} else {
			    		ivPhoto.setImageBitmap(bmp);
			    	}
	        		

				} else {
			         // something went wrong
	       	   }
			}
		}, new ProgressCallback() {
			
			@Override
			public void done(Integer arg0) {
				// TODO Auto-generated method stub
				progressBar.setProgress(arg0);
			}
		});
    	
/*		
    	String imgUrl = imageFile.getUrl();
    	m_context.imageLoader.displayImage(imgUrl, ivPhoto, m_context.optFull, new SimpleImageLoadingListener() {
			 @Override
			 public void onLoadingStarted(String imageUri, View view) {
				 progressBar.setProgress(0);
				 progressBar.setVisibility(View.VISIBLE);
			 }

			 @Override
			 public void onLoadingFailed(String imageUri, View view,
					 FailReason failReason) {
				 progressBar.setVisibility(View.GONE);
			 }

			 @Override
			 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			    	progressBar.setVisibility(View.GONE);
			 }
		 }, new ImageLoadingProgressListener() {
			 @Override
			 public void onProgressUpdate(String imageUri, View view, int current,
					 int total) {
				 progressBar.setProgress(Math.round(100.0f * current / total));
			 }
		 }
		);	
*/		 
		 
		ivPhoto.setClickable(true);
		ivPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (m_isFullMode) {
					layoutTitle.setVisibility(View.VISIBLE);
					m_isFullMode = false;
				}
				else {
					layoutTitle.setVisibility(View.GONE);
					m_isFullMode = true;
				}
			}
		});

    }
    
    public void getExtraInfo () {

    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Loading....");

    	progress.show();
    	
    	
		new Thread(new Runnable() {
			
			public void run() {
			}
		}).start();
    }
    

    
    public void onBack() {
    
    	m_context.removeHistory(getClass().getSimpleName());
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_PhotoDetailView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
//    	    	m_context.boardLayout.removeView(g_PhotoDetailView);
    	    	ViewGroup vg = (ViewGroup)(g_PhotoDetailView.getParent());
    	    	vg.removeView(g_PhotoDetailView);
    	    	
    	    }
    	});
    }
    
}

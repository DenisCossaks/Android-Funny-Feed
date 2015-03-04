package com.custom.items;


import java.io.ByteArrayOutputStream;

import org.json.JSONException;

import com.mycom.lib.gifimageview.GifDataDownloader;
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ItemFeedGif implements Item {
 
    public final ParseObject	feed;
    
    private HomeActivity m_context;
    
    public TextView tvCaption = null;
    public GifImageView ivPhoto = null;
    

    public ItemFeedGif(Context context, ParseObject feed) {
    	this.m_context = (HomeActivity)context;
    	
    	this.feed = feed;
    }

    
    
    @Override
    public int getViewType() {
        return RowType.IMAGE_GIF_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
        	convertView = (View) inflater.inflate(R.layout.list_feed_gif, null);
        }
 
		tvCaption = (TextView) convertView.findViewById(R.id.txtCaption);
		
		
		ivPhoto = (GifImageView) convertView.findViewById(R.id.ivPhoto);
		ivPhoto.setScaleType(ScaleType.CENTER_CROP);
		
		
		final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressPhoto);

		try {
			
	       	String caption = feed.getString("description");
	       	tvCaption.setText(caption);
	       	
	       	
	       	////////////////////////////////////////////////////
	       	
	       	ParseFile imageFile = (ParseFile)feed.get("picture");
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
						
						FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) ivPhoto.getLayoutParams();
						int ivWidth = bmp.getWidth();
						int ivHeight = bmp.getHeight();
				    	int virturlHeight = 320;
				    	if (ivWidth != 0 && ivHeight != 0)
				    		virturlHeight= ((HomeActivity)m_context).getScreenWidth() * ivHeight / ivWidth;
				    	param.height = virturlHeight;
				    	ivPhoto.setLayoutParams(param);
				    	
				    	progressBar.setVisibility(View.GONE);
				    	
		            	ivPhoto.setBytes(data);
		            	ivPhoto.startAnimation();

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
	       	
	       	
//	       	String imgUrl = imageFile.getUrl();
//	        new GifDataDownloader() {
//	            @Override
//	            protected void onPostExecute(final byte[] bytes) {
//	            	Bitmap loadedImage = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
//	            	
//	        		FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) ivPhoto.getLayoutParams();
//					int ivWidth = loadedImage.getWidth();
//					int ivHeight = loadedImage.getHeight();
//			    	int virturlHeight = 320;
//			    	if (ivWidth != 0 && ivHeight != 0)
//			    		virturlHeight= ((HomeActivity)m_context).getScreenWidth() * ivHeight / ivWidth;
//			    	param.height = virturlHeight;
//			    	ivPhoto.setLayoutParams(param);
//			    	
//			    	progressBar.setVisibility(View.GONE);
//			    	
//	            	ivPhoto.setBytes(bytes);
//	            	ivPhoto.startAnimation();
//	            }
//	        }.execute(imgUrl);
	       	
/*	       	
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
					 
						FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) ivPhoto.getLayoutParams();
						int ivWidth  = loadedImage.getWidth();
						int ivHeight = loadedImage.getHeight();
				    	int virturlHeight = 320;
				    	if (ivWidth != 0 && ivHeight != 0)
				    		virturlHeight= ((HomeActivity)m_context).getScreenWidth() * ivHeight / ivWidth;
				    	param.height = virturlHeight;
				    	ivPhoto.setLayoutParams(param);
				    	
				    	progressBar.setVisibility(View.GONE);
				    	
				    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
				    	loadedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
				    	byte[] byteArray = stream.toByteArray();
				    	ivPhoto.setBytes(byteArray);
				    	ivPhoto.startAnimation();
				    	
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
	       
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return convertView;
    }
 
}
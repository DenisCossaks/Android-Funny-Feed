package com.custom.items;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class ItemFeedMovie implements Item {
 
    public final ParseObject	feed;
    
    private HomeActivity m_context;
    
    public TextView tvCaption = null;
    public VideoView viewVideo = null;
    

    public ItemFeedMovie(Context context, ParseObject feed) {
    	this.m_context = (HomeActivity)context;
    	
    	this.feed = feed;
    }

    
    
    @Override
    public int getViewType() {
        return RowType.IMAGE_MOVIE_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
        	convertView = (View) inflater.inflate(R.layout.list_feed_movie, null);
        }
 
		tvCaption = (TextView) convertView.findViewById(R.id.txtCaption);
		
		
		viewVideo = (VideoView) convertView.findViewById(R.id.vvVideo);
		
		
		final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressPhoto);

		try {
			
	       	String caption = feed.getString("description");
	       	tvCaption.setText(caption);
	       	
	       	
	       	////////////////////////////////////////////////////
	       	
	       	ParseFile videoFile = (ParseFile)feed.get("movie");
	       	String key = feed.getObjectId();
	       	
	       	String urlPath = videoFile.getUrl();
	       	String extension = urlPath.substring((urlPath.lastIndexOf(".") + 1), urlPath.length());

	       	final String url = key + "." + extension;
	       	
	       	if (viewVideo.isPlaying()) {
	       		return convertView;
	       	}
	       	
	       	viewVideo.setVisibility(View.INVISIBLE);
	       	videoFile.getDataInBackground(new GetDataCallback() {
				
				@Override
				public void done(byte[] data, ParseException e) {
					// TODO Auto-generated method stub
					if (e == null) {

				    	progressBar.setVisibility(View.GONE);

				    	writeByteToFile(url, data);
						
				    	String filePath = Environment.getExternalStorageDirectory() + "/FunnyFeed/" + url;
				    	
				    	viewVideo.setVisibility(View.VISIBLE);
				    	viewVideo.setVideoPath(filePath);
				    	
//				    	viewVideo.setMediaController(new MediaController(m_context));
					    
				    	viewVideo.setOnErrorListener(new OnErrorListener() {

							@Override
							public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
								// TODO Auto-generated method stub
				                
				                return false;
							}
					    });

				    	viewVideo.setOnPreparedListener(new OnPreparedListener() {

				            public void onPrepared(MediaPlayer mp) {
				            	
				            	
				            	
				            	if (!viewVideo.isPlaying()) {
				            	viewVideo.requestFocus();
				            	viewVideo.start();
				            	
				            	mp.setLooping(true);
				            	}
				            }
					    });
					    
					    

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
	       	
	       
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return convertView;
    }
 
    private boolean isExistFile(String filename) {
    	
    	String filePath = Environment.getExternalStorageDirectory() + "/FunnyFeed/" + filename;
    	
    	File file = new File(filePath);
    	return file.exists();
    }
    
    private void writeByteToFile(String filename, byte[] data) {
    	String ext_storage_state = Environment.getExternalStorageState();
    	String folderPath = Environment.getExternalStorageDirectory() + "/FunnyFeed";
        File mediaStorage = new File(folderPath);
        if (ext_storage_state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            if (!mediaStorage.exists()) {
                mediaStorage.mkdirs();
            } 
            
            File fileStorage = new File(folderPath + "/" + filename);
            //write file writing code..

            try {

            FileOutputStream fos=new FileOutputStream(fileStorage);
            	try {
            		fos.write(data);
            		fos.close();
            	} catch (IOException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
            	}           
            } catch (FileNotFoundException e) {
            	// TODO Auto-generated catch block
            	e.printStackTrace();
            }
        }
        else
        {
            //Toast message sd card not found..
        }
    }
    private byte[] readByteFromFile(String filename) {
    	
    	String filePath = Environment.getExternalStorageDirectory() + "/FunnyFeed/" + filename;
    	
    	FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(filePath);
			
			ByteArrayOutputStream output = new ByteArrayOutputStream();
	        byte [] buffer               = new byte[ 1024 ];

	        int n = 0;
	        while (-1 != (n = inputStream.read(buffer))) {
	           output.write(buffer, 0, n);
	        }
	        inputStream.close();
	        
	        return output.toByteArray();
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
        
    }
}
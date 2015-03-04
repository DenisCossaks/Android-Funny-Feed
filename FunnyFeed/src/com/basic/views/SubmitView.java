package com.basic.views;


import java.io.ByteArrayOutputStream;

import com.mycom.customcontrol.CustomButtonTouchListener;
import com.mycom.data.Const;
import com.oxi.idivertido.HomeActivity;
import com.oxi.idivertido.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SubmitView extends BaseView {

	HomeActivity mContext;
	public static SubmitView g_submitView;
	
	
	Bitmap m_bmp = null;
	
	EditText tvName, tvEmail, tvCaption = null;
	
	ProgressDialog progress = null;
	
	public SubmitView(Context context) {
		super(context);
		
		initView(context);
	}

	public SubmitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        
        initView(context);
    }

    public SubmitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initView(context);
    }

    
    private void initView(Context context){

    	mContext = (HomeActivity)context;
    	g_submitView = this;
    	
    	View.inflate(context, R.layout.view_submit, this);
    	  
    }
    
    public void init(Bitmap bmp) {
    	
    	mContext.addHistory(getClass().getSimpleName(), this);
    	
    	m_bmp = bmp;
    	
    	tvName = (EditText) findViewById(R.id.editName);
    	tvEmail = (EditText) findViewById(R.id.editEmail);
    	tvCaption = (EditText) findViewById(R.id.editCaption);
    	
    	ImageView ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
    	ivPhoto.setImageBitmap(m_bmp);
    	
    	Button btnCancel = (Button) findViewById(R.id.btnCancel);
    	btnCancel.setOnTouchListener(CustomButtonTouchListener.getInstance());
    	btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mContext.hideKeyboard();
				onClose();
			}
		});
    	
    	Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
    	btnSubmit.setOnTouchListener(CustomButtonTouchListener.getInstance());
    	btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mContext.hideKeyboard();
				onSubmit();
			}
		});
    	
    	
    }

    public void onClose() {
    	
    	mContext.removeHistory(getClass().getSimpleName());
    	
    	Animation in = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
    	g_submitView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	ViewGroup vg = (ViewGroup)(g_submitView.getParent());
    	    	vg.removeView(g_submitView);
    	    	
    	    }
    	});
    }
    private void onSubmit() {
    	final String name = tvName.getText().toString();
    	final String email = tvEmail.getText().toString();
    	final String caption = tvCaption.getText().toString();
    	
    	if (name.length() < 1) {
    		Const.showToastMessage("Please input your name", mContext);
    		return;
    	}
    	if (email.length() < 1) {
    		Const.showToastMessage("Please input your email", mContext);
    		return;
    	}
    	if (caption.length() < 1) {
    		Const.showToastMessage("Please input image caption", mContext);
    		return;
    	}
    	if (m_bmp == null) {
    		Const.showToastMessage("You did not choose image", mContext);
    		return;
    		
    	}
    	
    	progress = new ProgressDialog(mContext);
		progress.setCancelable(false);
		progress.setMessage("Uploading....");

    	progress.show();
    	
    	new Thread(new Runnable() {
			
			public void run() {
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				m_bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] image = stream.toByteArray();
				
				ParseFile file = new ParseFile("picture.png", image);
				file.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
									
						}
						else {
							m_handler.sendEmptyMessage(-1);		
						}
					}
				});
				
				ParseObject imgupload = new ParseObject("UserFeed");
				imgupload.put("author", name);
				imgupload.put("email",  email);
				imgupload.put("description", caption);
				imgupload.put("picture", file);
				
				imgupload.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							m_handler.sendEmptyMessage(0);			
						}
						else {
							Const.showToastMessage("Uploading error", mContext);
						}
					}
					
					
				});
				
				
 			}
			
		}).start();
    }
    
    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if (progress != null) {
				progress.hide();
				progress = null;
			}
			
			
			switch (msg.what) {
			case 0:
				
//				mContext.refreshTimeline();
//				onClose();
				
				mContext.gotoThanksView();
				
				ViewGroup vg = (ViewGroup)(g_submitView.getParent());
    	    	vg.removeView(g_submitView);
				
				break;
				
			case -1:
				
				Const.showMessage("", "Uploading failed.", (HomeActivity)mContext);

				break;
			}
			
		}
    };
}

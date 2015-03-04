package com.oxi.idivertido;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.basic.views.FAQView;
import com.basic.views.PhotoDetailView;
import com.basic.views.SubmitView;
import com.basic.views.ThanksView;
import com.basic.views.TimelineView;
import com.chupamobile.android.ratemyapp.RateMyApp;
import com.mycom.customcontrol.CustomButtonTouchListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeActivity extends BaseActivity {

	public ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions optFull;
	public ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	
	public RelativeLayout boardLayout = null;
	ImageView btnTimeline = null, btnSubmit = null, btnFAQ = null;
	ImageView btnTimelineSel = null, btnFAQSel = null;
	
	
	TimelineView mTimelineView = null;
	SubmitView mSubmitView = null;
	FAQView mFaqView = null;
	
	private RateMyApp rate;
	
	
	ArrayList<String> arrHistory = new ArrayList<String>();
	ArrayList<Object> arrHistoryObj = new ArrayList<Object>();
	
	public void addHistory(String viewName, Object obj){
		arrHistory.add(viewName);
		arrHistoryObj.add(obj);
	}
	public void removeHistory(String viewName){
		if (arrHistory.contains(viewName)) {
			arrHistory.remove(arrHistory.size()-1);
			arrHistoryObj.remove(arrHistoryObj.size() -1);
		}
	}
	public void clearHistory() {
		arrHistory.clear();
		arrHistoryObj.clear();
	}
	
	public void showRating() {
        //Initialize the RateMyApp component
        //set the title, days till the user is prompted and the no. of launches till the user is prompted
        rate = new RateMyApp(this, getResources().getString(R.string.app_name), 0, 4);
        
        //make all text white
        rate.setTextColor(Color.WHITE);
        
        //set a custom message
//        rate.setMessage(getResources().getString(R.string.like_rate));
        
        //set a custom text size
        rate.setTextSize(16);
        rate.start();
	}
	
    @Override
    public void onPause() {
    	super.onPause();
    	if(rate.getDialog() != null && rate.getDialog().isShowing()) {
      
    		rate.getDialog().dismiss();
    	}
    }
    
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		
		if (arrHistory.size() != 0) {
        	String viewName = arrHistory.get(arrHistory.size() -1);
        	Object obj = arrHistoryObj.get(arrHistoryObj.size() -1);
        	
        	if (viewName.equals("PhotoDetailView")) {
        		((PhotoDetailView)obj).onBack();
        	}
        	if (viewName.equals("SubmitView")) {
        		((SubmitView)obj).onClose();
        	}
        }
		else {
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle(getResources().getString(R.string.do_you_want_to_exit_this_app));
			dialog.setPositiveButton(getResources().getString(R.string.NO), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					dialog.cancel();

				}
			});

			dialog.setNegativeButton(getResources().getString(R.string.YES), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					dialog.cancel();
					
					finish();
				}
			});
			
			dialog.show();

		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		com.facebook.AppEventsLogger.activateApp(HomeActivity.this, "312401348947190");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		showRating();
		
		
    	optFull = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_empty)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		
    	boardLayout = (RelativeLayout) findViewById(R.id.boardLayout);
    	
    	btnTimeline = (ImageView) findViewById(R.id.btnTimeline);
    	btnTimeline.setOnTouchListener(CustomButtonTouchListener.getInstance());
    	btnTimeline.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				gotoTimeline();
			}
		});
    	
    	btnSubmit = (ImageView) findViewById(R.id.btnSubmit);
    	btnSubmit.setOnTouchListener(CustomButtonTouchListener.getInstance());
    	btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showActionSheet();
			}
		});
    	
    	btnFAQ = (ImageView) findViewById(R.id.btnFaq);
    	btnFAQ.setOnTouchListener(CustomButtonTouchListener.getInstance());
    	btnFAQ.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoFAQView();
			}
		});
    	
    	btnTimelineSel = (ImageView) findViewById(R.id.layoutTimelineSel);
    	btnFAQSel = (ImageView) findViewById(R.id.layoutFaqSel);
    	
    	gotoTimeline();
	}

	private void clearViews() {
//		if (mTimelineView != null) {
//			mTimelineView = null;
//		}
//		if (mSubmitView != null) {
//			mSubmitView = null;
//		}
//		if (mFaqView != null) {
//			mFaqView = null;
//		}
	}
	
	public void refreshTimeline() {
		if (mTimelineView != null) {
			mTimelineView.onRefresh();
		}
	}
	private void gotoTimeline(){
		
//		boardLayout.removeAllViews();
//		clearViews();
		if (mFaqView != null) {
			boardLayout.removeView(mFaqView);
			mFaqView = null;
		}
		if (mSubmitView != null) {
			boardLayout.removeView(mSubmitView);
			mSubmitView = null;
		}
		
		
		if (mTimelineView == null) {
			mTimelineView = new TimelineView(this);
			mTimelineView.init();
		
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT); 
			boardLayout.addView(mTimelineView, params);
		}
		
    	refreshButton();
	}
	
	private void gotoSubmitView(Bitmap bmp) {
		
		mSubmitView = new SubmitView(this);
		mSubmitView.init(bmp);
		
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT);
    	addContentView(mSubmitView, params);
//    	boardLayout.addView(mSubmitView, params);
		
    	refreshButton();
	}
	
	private void gotoFAQView() {
		
//		boardLayout.removeAllViews();
//		clearViews();
		
		
		mFaqView = new FAQView(this);
		mFaqView.init();
		
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	boardLayout.addView(mFaqView, params);
	
    	refreshButton();
	}
	
	public void gotoThanksView() {
		
		ThanksView mThanksView = new ThanksView(this);
		
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT);
    	addContentView(mThanksView, params);
	}
	
	
	private void refreshButton() {
		btnTimeline.setImageResource(R.drawable.img_btn_timeline_nor);
		btnFAQ.setImageResource(R.drawable.img_btn_faq_nor);
		
		btnTimelineSel.setBackgroundColor(Color.TRANSPARENT);
		btnFAQSel.setBackgroundColor(Color.TRANSPARENT);
		
		
		if (mFaqView != null) {
			btnFAQ.setImageResource(R.drawable.img_btn_faq_sel);
			btnFAQSel.setBackgroundResource(R.drawable.layout_select);
		}
		else if (mTimelineView != null) {
			btnTimeline.setImageResource(R.drawable.img_btn_timeline_sel);
			btnTimelineSel.setBackgroundResource(R.drawable.layout_select);
		} 
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	public int getScreenWidth() {
		DisplayMetrics dimension = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimension);
        int screen_width = dimension.widthPixels;
        int screen_height = dimension.heightPixels;

        return screen_width;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == 1000) {

			try {
				if (data != null) {

					Uri selPhotoUri = data.getData();
					Bitmap bmp = getThumbnail(selPhotoUri);
					
					gotoSubmitView(bmp);
					
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				
			}

		}
	}
	
    private void openImageIntent() {
		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				"Please Choose");

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));

		startActivityForResult(chooserIntent, 1000);
	}

	

	public Bitmap getThumbnail(Uri uri) throws FileNotFoundException,
			IOException {
		final int THUMBNAIL_SIZE = 600;

		InputStream input = getContentResolver().openInputStream(uri);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1)
				|| (onlyBoundsOptions.outHeight == -1))
			return null;

		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
				: onlyBoundsOptions.outWidth;

		double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE)
				: 1.0;

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = this.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}

	private static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0)
			return 1;
		else
			return k;
	}
	
	/*
	 * ActionSheet
	 */
/*	
	public void showActionSheet() {

		final Dialog myDialog = new Dialog(this, R.style.CustomTheme);

		myDialog.setContentView(R.layout.layout_actionsheet);

		TextView tvNewButton = (TextView) myDialog.findViewById(R.id.tvButtonExample);
		tvNewButton.setOnTouchListener(CustomButtonTouchListener.getInstance());
		tvNewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				myDialog.dismiss();
				
				openImageIntent();
			}

		});

		TextView tvCancel = (TextView) myDialog.findViewById(R.id.tvCancel);
		tvCancel.setOnTouchListener(CustomButtonTouchListener.getInstance());
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				myDialog.dismiss();

			}
		});

		myDialog.getWindow().getAttributes().windowAnimations = R.anim.slide_up;
		myDialog.show();
		myDialog.getWindow().setGravity(Gravity.BOTTOM);

	}
*/
	public void showActionSheet() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(getResources().getString(R.string.action_sheet_default));
		dialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();

			}
		});
		
		dialog.setPositiveButton(getResources().getString(R.string.choose_photo), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.cancel();
				
				openImageIntent();
			}
		});
		
		dialog.show();
	}
	
	public void hideKeyboard() {
//		getWindow().setSoftInputMode(
//			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		if(getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//			((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
//		      .toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		}
	}
}

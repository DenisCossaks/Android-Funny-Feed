package com.mycom.customcontrol;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@SuppressLint("NewApi")
public class CustomButtonTouchListener implements OnTouchListener {

	static CustomButtonTouchListener globalInstance;
	public CustomButtonTouchListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if( arg1.getAction() == MotionEvent.ACTION_DOWN )
		{
			arg0.setAlpha(0.5f);
		}
		else
		{
			arg0.setAlpha(1.0f);
		}
		return false;
	}
	
	public static CustomButtonTouchListener getInstance()
	{
		if( globalInstance == null )
			globalInstance = new CustomButtonTouchListener();
		return globalInstance;
	}
}

package com.basic.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mycom.customcontrol.XListView.IXListViewListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class BaseView extends LinearLayout  implements IXListViewListener {

	public BaseView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
	}

	public String getCurrentTime() {

		Calendar c = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());

		return formattedDate;
	}
}

package com.basic.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.views.TimelineView.MyListAdapter;
import com.custom.items.Item;
import com.custom.items.ItemFeed;
import com.custom.items.RowType;
import com.custom.items.ItemFaq;
import com.mycom.customcontrol.CustomButtonTouchListener;
import com.mycom.customcontrol.XListView;
import com.oxi.idivertido.HomeActivity;
import com.oxi.idivertido.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ThanksView extends BaseView {

	HomeActivity mContext;
	ThanksView g_thanksView;

	public ThanksView(Context context) {
		super(context);

		initView(context);
	}

	public ThanksView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);

		initView(context);
	}

	public ThanksView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView(context);
	}

	private void initView(Context context) {

		mContext = (HomeActivity) context;
		g_thanksView = this;
		
		
		View.inflate(context, R.layout.view_thanks, this);

		Button btnClose = (Button) findViewById(R.id.btnClose);
		btnClose.setOnTouchListener(CustomButtonTouchListener.getInstance());
		btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ViewGroup vg = (ViewGroup)(g_thanksView.getParent());
    	    	vg.removeView(g_thanksView);
			}
		});
	}

	
}

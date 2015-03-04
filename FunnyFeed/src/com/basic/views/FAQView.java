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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FAQView extends BaseView {

	HomeActivity mContext;

	private ListView mListView = null;
	private MyListAdapter adapter = null;

	ArrayList<ItemFaq> m_items = new ArrayList<ItemFaq>();

	public FAQView(Context context) {
		super(context);

		initView(context);
	}

	public FAQView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);

		initView(context);
	}

	public FAQView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView(context);
	}

	private void initView(Context context) {

		mContext = (HomeActivity) context;

		View.inflate(context, R.layout.view_faq, this);

		mListView = (ListView) findViewById(R.id.listview);

	}

	public String loadJSONFromAsset() {
	    String json = null;
	    try {

	        InputStream is = mContext.getAssets().open("FAQ.json");

	        int size = is.available();

	        byte[] buffer = new byte[size];

	        is.read(buffer);

	        is.close();

	        json = new String(buffer, "UTF-8");


	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    return json;

	}
	
	public void init() {

		m_items.clear();
		
		try {
			
			JSONObject jsonobject = new JSONObject(loadJSONFromAsset());
			JSONArray jarray = (JSONArray) jsonobject.getJSONArray("FAQ");
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jb = (JSONObject) jarray.get(i);
				String title = jb.getString("title");
				String description = jb.getString("content");
				
				m_items.add(new ItemFaq(title, description));
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		refresh();
	}

	public void refresh() {

		if (adapter == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			adapter = new MyListAdapter(mContext, inflater, m_items);
			mListView.setAdapter(adapter);
		} else {
			adapter.setItem(m_items);
			adapter.notifyDataSetChanged();
		}

	}

	public class MyListAdapter extends ArrayAdapter<ItemFaq> {
		private List<ItemFaq> items;
		private LayoutInflater inflater;

		public MyListAdapter(Context context, LayoutInflater inflater,
				List<ItemFaq> items) {
			super(context, 0, items);
			this.items = items;
			this.inflater = inflater;
		}

		public void setItem(List<ItemFaq> arrItems) {
			this.items = arrItems;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// Use getView from the Item interface
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.list_faq, parent, false);

			}

			TextView tvCaption = (TextView) view.findViewById(R.id.txtCaption);
			TextView tvDescription = (TextView) view
					.findViewById(R.id.txtDescription);

			ItemFaq item = items.get(position);

			tvCaption.setText(item.title);
			tvDescription.setText(item.description);

			return view;

		}
	}
}

package com.basic.views;

import java.util.ArrayList;
import java.util.List;
import com.custom.items.Item;
import com.custom.items.ItemFeed;
import com.custom.items.ItemFeedGif;
import com.custom.items.ItemFeedMovie;
import com.custom.items.RowType;
import com.mycom.customcontrol.XListView;
import com.mycom.data.Const;
import com.mycom.data.Global;

import com.oxi.idivertido.HomeActivity;
import com.oxi.idivertido.MyActivity;
import com.oxi.idivertido.R;
import com.oxi.idivertido.VideoActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;



public class TimelineView extends BaseView{

	HomeActivity mContext;
	
	private XListView mListView = null;
	private MyListAdapter adapter = null;

	private FrameLayout layoutNav = null;
	
	
	ProgressDialog progress = null;

	List<ParseObject> m_arrData = null;
	List<Item> m_items = new ArrayList<Item>();
	
	
	int m_nPage = 0;
	
	public TimelineView(Context context) {
		super(context);
		
		initView(context);
	}

	public TimelineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        
        initView(context);
    }

    public TimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initView(context);
    }

    
    private void initView(Context context){

    	mContext = (HomeActivity)context;
    	
    	View.inflate(context, R.layout.view_timeline, this);
    	  
    }
    
    public void init() {
    	
    	layoutNav = (FrameLayout) findViewById(R.id.layoutNav);
    	
    	Drawable drawable = getResources().getDrawable(R.drawable.navigation_back);
    	int ivHeight = drawable.getIntrinsicHeight();
    	int ivWidth = drawable.getIntrinsicWidth();
    	
    	LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) layoutNav.getLayoutParams();
    	int virturlHeight = 50;
    	if (ivWidth != 0 && ivHeight != 0)
    		virturlHeight= getScreenWidth() * ivHeight / ivWidth;
    	param.height = virturlHeight;
    	layoutNav.setLayoutParams(param);
    	layoutNav.setBackgroundResource(R.drawable.navigation_back);
    	
    	
    	
		mListView = (XListView) findViewById(R.id.listview);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
    	
    	
    	initData();
    }
    
	public int getScreenWidth() {
		DisplayMetrics dimension = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dimension);
        int screen_width = dimension.widthPixels;
        int screen_height = dimension.heightPixels;

        return screen_width;
	}
	
    public void initData() {
    	
    	progress = new ProgressDialog(mContext);
		progress.setCancelable(false);
		progress.setMessage(getResources().getString(R.string.loading));

    	progress.show();
    	
    	new Thread(new Runnable() {
			
			public void run() {
				
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Feed");
				query.orderByDescending("_created_at");
				query.setLimit(Global.LIMIT_PAGE);
				query.setSkip(m_nPage * Global.LIMIT_PAGE);
				
				List<ParseObject> feed = null;
				try {
					feed = query.find();
				} catch (ParseException e) {
					m_handler.sendEmptyMessage(-1);
					return;
				}

				if (feed == null || feed.size() == 0) {
					m_handler.sendEmptyMessage(2);
					return;
				}
				
				if (m_arrData == null || m_nPage == 0) {
					m_arrData = feed;
				}
				else {
					m_arrData.addAll(feed);
				}
				
				if (feed.size() < Global.LIMIT_PAGE) {
					m_handler.sendEmptyMessage(1);
					return;
				}
				
				if (m_arrData != null) {
					m_handler.sendEmptyMessage(0);
				}
				
				m_nPage ++;
			}
		}).start();
    }
    
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		m_nPage = 0;
		initData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
		initData();
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
				mListView.setPullLoadEnable(true);
				refresh();
				
				break;
				
				
			case 1:
				refresh();
			case 2:
				mListView.setPullLoadEnable(false);
				break;
				
			case -1:
				
				refresh();
				mListView.setPullLoadEnable(false);
				
				Const.showMessage("", "Loading failed.", (HomeActivity)mContext);

				break;
			}
			
		}
    };
    
    public void refresh() {
    	
    	m_items.clear();
    	
    	if (m_arrData != null) {
    		for (int i = 0 ; i < m_arrData.size() ; i ++) {
        		ParseObject obj = m_arrData.get(i);
        		
        		String itemType = obj.getString("item_type");
        		
        		if (itemType != null && itemType.equalsIgnoreCase("video")) {
        			m_items.add(new ItemFeedMovie(mContext, obj));
        		}
        		else if (itemType != null && 
        				(itemType.equalsIgnoreCase("gif") || itemType.equalsIgnoreCase("image") || itemType.equalsIgnoreCase("youtube_image_url"))){
            		ParseFile imageFile = (ParseFile)obj.get("picture");
            		String imgUrl = imageFile.getUrl();
        			
            		if (imgUrl.contains(".gif")) {
            			m_items.add(new ItemFeedGif(mContext, obj));        			
            		}
            		else {
            			m_items.add(new ItemFeed(mContext, obj));	
            		}
        			
        		}
        		
        	}
    	}
    	
    	if (adapter == null) {
     		LayoutInflater inflater = LayoutInflater.from(mContext);
     	    adapter = new MyListAdapter(mContext, inflater, m_items);
     	    mListView.setAdapter(adapter);
         } else {
             	adapter.setItem(m_items);
     			adapter.notifyDataSetChanged();
         }

         System.out.println("count = " + m_items.size());
         
         	mListView.stopRefresh();
			mListView.stopLoadMore();
			mListView.setRefreshTime(getCurrentTime());
    }
    
    public class MyListAdapter extends ArrayAdapter<Item> {
        private List<Item> items;
        private LayoutInflater inflater;
     

        public MyListAdapter(Context context, LayoutInflater inflater, List<Item> items) {
            super(context, 0, items);
            this.items = items;
            this.inflater = inflater;
        }
        public void setItem(List<Item> arrItems) {
        	this.items = arrItems;
        }
        
        @Override
        public int getViewTypeCount() {
            // Get the number of items in the enum
            return RowType.getValues();
     
        }
     
        @Override
        public int getItemViewType(int position) {
            // Use getViewType from the Item interface
            return items.get(position).getViewType();
        }
     
        @Override
        public int getCount() {
        	// TODO Auto-generated method stub
        	return items.size();
        }
        
        
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Use getView from the Item interface
        	Item item = items.get(position);
        	
        	int type = item.getViewType();
        	
        	View v = item.getView(inflater, convertView);

        	if (type == RowType.IMAGE_ITEM) {
        		ItemFeed itemfeed = (ItemFeed) item;
        		
        		itemfeed.ivPhoto.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View arg0) {
    					onClickPhoto((ImageView)arg0, false);
					}
    			});
        		itemfeed.ivPhoto.setTag(position);
        	}
        	if (type == RowType.IMAGE_GIF_ITEM) {
        		ItemFeedGif itemfeed = (ItemFeedGif) item;
        		
        		itemfeed.ivPhoto.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View arg0) {
    					onClickPhoto((ImageView)arg0, true);
					}
    			});
        		itemfeed.ivPhoto.setTag(position);
        	}
        	
        	return v;
        }
    }
    
    
 	public void onClickPhoto(ImageView imageView, boolean bGif) {
 		
 		int index = Integer.parseInt(imageView.getTag().toString());
 		
 		final ParseObject object = bGif == false ? ((ItemFeed)m_items.get(index)).feed : ((ItemFeedGif)m_items.get(index)).feed;   
 		
 		String itemType = object.getString("item_type");
 		if (itemType.equalsIgnoreCase("youtube_image_url")) {
 			String url = object.getString("youtube_image_url");
 			
 			Intent intent = new Intent(mContext, MyActivity.class);
 			intent.putExtra("youtube_url", url);
 			mContext.startActivity(intent);
 			return;
 		}

/* 		
    	final PhotoDetailView subView = new PhotoDetailView(mContext);

    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    			ViewGroup.LayoutParams.FILL_PARENT,
    			ViewGroup.LayoutParams.FILL_PARENT); 
//    	mContext.boardLayout.addView(subView, params);
    	mContext.addContentView(subView, params);

    	Animation out = AnimationUtils.loadAnimation(mContext, R.anim.slide_left);
    	subView.startAnimation(out);
    	out.setAnimationListener(new Animation.AnimationListener(){
     	    @Override
     	    public void onAnimationStart(Animation arg0) {
     	    }           
     	    @Override 
     	    public void onAnimationRepeat(Animation arg0) {
     	    }           
     	    @Override
     	    public void onAnimationEnd(Animation arg0) {
     	    	subView.init(object);
     	    }
     	});
*/ 		
 		
 	}
    
}

package com.oxi.idivertido;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

public class MyBroadcastReceiver extends ParsePushBroadcastReceiver {

	@Override
	protected void onPushReceive(Context context, final Intent intent)
	{
		Log.i("Push", "Receive Push");
		super.onPushReceive(context, intent);
		
//		ParseManager.getSharedInstance().updateBadgeCount();
//		if( BaseActivity.currentTopActivity == null )
//			return;
//		if( !BaseActivity.currentTopActivity.getClass().equals(ChatActivity.class) )
//		{
//			if( MainActivity.instance != null )
//			{
//				if( MainActivity.instance.currentFragment.getClass().equals(MessagesFragment.class) )
//				{
//					MessagesFragment msgFragment = (MessagesFragment)MainActivity.instance.currentFragment;
//					msgFragment.readMessageUsersFromServer(null);
//				}
//			}
//			
//			new AlertDialog.Builder(BaseActivity.currentTopActivity)
//			.setMessage("You received the message!")
//			.setTitle("Message!")
//			.setPositiveButton("View", new OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					Intent i = new Intent(BaseActivity.currentTopActivity, ChatActivity.class);
//					JSONObject optionParams = null;
//					String otherUserId = null;
//					try {
//						optionParams = new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
//						otherUserId = optionParams.optString(ParseDefine.KEY_PUSH_FROM_USER_ID);
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if(MomCoHelper.currentSelectUser == null || MomCoHelper.currentSelectUser.getObjectId().compareTo(otherUserId) != 0)
//					{
//						i.putExtra("OtherUserId", otherUserId);
//					}
//					BaseActivity.currentTopActivity.startActivity(i);
//				}
//			})
//			.setNegativeButton("No", null)
//			.show();
//		}
//		else
//		{
//			ChatActivity chatView = (ChatActivity)BaseActivity.currentTopActivity;
//			chatView.loadMessagesFromServer();
//		}
	}
	
	@Override
	protected void onPushOpen(final Context context, Intent intent)
	{
		Log.i("Push", "Open Push");

//		super.onPushOpen(context, intent);
		
		Intent i = null;
		i = new Intent(context, HomeActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
		
//		ParseAnalytics.trackAppOpened(intent);
//
//        String uriString = null;
//        try {
//            JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
//            uriString = pushData.optString("uri");
//        } catch (JSONException e) {
//            Log.v("com.parse.ParsePushReceiver", "Unexpected JSONException when receiving push data: ", e);
//        }
//        Class<? extends Activity> cls = getActivity(context, intent);
//        Intent activityIntent;
//        if (uriString != null && !uriString.isEmpty()) {
//            activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
//        } else {
//            activityIntent = new Intent(context, HomeActivity.class);
//        }
//        activityIntent.putExtras(intent.getExtras());
//        if (Build.VERSION.SDK_INT >= 16) {
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//            stackBuilder.addParentStack(cls);
//            stackBuilder.addNextIntent(activityIntent);
//            stackBuilder.startActivities();
//        } else {
//            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(activityIntent);
//        }
	}
}

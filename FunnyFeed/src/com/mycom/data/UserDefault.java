package com.mycom.data;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class UserDefault {

	
	public static SharedPreferences share;
	
 	/*
	 *  memory
	 */

	
	public static void init(Context _act){
		
		share = PreferenceManager.getDefaultSharedPreferences(_act);
	}
	
	
	public static void setIntForKey(int value, String forkey) {
		
		SharedPreferences.Editor editor = share.edit();
		
		editor.putInt(forkey, value);
		
		editor.commit();
		
	}

	public static int getIntForKey(String forkey, int defaultVal){
		int result = 0;
		
		try {
			result = share.getInt(forkey, defaultVal);	
		} catch (Exception e) {
			result = 0;
		}
		
		
		return result;
	}
	
	public static void setFloatForKey(float value, String forkey) {
		
		SharedPreferences.Editor editor = share.edit();
		
		editor.putFloat(forkey, value);
		
		editor.commit();
	}

	public static float getFloatForKey(String forkey, float defValue){
		float result = 0.0f;
		
		try {
			result = share.getFloat(forkey, defValue);
		} catch (Exception e) {
			result = 0.0f;
		}
		
		return result;
	}

	public static void setBoolForKey(boolean value, String forkey) {
		
		SharedPreferences.Editor editor = share.edit();
		
		editor.putBoolean(forkey, value);
		
		editor.commit();
	}

	public static boolean getBoolForKey(String forkey, boolean defValue){
		boolean result = false;
		
		try {
			result = share.getBoolean(forkey, defValue);
		} catch (Exception e) {
			result = false;
		}
		
		return result;
	}

	public static void setStringForKey(String value, String forkey) {
		
		SharedPreferences.Editor editor = share.edit();
		
		editor.putString(forkey, value);
		
		editor.commit();
	}

	public static String getStringForKey(String forkey, String defValue){
		String result = "";
		
		try {
			result = share.getString(forkey, defValue);
		} catch (Exception e) {
			result = "";
		}
		
		return result;
	}
	
	
	public static void setArrayForKey(ArrayList<String> value, String forkey) {
		
		SharedPreferences.Editor editor = share.edit();
		
		editor.putInt(forkey + "size", value.size());
		for (int i = 0; i < value.size(); i++) {
			editor.putString(forkey + i, value.get(i));
		}
		
		editor.commit();
	}

	public static ArrayList<String> getArrayForKey(String forkey){
		
		int size = share.getInt(forkey + "size", 0);
		
		ArrayList<String> result = new ArrayList<String>();
		
		for (int i = 0; i < size; i++) {
			String value = share.getString(forkey + i, "");
			result.add(value);
		}
		
		return result;
	}

	public static void setDictionaryForKey(JSONObject json, String forkey) {
		
		SharedPreferences.Editor editor = share.edit();
		
		String value = "";
		try {
			value = json.toString();
		} catch (Exception e) {
			value = "";
		}
		
		editor.putString(forkey, value);
		
		editor.commit();
	}
	
	public static void removeDictionaryForKey(String forkey) {
		
		SharedPreferences.Editor editor = share.edit();
		
		editor.remove(forkey);
		
		editor.commit();
	}

	public static JSONObject getDictionaryForKey(String forkey){
		
		try {
			return new JSONObject(share.getString(forkey, "{}"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}

}

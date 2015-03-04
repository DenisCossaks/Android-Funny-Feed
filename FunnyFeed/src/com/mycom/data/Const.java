package com.mycom.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class Const {

	
	public static void showMessage(String strTitle, String strMessage, Activity parent) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(parent);
		dialog.setTitle(strTitle);
		dialog.setMessage(strMessage);
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setNegativeButton("Ok", null);
		dialog.show();
	}
	public static void showMessage(Activity parent) {
		String strTitle = "Oops!";
		String strMessage = "We seem to be experiencing a system overload. Please try again in a few minutes.";
		AlertDialog.Builder dialog = new AlertDialog.Builder(parent);
		dialog.setTitle(strTitle);
		dialog.setMessage(strMessage);
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setNegativeButton("Ok", null);
		dialog.show();
	}
	
	public static void showToastMessage(String toasttext, Context context) {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, toasttext, duration);
		toast.show();
	}
	
	public static int toInt(String str) {
		int result = -1;

		try {
			float fRet = Float.parseFloat(str);
			
			result = (int) fRet;

		} catch (Exception e) {
			// TODO: handle exception
			result = 0;
		}

		return result;

	}
	
	public static float toFloat(String str) {
		float result = 0.0f;

		// result = Integer.parseInt(str);

		try {
			result = Float.parseFloat(str);
		} catch (Exception e) {
			// TODO: handle exception
			result = 0.0f;
		}

		return result;

	}
	
	public static double toDouble(String str) {
		double result = 0.0f;

		try {
			result = Double.parseDouble(str);
		} catch (Exception e) {
			// TODO: handle exception
			result = 0.0f;
		}

		return result;

	}

	
	public static void Debug(String _debug) {
		System.out.println(_debug);

	}
}

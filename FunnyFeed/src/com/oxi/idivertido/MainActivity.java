package com.oxi.idivertido;


import com.mycom.data.Global;
import com.parse.Parse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	
		Thread thread = new Thread(new Runnable(){

			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				handler.post(new Runnable() {
					public void run() {
						gotoNextScreen();
					}
				});
				
			}
			
		});
		thread.start();
		
	}

	private static Handler handler = new Handler(); 

	private void gotoNextScreen() {

    	
		Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    	
    	overridePendingTransition(R.anim.fadein, R.anim.fade_hold);
		finish();

    }
	
}

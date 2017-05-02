package com.doubleghost.injecttest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.doubleghost.utils.FileUtils;
import com.doubleghost.utils.ShellUtils;
import com.doubleghost.utils.ShellUtils.CommandResult;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	final static String   TAG	= "MainActivity";
	
	private Button mButtonStartInjectServer;
	private Button mButtonStartService;
	private Button mButtonStopService;
	private boolean injectServerRunning = false;
	
	Socket inetSocket = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	

		try {
			FileUtils.copyAssetFileToFilesDir(this,"InjectServer.jar");
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CommandResult result = ShellUtils.execCommand("chmod 777 "+this.getFilesDir() + "/" + "InjectServer.jar", false);
		Log.d(TAG,"CommandResult result"+result.result+"; success:"+result.successMsg+"; error:"+result.errorMsg);
		
		//m_LocalSocket = new LocalSocket();
		
		mButtonStartInjectServer = (Button) findViewById(R.id.startinject);
		mButtonStartService = (Button) findViewById(R.id.start);
		mButtonStartInjectServer.setOnClickListener(this);
		mButtonStartService.setOnClickListener(this);
		
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//stopService(intent);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}
	
	 Handler mHandler = new Handler(){
	        public void handleMessage(android.os.Message msg) {	            
	            if (msg.what == 1) {
	            	mButtonStartInjectServer.setText("启动键值映射成功");
	            }
	            else if(msg.what == 2)
	            {
	            	mButtonStartInjectServer.setText("启动键值映射超时，请退出程序");
	            }
	            else if(msg.what == 3)
	            {
	            	mButtonStartInjectServer.setText("正在启动键值映射");
	            }
	            else if(msg.what == 4)
	            {
	            	mButtonStartInjectServer.setText("开启键值映射失败，没有root权限");
	            }
	            
	        };
	    };

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this, ControllerListenerService.class);
		if(v.getId() == R.id.startinject)
		{
			if(!injectServerRunning)
			{
				new Thread(new TestInjectThread()).start();
			}
			else
			{
				mButtonStartInjectServer.setText("键值映射已开启");
			}
		}
		if(v.getId() == R.id.start)
		{
			Log.d(TAG,"button1");
			startService(intent);
		}
		
	}
	
	public boolean isInjectServerRuning()
	{
		try {
			inetSocket = new Socket(InetAddress.getLocalHost(), 10080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		try {
			inetSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//m_LocalSocket = null;
		inetSocket = null;
		injectServerRunning = true;
		return true;
	}


	
    class StartInjectThread implements Runnable{
		public void run(){
				List<String> commnandList = new ArrayList<String>();
				commnandList.add("setenforce 0");
				commnandList.add("rm -Rf /data/local/tmp/.gamepadtool");
				commnandList.add("mkdir /data/local/tmp/.gamepadtool");
				commnandList.add("chmod 777 /data/local/tmp/.gamepadtool");
				commnandList.add("mkdir /data/local/tmp/.gamepadtool/dalvik-cache");
				commnandList.add("chmod 777 /data/local/tmp/.gamepadtool/dalvik-cache");
				commnandList.add("cp /data/data/com.doubleghost.injecttest/files/InjectServer.jar /data/local/tmp/.gamepadtool");
				commnandList.add("dd if=/data/data/com.doubleghost.injecttest/files/InjectServer.jar of=/data/local/tmp/.gamepadtool/InjectServer.jar");
				commnandList.add("chmod 777 /data/local/tmp/.gamepadtool/InjectServer.jar");
				commnandList.add("chown shell /data/local/tmp/.gamepadtool/InjectServer.jar");
				commnandList.add("export CLASSPATH=/data/local/tmp/.gamepadtool/InjectServer.jar");
				commnandList.add("export ANDROID_DATA=/data/local/tmp/.gamepadtool");
				commnandList.add("trap \"\" HUP");
				commnandList.add("exec app_process /data/local/tmp/.gamepadtool com.doubleghost.inject.InjectServer &");
				CommandResult result = ShellUtils.execCommand(commnandList, true);
				Log.d(TAG,"CommandResult result"+result.result+"; success:"+result.successMsg+"; error:"+result.errorMsg);
			}
	};
	
	class CheckInjectThread implements Runnable{
		private int checkcount = 0;
		public void run()
		{
			while(true)
			{
				if(isInjectServerRuning())
				{
					mHandler.sendEmptyMessage(1);
					break;
				}
				else
				{
					checkcount++;
					if(checkcount>20)
					{
						mHandler.sendEmptyMessage(2);
						break;
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}
    class TestInjectThread implements Runnable{
		public void run(){
	        	try {	
	        		inetSocket = new Socket(InetAddress.getLocalHost(), 10080);
					inetSocket.getOutputStream().close();
					inetSocket.close();
					injectServerRunning = true;
					mHandler.sendEmptyMessage(1);
					return ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					injectServerRunning = false;
				}
	        	
    			Log.d(TAG,"startinject");
    			mHandler.sendEmptyMessage(3);
    			if(ShellUtils.checkRootPermission())
    			{
    				new Thread(new StartInjectThread()).start();
    				new Thread(new CheckInjectThread()).start();
    			}
    			else
    			{
    				mHandler.sendEmptyMessage(4);
    			}
    			
			}
	};
	
}

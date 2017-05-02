package com.doubleghost.injecttest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.doubleghost.injecttest.MainActivity.CheckInjectThread;
import com.doubleghost.utils.ShellUtils;
import com.doubleghost.utils.ShellUtils.CommandResult;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class ControllerListenerService extends Service
{
	final static String   TAG	= "ControllerListenerService";
    static final int PRESS = 0;
    static final int SWIPE = 1;
    static final int UP = 0;
    static final int DOWN = 1;

	Socket inetSocket = null;
	
	IntentFilter intentFilter = new IntentFilter();
	int m_displaywidth;
	int m_displayheight;
	//InputDevice m_injectdev;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();		
		WindowManager wm = (WindowManager) getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);
		m_displaywidth = wm.getDefaultDisplay().getWidth();
	    m_displayheight = wm.getDefaultDisplay().getHeight();
	    Log.d(TAG, "m_displaywidth:"+ m_displaywidth+"  m_displayheight:"+m_displayheight);

		new SocketLooperThread().start();
		
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		new TestThread().start();
	}
	byte[] key={0,0,0,0,0,0,0,0,0,0,0};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "Service destroyed.");
		try {
			//m_LocalSocket.getOutputStream().close();
			inetSocket.getOutputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	public Handler socketmHandler;      
	class SocketLooperThread extends Thread {    
	      
	    public void run() {    
	        Looper.prepare();    
	    
	        socketmHandler = new Handler() {    
	            public void handleMessage(Message msg) {    
	                // process incoming messages here    
	            	if(msg.what == 1)
	            	{
		            	try {					
		            		inetSocket = new Socket(InetAddress.getLocalHost(), 10080);
							inetSocket.getOutputStream().write(key);
							inetSocket.getOutputStream().close();
							inetSocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	}
	            }    
	        };        
	        Looper.loop();    
	    }    
	}    
	
	int x1 = 320;
	int y1 = 480;
	int x2 = 960;
	int y2 = 480;
	class TestThread extends Thread {    
	      
	    public void run() {    
	    	while(true)
	    	{
	    	try {
	    	//这是划动
				sleep(2000);
				key[0]=SWIPE;
				key[1]=DOWN;
				key[2]=(byte) (x1&0xFF);
				key[3]=(byte) ((x1>>8)&0xFF);
				key[4]=(byte) (y1&0xFF);
				key[5]=(byte) ((y1>>8)&0xFF);
				key[6]=(byte) (x2&0xFF);
				key[7]=(byte) ((x2>>8)&0xFF);
				key[8]=(byte) (y2&0xFF);
				key[9]=(byte) ((y2>>8)&0xFF);
				
				socketmHandler.sendEmptyMessage(1);
				sleep(2000);
				key[1]=UP;
				socketmHandler.sendEmptyMessage(1);
				
			//这是点击
//				sleep(2000);
//				key[0]=PRESS;
//				key[1]=DOWN;
//				key[2]=(byte) (x1&0xFF);
//				key[3]=(byte) ((x1>>8)&0xFF);
//				key[4]=(byte) (y1&0xFF);
//				key[5]=(byte) ((y1>>8)&0xFF);
//				socketmHandler.sendEmptyMessage(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	}
	    	
	    }    
	}
}
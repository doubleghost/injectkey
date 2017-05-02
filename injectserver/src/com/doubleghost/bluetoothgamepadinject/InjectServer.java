package com.doubleghost.bluetoothgamepadinject;

import android.net.LocalServerSocket;
import android.net.LocalSocket;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;

import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputDevice;

import java.io.PrintStream;
public class InjectServer {
	private int mInetSocketPort;
    private String mLocalSocketName;
    static final int PRESS = 0;
    static final int SWIPE = 1;
    static final int UP = 0;
    static final int DOWN = 1;
    
    InputUtils mInputUtils = null;
    public InjectServer(String localSocketName, int inetSocketPort) {
        mLocalSocketName = localSocketName;
        mInetSocketPort = inetSocketPort;
    }
    public InjectServer() {
        mLocalSocketName = "inject_servers_socket";
        mInetSocketPort = 10080;
        mInputUtils = new InputUtils();
    }
    
    public static void main(String[] args) {
        Process.setArgV0("com.doubleghost.bluetoothgamepadinject");
        Log.d("InjectServer", "InjectServer: Starting ...");
        System.err.println("InjectService: start success!");
        InjectServer instance = null;
        try {
                instance = new InjectServer();
                instance.execute();
        } catch(Exception ex) {
            ex.printStackTrace();
            return;
        }
        
    }
    public void execute() {
    	long nowtime = 0;
    	Log.e("InjectServer","execute");
    	ServerSocket mServerSocket = null;
    	try { 
    		mServerSocket = new ServerSocket(mInetSocketPort);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	while(true)
    	{
    		try {
				//LocalSocket receiver  = mlocalServerSocket.accept();
    			Socket receiver  = mServerSocket.accept();
				InputStream input = receiver.getInputStream();
				while(true)
				{
					byte[] key={0,0,0,0,0,0,0,0,0,0,0};
					int ret = input.read(key);
					if(ret == -1)
					{
						input.close();
						receiver.close();
						break;					
					}
					int x1=0;int y1=0; int x2=0;int y2=0;
					
					x1 = key[2]&0xFF |((key[3]&0xFF)<<8);
					y1 = key[4]&0xFF |((key[5]&0xFF)<<8);
					x2 = key[6]&0xFF |((key[7]&0xFF)<<8);
					y2 = key[8]&0xFF |((key[9]&0xFF)<<8);
					
					Log.e("InjectServer","localsocket position:"+x1+","+y1+","+x2+","+y2+","+"command:"+key[0]+","+"action"+key[1]+",ret="+ret);
					if(PRESS == key[0])
					{
						if(DOWN == key[1])
						{
							nowtime = SystemClock.uptimeMillis();
							if(key[10]=='B')
							{
								//mInputUtils.sendTapB(InputDevice.SOURCE_TOUCHSCREEN,x1, y1);
								mInputUtils.sendTapDownB(nowtime, x1, y1);
							}
							else 
							{
								//mInputUtils.sendTap(InputDevice.SOURCE_TOUCHSCREEN,x1, y1);
								mInputUtils.sendTapDown(nowtime, x1, y1);
							}
						}
						else if(UP == key[1])
						{
							if(key[10]=='B')
							{
								mInputUtils.sendTapUpB(nowtime, x1, y1);
							}
							else
							{
								mInputUtils.sendTapUp(nowtime, x1, y1);
							}
						}
					}
					else if(SWIPE == key[0])
					{
						if(DOWN == key[1])
						{
							nowtime = SystemClock.uptimeMillis();
							if(key[10]=='B')
							{
								mInputUtils.sendSwipeStartB(x1,y1,x2,y2,nowtime);
							}
							else
							{
							mInputUtils.sendSwipeStart(x1,y1,x2,y2,nowtime);
							}
						}
						else if(UP == key[1])
						{
							if(key[10]=='B')
							{
								mInputUtils.sendSwipeStopB(x1,y1,x2,y2,nowtime);
							}
							else
							{
							mInputUtils.sendSwipeStop(x1,y1,x2,y2,nowtime);
							}
						}
					}
					
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

    	}
    }
    
    
}
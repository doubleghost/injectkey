package com.doubleghost.inject;
import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.input.IInputManager;
import android.os.SystemClock;
import android.os.ServiceManager;
import android.os.SystemService;
import android.app.Service;
import android.os.IBinder;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.os.RemoteException;

public class InputUtils{
	private static final String TAG = "InputUtils";
	public IInputManager inputManager;
	InputUtils(){
		IBinder imBinder = ServiceManager.getService("input");  
		inputManager = IInputManager.Stub.asInterface(imBinder);
	}
	public static KeyEvent getKeyEvent(int action, int code) {
        return KeyEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), action, code, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, null);
    }
	
    public  void sendTap(int inputSource, float x, float y) {
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x, y, 1.0f);
        injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x, y, 0.0f);
    }
    
    public  void sendTapB(int inputSource, float x, float y) {
        long now = SystemClock.uptimeMillis();
        injectMotionEventB(inputSource, MotionEvent.ACTION_DOWN, now, x, y, 1.0f);
        injectMotionEventB(inputSource, MotionEvent.ACTION_UP, now, x, y, 0.0f);
    }
   
    public  void sendSwipe(int inputSource, float x1, float y1, float x2, float y2) {
        final int NUM_STEPS = 11;
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x1, y1, 1.0f);
        for (int i = 1; i < NUM_STEPS; i++) {
            float alpha = (float) i / NUM_STEPS;
            injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now, lerp(x1, x2, alpha),
                    lerp(y1, y2, alpha), 1.0f);
        }
        injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x1, y1, 0.0f);
    }
    
    public  void sendSwipeUnUP(int inputSource, float x1, float y1, float x2, float y2,long now) {
    	final int NUM_STEPS = 11;
        injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x1, y1, 1.0f);
        for (int i = 1; i < NUM_STEPS; i++) {
            float alpha = (float) i / NUM_STEPS;
            injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now, lerp(x1, x2, alpha),
                    lerp(y1, y2, alpha), 1.0f);
        }
    }
    public  void sendSwipeUP(int inputSource, float x1, float y1, float x2, float y2,long now) {
        injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x1, y1, 0.0f);
    }
    
    public  void sendSwipeStart(float x1, float y1, float x2, float y2,long now) {
    	final int NUM_STEPS = 11;
        injectMotionEvent(InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_DOWN, now, x1, y1, 1.0f);
        for (int i = 1; i < NUM_STEPS; i++) {
            float alpha = (float) i / NUM_STEPS;
            injectMotionEvent(InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_MOVE, now, lerp(x1, x2, alpha),
                    lerp(y1, y2, alpha), 1.0f);
        }
    }
    public  void sendSwipeStop(float x1, float y1, float x2, float y2,long now) {
        injectMotionEvent(InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_UP, now, x1, y1, 0.0f);
    }
    
    public void sendSwipeDown(float x1, float y1,long now)
    {
    	injectMotionEvent(InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_DOWN, now, x1, y1, 1.0f);
    }
    
    public  void sendSwipeStartB(float x1, float y1, float x2, float y2,long now) {
    	final int NUM_STEPS = 11;
        injectMotionEventB(InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_DOWN, now, x1, y1, 1.0f);
        for (int i = 1; i < NUM_STEPS; i++) {
            float alpha = (float) i / NUM_STEPS;
            injectMotionEventB(InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_MOVE, now, lerp(x1, x2, alpha),
                    lerp(y1, y2, alpha), 1.0f);
        }
    }
    public  void sendSwipeStopB(float x1, float y1, float x2, float y2,long now) {
        injectMotionEventB(InputDevice.SOURCE_TOUCHSCREEN, MotionEvent.ACTION_UP, now, x1, y1, 0.0f);
    }
    
    public  void sendMove(int inputSource, float dx, float dy) {
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now, dx, dy, 0.0f);
    }

    public  void injectKeyEvent(KeyEvent event) {
        Log.i(TAG, "injectKeyEvent: " + event);
        try {
        inputManager.injectInputEvent(event,
                InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);//INJECT_INPUT_EVENT_MODE_ASYNC;INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH
        }catch (RemoteException e) {
            System.err.println(e.toString());
            return;
        }
    }
    public  void injectMotionEvent(int inputSource, int action, long when, float x, float y, float pressure) {
        final float DEFAULT_SIZE = 1.0f;
        final int DEFAULT_META_STATE = 0;
        final float DEFAULT_PRECISION_X = 1.0f;
        final float DEFAULT_PRECISION_Y = 1.0f;
        final int DEFAULT_DEVICE_ID = 0;
        final int DEFAULT_EDGE_FLAGS = 0;
        MotionEvent event = MotionEvent.obtain(when, when, action, x, y, pressure, DEFAULT_SIZE,
                DEFAULT_META_STATE, DEFAULT_PRECISION_X, DEFAULT_PRECISION_Y, DEFAULT_DEVICE_ID,
                DEFAULT_EDGE_FLAGS);
        event.setSource(inputSource);
        Log.i(TAG, "injectMotionEvent: " + event);
        try {
        inputManager.injectInputEvent(event,
                InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
        }catch (RemoteException e) {
            System.err.println(e.toString());
            return;
        }
    }
    public  void injectMotionEventB(int inputSource, int action, long when, float x, float y, float pressure) {
        final float DEFAULT_SIZE = 1.0f;
        final int DEFAULT_META_STATE = 0;
        final float DEFAULT_PRECISION_X = 1.0f;
        final float DEFAULT_PRECISION_Y = 1.0f;
        final int DEFAULT_DEVICE_ID = 0;
        final int DEFAULT_EDGE_FLAGS = 0;
        MotionEvent event = MotionEvent.obtain(when, when, action, x, y, pressure, DEFAULT_SIZE,
                DEFAULT_META_STATE, DEFAULT_PRECISION_X, DEFAULT_PRECISION_Y, DEFAULT_DEVICE_ID,
                DEFAULT_EDGE_FLAGS);
        event.setSource(inputSource);
        Log.i(TAG, "injectMotionEvent: " + event);
        try {
        inputManager.injectInputEvent(event,
        		InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
        }catch (RemoteException e) {
            System.err.println(e.toString());
            return;
        }
    }
    public static final float lerp(float a, float b, float alpha) {
        return (b - a) * alpha + a;
    }
}
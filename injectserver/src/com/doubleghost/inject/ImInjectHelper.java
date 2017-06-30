package com.doubleghost.inject;

import android.hardware.input.InputManager;
import android.view.KeyEvent;
import android.os.SystemClock;
import android.view.InputEvent;

public class ImInjectHelper {
    public InputManager inputManager;
    
    ImInjectHelper() {
        inputManager = getInputManager();
    }
    
    public static KeyEvent getKeyEvent(int action, int code) {
        return KeyEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), action, code, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, null);
    }
    
    public static InputManager getInputManager() {
        return InputManager.getInstance();
    }
    
    public boolean injectInputEvent(InputEvent event, int source, boolean setSource) {
        if(setSource) {
            event.setSource(source);
        }
        return inputManager.injectInputEvent(event, InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
    }
}
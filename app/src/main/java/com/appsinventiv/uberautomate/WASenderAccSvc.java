package com.appsinventiv.uberautomate;

import static android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public class WASenderAccSvc extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info=new AccessibilityServiceInfo();
        info.eventTypes =
                AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED|
                AccessibilityEvent.TYPE_VIEW_SCROLLED|
                AccessibilityEvent.WINDOWS_CHANGE_FOCUSED|
                AccessibilityEvent.WINDOWS_CHANGE_CHILDREN|
                AccessibilityEvent.WINDOWS_CHANGE_ADDED|
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED|
                AccessibilityEvent.TYPE_VIEW_FOCUSED;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
        Log.d("loog","connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("running", false)) {
            return;
        }

        try{
            List<AccessibilityNodeInfo> text = getRootInActiveWindow().findAccessibilityNodeInfosByText("Fare breakdown");
            if(text.size()>0){
                Log.d("loog",text.size()+"");
            }
        }catch (Exception e){

        }
//        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
//            String actname = event.getClassName().toString();
//            if (actname != null) {
//                if (actname.equals("com.whatsapp.Conversation")) {
//
//                    if (getRootInActiveWindow() != null) {
//                        try {
//                            if (getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.whatsapp:id/send") != null) {
//                                if (!getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.whatsapp:id/send").isEmpty() && getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.whatsapp:id/send") != null) {
//                                    if (getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.whatsapp:id/send").size() >= 1) {
//                                        try {
//                                            if (getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.whatsapp:id/send").get(0) != null) {
//                                                try {
//                                                    getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.whatsapp:id/send").get(0).performAction(ACTION_CLICK);
//                                                    performGlobalAction(GLOBAL_ACTION_BACK);
//
//                                                    if (actname.equals("com.whatsapp.Conversation")) {
//                                                        performGlobalAction(GLOBAL_ACTION_BACK);
//                                                    }
//
//                                                } catch (IndexOutOfBoundsException e) {
//                                                    e.printStackTrace();
//                                                    performGlobalAction(GLOBAL_ACTION_BACK);
//                                                }
//                                            }
//                                        } catch (IndexOutOfBoundsException e) {
//                                            e.printStackTrace();
//                                            performGlobalAction(GLOBAL_ACTION_BACK);
//                                        }
//                                    }
//
//                                }
//                            }
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                            performGlobalAction(GLOBAL_ACTION_BACK);
//                        }
//                    }
//                } else if (actname.equals("com.whatsapp.HomeActivity")) {
//                    sendNext();
//                } else if (actname.equals("com.whatsapp.ContactPicker")) {
//                    sendNext();
//                }
//
//
//            }
//        }
    }


    @Override
    public void onInterrupt() {
        Toast.makeText(this, "Please allow accessibility permission to WhatsApp Sender", Toast.LENGTH_SHORT).show();
    }

    private void sendNext() {
        Intent intent = new Intent(this, WASenderFgSvc.class);
        intent.putExtra("start", false);
        startService(intent);
    }

}

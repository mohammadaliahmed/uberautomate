package com.appsinventiv.uberautomate;

import static android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK;
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_FORWARD;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.appsinventiv.uberautomate.Model.ApiResponse;
import com.appsinventiv.uberautomate.Model.Constants;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WASenderAccSvc extends AccessibilityService {
    int counter = 1;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes =
                AccessibilityEvent.TYPE_VIEW_CLICKED |
                        AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED |
                        AccessibilityEvent.TYPE_VIEW_SCROLLED |
                        AccessibilityEvent.WINDOWS_CHANGE_FOCUSED |
                        AccessibilityEvent.WINDOWS_CHANGE_CHILDREN |
                        AccessibilityEvent.WINDOWS_CHANGE_ADDED |
                        AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED |
                        AccessibilityEvent.TYPE_VIEW_FOCUSED;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
        Log.d("loog", "uber automate connected");
        Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "connected");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (!Constants.WORK) {
            if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("running", false)) {
                return;
            }


            String screen = event.getClassName().toString();
            try {
                AccessibilityNodeInfo whereTo = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.ubercab:id/ub__internal_text").get(0);
                if (whereTo != null) {
                    whereTo.performAction(ACTION_CLICK);
                }
            } catch (Exception e) {

            }
//        if (!pickupClicked) {
            try {

                boolean pickup = getRootInActiveWindow()
                        .findAccessibilityNodeInfosByViewId("com.ubercab:id/ub__location_edit_search_container_pickup").get(0).performAction(ACTION_CLICK);
                Log.d("loog", "pickupClicked");
//                if (!pickUpEntered) {
                try {
                    AccessibilityNodeInfo pickupEt = getRootInActiveWindow()
                            .findAccessibilityNodeInfosByViewId("com.ubercab:id/ub__location_edit_search_pickup_edit").get(0);
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo
                            .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, MainActivity.data.getStartingPoint());
                    pickupEt.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    Log.d("loog", "pickUpEntered");
                    Thread.sleep(1500);
//                        if (!pickupChosen) {
                    try {
                        getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.ubercab:id/list").get(0)
                                .getChild(0).performAction(ACTION_CLICK);
                        Log.d("loog", "pickupChosen");
                        AccessibilityNodeInfo dropOffLocation = getRootInActiveWindow()
                                .findAccessibilityNodeInfosByViewId("com.ubercab:id/ub__location_edit_search_destination_edit").get(0);
                        Bundle arguments2 = new Bundle();
                        arguments2.putCharSequence(AccessibilityNodeInfo
                                .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, MainActivity.data.getEndingPoint());
                        dropOffLocation.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments2);
                        Thread.sleep(1500);
                        Log.d("loog", "dropOffLocation");

                        getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.ubercab:id/list").get(0)
                                .getChild(0).performAction(ACTION_CLICK);

                        Thread.sleep(2000);

                        counter = 1;
                        getRideData(counter);


                    } catch (Exception e) {

                    }

                } catch (Exception e) {

                }

            } catch (Exception e) {

            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getRideData(int child) throws InterruptedException {

        AccessibilityNodeInfo products = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.ubercab:id/ub__product_selection").get(0);

        //product list shown



//

        AccessibilityNodeInfo vehicle = products.getChild(child);
        vehicle.performAction(ACTION_CLICK);
        vehicle.performAction(ACTION_CLICK);
        //vheicle chosen

        Thread.sleep(1000);
        String vehicleType = getRootInActiveWindow().
                findAccessibilityNodeInfosByViewId("com.ubercab:id/ub__default_title_cell_element_view").get(0).getText().toString();
        AccessibilityNodeInfo totalAmount = getRootInActiveWindow()
                .findAccessibilityNodeInfosByViewId("com.ubercab:id/ub__default_fare_cell_element_view").get(0);

        Log.d("loog", vehicleType);
//                        Log.d("loog",totalAmount);


        AccessibilityNodeInfo infoBtn = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.ubercab:id/ub__default_description_cell_element_view").get(0);
        infoBtn.performAction(ACTION_CLICK);
        Thread.sleep(1000);

        AccessibilityNodeInfo fareBreakDownList = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.ubercab:id/line_items_container").get(0);
        HashMap<String, String> map = new HashMap<>();

        for (int j = 0; j < fareBreakDownList.getChildCount(); j++) {

            String key = fareBreakDownList.getChild(j).getChild(0).getText().toString();
            String val = fareBreakDownList.getChild(j).getChild(1).getText().toString();
            map.put(key, val);

        }

        Log.d("loog", "" + map);
        Toast.makeText(this, "" + map, Toast.LENGTH_SHORT).show();
        performGlobalAction(GLOBAL_ACTION_BACK);
        performGlobalAction(GLOBAL_ACTION_BACK);
        counter++;
        Log.d("loog", "counter" + counter);

        Thread.sleep(1000);
        Log.d("loog", "going home");
        performGlobalAction(GLOBAL_ACTION_BACK);
        sendNext();


    }


    @Override
    public void onInterrupt() {
        Toast.makeText(this, "Please allow accessibility permission to WhatsApp Sender", Toast.LENGTH_SHORT).show();
    }

    private void sendNext() {
        Constants.WORK = true;


        UserClient userClient = AppConfig.getRetrofit().create(UserClient.class);
        Call<ApiResponse> call = userClient.saveData();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {

                    MainActivity.data = response.body().getData();
                    MainActivity.sp.edit().putBoolean("running", true).apply();
                    Intent i = new Intent(getApplicationContext(), WASenderFgSvc.class);
                    startService(i);
                    Constants.WORK=false;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }

}

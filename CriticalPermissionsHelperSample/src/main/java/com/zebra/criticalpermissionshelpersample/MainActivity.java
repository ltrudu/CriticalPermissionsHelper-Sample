package com.zebra.criticalpermissionshelpersample;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.zebra.criticalpermissionshelper.CriticalPermissionsHelper;
import com.zebra.criticalpermissionshelper.EPermissionType;
import com.zebra.criticalpermissionshelper.IResultCallbacks;

/**
 * Critical Permissions Helper Sample
 *
 *  Code:
 *          - Trudu Laurent
 *          - https://github.com/ltrudu/CriticalPermissionsHelperSample
 *
 *  (c) Zebra 2022
 */

public class MainActivity extends AppCompatActivity {

    static  String status = "";
    TextView tvStatus;

    Switch sw_access_notifications;
    Switch sw_package_usage_stats;
    Switch sw_system_alert_window;
    Switch sw_get_app_ops_stats;
    Switch sw_battery_stats;
    Switch sw_manage_external_storage;

    Button bt_access_notifications;
    Button bt_package_usage_stats;
    Button bt_system_alert_window;
    Button bt_get_app_ops_stats;
    Button bt_battery_stats;
    Button bt_manage_external_storage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tv_status);

        sw_access_notifications         = findViewById(R.id.swAccessNotifications);
        sw_package_usage_stats          = findViewById(R.id.swPackageUsageStats);
        sw_system_alert_window          = findViewById(R.id.swSystemAlertWindow);
        sw_get_app_ops_stats            = findViewById(R.id.swGetAppsOpsStats);
        sw_battery_stats                = findViewById(R.id.swBatteryStats);
        sw_manage_external_storage      = findViewById(R.id.swManageExternalFiles);
        bt_access_notifications         = findViewById(R.id.btAccessNotifications);
        bt_package_usage_stats          = findViewById(R.id.btPackageUsageStats);
        bt_system_alert_window          = findViewById(R.id.btSystemAlertWindow);
        bt_get_app_ops_stats            = findViewById(R.id.btGetAppsOpsStats);
        bt_battery_stats                = findViewById(R.id.btBatteryStats);
        bt_manage_external_storage      = findViewById(R.id.btManageExternalFiles);

        initPermissionGUI(sw_access_notifications   , bt_access_notifications   , EPermissionType.ACCESS_NOTIFICATIONS);
        initPermissionGUI(sw_package_usage_stats    , bt_package_usage_stats    , EPermissionType.PACKAGE_USAGE_STATS);
        initPermissionGUI(sw_system_alert_window    , bt_system_alert_window    , EPermissionType.SYSTEM_ALERT_WINDOW);
        initPermissionGUI(sw_get_app_ops_stats      , bt_get_app_ops_stats      , EPermissionType.GET_APP_OPS_STATS);
        initPermissionGUI(sw_battery_stats          , bt_battery_stats          , EPermissionType.BATTERY_STATS);
        initPermissionGUI(sw_manage_external_storage, bt_manage_external_storage, EPermissionType.MANAGE_EXTERNAL_STORAGE);
     }

     private void initPermissionGUI(final Switch aSwitch, final Button aButton, final EPermissionType aPermissionType)
     {
         aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 if(b)
                 {
                     // Enable permission
                     CriticalPermissionsHelper.grantPermission(MainActivity.this, aPermissionType, new IResultCallbacks() {
                         @Override
                         public void onSuccess(String message, String resultXML) {
                             addMessageToStatusText(aPermissionType.toString() + " granted with success.");
                         }

                         @Override
                         public void onError(String message, String resultXML) {
                             addMessageToStatusText("Error granting " + aPermissionType.toString() + " permission.\n" + message);
                         }

                         @Override
                         public void onDebugStatus(String message) {
                             addMessageToStatusText("Grant " + aPermissionType.toString() + ": " + message);
                         }
                     });
                 }
                 else
                 {
                     // Disable permission
                     CriticalPermissionsHelper.denyPermission(MainActivity.this, aPermissionType, new IResultCallbacks() {
                         @Override
                         public void onSuccess(String message, String resultXML) {
                             addMessageToStatusText(aPermissionType.toString() + " denied with success.");
                         }

                         @Override
                         public void onError(String message, String resultXML) {
                             addMessageToStatusText("Error denying " + aPermissionType.toString() + " permission.\n" + message);
                         }

                         @Override
                         public void onDebugStatus(String message) {
                             addMessageToStatusText("Deny " + aPermissionType.toString() + ": " + message);
                         }
                     });
                 }
             }
         });

         aButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 CriticalPermissionsHelper.verifyPermission(MainActivity.this, aPermissionType, new IResultCallbacks() {
                     @Override
                     public void onSuccess(String message, String resultXML) {
                         addMessageToStatusText(aPermissionType.toString() + " verified with success.");
                         addMessageToStatusText("ResultXML:" + resultXML);
                         // Return Success when permission is granted
                         MainActivity.this.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 aSwitch.setChecked(true);
                             }
                         });
                     }

                     @Override
                     public void onError(String message, String resultXML) {
                         // Return error when permission is not granted
                         if(message.contains("Permission is not granted!"))
                         {
                             // Permission not granted
                             MainActivity.this.runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     aSwitch.setChecked(false);
                                 }
                             });
                         }
                         else {
                             // A real error happened
                             addMessageToStatusText("Error verifying " + aPermissionType.toString() + " permission.\n" + message);
                             addMessageToStatusText("ResultXML:" + resultXML);
                         }
                     }

                     @Override
                     public void onDebugStatus(String message) {
                         addMessageToStatusText("Verify " + aPermissionType.toString() + ": " + message);
                     }
                 });
             }
         });
     }
     
    private void addMessageToStatusText(String message)
    {
        status += message + "\n";
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                tvStatus.setText(status);
            }
        });
    }

    private void updateTextViewContent(final TextView tv, final String text)
    {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                tv.setText(text);
            }
        });
    }
}
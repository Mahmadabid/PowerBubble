package com.mahmad.powerbubble;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.mahmad.powerbubble.MyDeviceAdminReceiver;

public class MainActivity extends AppCompatActivity {

    private Switch bubbleSwitch;
    private Button btnOverlay, btnAccessibility, btnDeviceAdmin, btnRevokeAll;
    private TextView statusText, deviceAdminStatusText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbleSwitch = findViewById(R.id.switch_bubble);
        btnOverlay = findViewById(R.id.btn_overlay_perm);
        btnAccessibility = findViewById(R.id.btn_accessibility_perm);
        btnDeviceAdmin = findViewById(R.id.btn_device_admin);
        btnRevokeAll = findViewById(R.id.btn_revoke_all);
        btnRevokeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revoke Device Admin
                android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                android.content.ComponentName admin = new android.content.ComponentName(MainActivity.this, MyDeviceAdminReceiver.class);
                if (dpm.isAdminActive(admin)) {
                    dpm.removeActiveAdmin(admin);
                }
                // Open overlay permission settings for user to revoke
                Intent overlayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(overlayIntent);
                // Open accessibility settings for user to revoke
                Intent accessibilityIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(accessibilityIntent);
                updateStatus();
            }
        });
        statusText = findViewById(R.id.text_status);
        deviceAdminStatusText = findViewById(R.id.text_device_admin_status);

        bubbleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Settings.canDrawOverlays(MainActivity.this)) {
                        startService(new Intent(MainActivity.this, BubbleService.class));
                    } else {
                        requestOverlayPermission();
                        bubbleSwitch.setChecked(false);
                    }
                } else {
                    stopService(new Intent(MainActivity.this, BubbleService.class));
                }
                updateStatus();
            }
        });

        btnOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOverlayPermission();
            }
        });

        btnAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });

        btnDeviceAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                android.content.ComponentName admin = new android.content.ComponentName(MainActivity.this, MyDeviceAdminReceiver.class);
                if (!dpm.isAdminActive(admin)) {
                    Intent intent = new Intent(android.app.admin.DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(android.app.admin.DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin);
                    intent.putExtra(android.app.admin.DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Device admin is needed to lock the device from the app.");
                    startActivity(intent);
                }
            }
        });

        btnRevokeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revoke Device Admin
                android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                android.content.ComponentName admin = new android.content.ComponentName(MainActivity.this, MyDeviceAdminReceiver.class);
                if (dpm.isAdminActive(admin)) {
                    dpm.removeActiveAdmin(admin);
                }
                // Open overlay permission settings for user to revoke
                Intent overlayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(overlayIntent);
                // Open accessibility settings for user to revoke
                Intent accessibilityIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(accessibilityIntent);
                updateStatus();
            }
        });
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    private void updateStatus() {
        boolean overlay = Settings.canDrawOverlays(this);
        boolean accessibility = BubbleAccessibilityService.isEnabled(this);
        android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        android.content.ComponentName admin = new android.content.ComponentName(this, MyDeviceAdminReceiver.class);
        boolean deviceAdmin = dpm.isAdminActive(admin);
        StringBuilder sb = new StringBuilder();
        sb.append("Overlay: ").append(overlay ? "Granted" : "Missing").append("\n");
        sb.append("Accessibility: ").append(accessibility ? "Granted" : "Missing");
        statusText.setText(sb.toString());
        deviceAdminStatusText.setText("Device Admin: " + (deviceAdmin ? "Enabled" : "Disabled"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }
}

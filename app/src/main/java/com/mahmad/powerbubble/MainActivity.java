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

public class MainActivity extends AppCompatActivity {

    private Switch bubbleSwitch;
    private Button btnOverlay, btnAccessibility;
    private TextView statusText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbleSwitch = findViewById(R.id.switch_bubble);
        btnOverlay = findViewById(R.id.btn_overlay_perm);
        btnAccessibility = findViewById(R.id.btn_accessibility_perm);
        statusText = findViewById(R.id.text_status);

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
        StringBuilder sb = new StringBuilder();
        sb.append("Overlay: ").append(overlay ? "Granted" : "Missing").append("\n");
        sb.append("Accessibility: ").append(accessibility ? "Granted" : "Missing");
        statusText.setText(sb.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }
}

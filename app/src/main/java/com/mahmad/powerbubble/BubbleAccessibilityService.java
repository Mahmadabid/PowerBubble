package com.mahmad.powerbubble;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * Accessibility Service to provide lock screen and power dialog actions.
 */
public class BubbleAccessibilityService extends AccessibilityService {
    public static boolean isEnabled(Context context) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am == null) return false;
        for (AccessibilityServiceInfo service : am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)) {
            if (service.getId() != null && service.getId().contains(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Not used
    }

    @Override
    public void onInterrupt() {
        // Not used
    }

    public static void lockScreen(Context context) {
        Intent intent = new Intent(context, BubbleAccessibilityService.class);
        intent.setAction("LOCK_SCREEN");
        context.startService(intent);
    }

    public static void showPowerDialog(Context context) {
        Intent intent = new Intent(context, BubbleAccessibilityService.class);
        intent.setAction("POWER_DIALOG");
        context.startService(intent);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("LOCK_SCREEN")) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN);
                }
            } else if (intent.getAction().equals("POWER_DIALOG")) {
                performGlobalAction(GLOBAL_ACTION_POWER_DIALOG);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}

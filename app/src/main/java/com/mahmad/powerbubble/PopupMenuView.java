package com.mahmad.powerbubble;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.media.AudioManager;
import androidx.core.content.ContextCompat;

/**
 * Popup menu overlay for bubble actions.
 */
public class PopupMenuView extends LinearLayout {
    // Static references are used for overlay management. Ensure dismiss() is always called to avoid leaks.
    private static PopupMenuView currentMenu;
    private static View currentBackground;

    public PopupMenuView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundColor(Color.TRANSPARENT);
        setPadding(0, 0, 0, 0);
        setElevation(0f);
        setAlpha(1f);

        int btnSize = 104; // px, slightly bigger
        int margin = 28; // px, slightly more space

        // Turn Off Button
        ImageButton btnLock = new ImageButton(context);
        btnLock.setBackground(createCircleButtonBg(Color.BLACK));
        btnLock.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        btnLock.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_turn_off));
        btnLock.setColorFilter(Color.WHITE);
        btnLock.setPadding(20, 20, 20, 20);
        LayoutParams lockParams = new LayoutParams(btnSize, btnSize);
        lockParams.setMargins(0, margin, 0, margin);
        btnLock.setLayoutParams(lockParams);
        btnLock.setOnClickListener(v -> {
            BubbleAccessibilityService.lockScreen(context);
            PopupMenuView.dismiss(context);
        });
        addView(btnLock);

        // Volume Button
        ImageButton btnVolume = new ImageButton(context);
        btnVolume.setBackground(createCircleButtonBg(Color.BLACK));
        btnVolume.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        btnVolume.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_volume));
        btnVolume.setColorFilter(Color.WHITE);
        btnVolume.setPadding(20, 20, 20, 20);
        LayoutParams volParams = new LayoutParams(btnSize, btnSize);
        volParams.setMargins(0, margin, 0, margin);
        btnVolume.setLayoutParams(volParams);
        btnVolume.setOnClickListener(v -> {
            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
            PopupMenuView.dismiss(context);
        });
        addView(btnVolume);

        // Power Off Button
        ImageButton btnPower = new ImageButton(context);
        btnPower.setBackground(createCircleButtonBg(Color.BLACK));
        btnPower.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        btnPower.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_power_off));
        btnPower.setColorFilter(Color.WHITE);
        btnPower.setPadding(20, 20, 20, 20);
        LayoutParams powerParams = new LayoutParams(btnSize, btnSize);
        powerParams.setMargins(0, margin, 0, margin);
        btnPower.setLayoutParams(powerParams);
        btnPower.setOnClickListener(v -> {
            BubbleAccessibilityService.showPowerDialog(context);
            PopupMenuView.dismiss(context);
        });
        addView(btnPower);
    }

    public static void show(Context context, WindowManager windowManager, WindowManager.LayoutParams anchorParams) {
        // Remove any existing menu/background first
        dismiss(context);
        // Add a full-screen transparent background to catch outside touches
        currentBackground = new View(context);
        // Popup background color: darker semi-transparent black
        currentBackground.setBackgroundColor(0x88000000); // more dark, 0x88 = ~53% opacity
        currentBackground.setOnTouchListener((v, event) -> {
            v.performClick();
            dismiss(context);
            return true;
        });
        WindowManager.LayoutParams bgParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                android.graphics.PixelFormat.TRANSLUCENT);
        bgParams.gravity = Gravity.TOP | Gravity.START;
        windowManager.addView(currentBackground, bgParams);

        currentMenu = new PopupMenuView(context);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                android.graphics.PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        // Dynamically position the menu to the left or right of the bubble based on anchorParams.x
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int menuWidth = 150; // Approximate width in px, adjust as needed
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int menuHeight = 400; // Approximate height in px, adjust as needed
        // X positioning (left/right)
        if (anchorParams != null && anchorParams.x > screenWidth / 2) {
            params.x = Math.max(anchorParams.x - menuWidth, 0);
        } else {
            params.x = anchorParams != null ? anchorParams.x + 140 : 200;
        }
        // Y positioning (top/bottom)
        if (anchorParams != null && anchorParams.y > screenHeight - menuHeight) {
            // If bubble is near the bottom, show menu above
            params.y = Math.max(anchorParams.y - menuHeight, 0);
        } else {
            params.y = anchorParams != null ? anchorParams.y : 300;
        }
        windowManager.addView(currentMenu, params);
        // Remove auto-dismiss handler to prevent immediate closing
        // new Handler().postDelayed(() -> dismiss(context), 4000);
    }

    public static void dismiss(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (currentMenu != null) {
            try { wm.removeViewImmediate(currentMenu); } catch (Exception ignored) {}
            currentMenu = null;
        }
        if (currentBackground != null) {
            try { wm.removeViewImmediate(currentBackground); } catch (Exception ignored) {}
            currentBackground = null;
        }
    }

    private GradientDrawable createCircleButtonBg(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setSize(104, 104); // match btnSize
        return drawable;
    }
}

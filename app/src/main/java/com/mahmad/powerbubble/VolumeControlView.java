package com.mahmad.powerbubble;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Overlay for controlling device volume.
 */
public class VolumeControlView extends LinearLayout {
    public VolumeControlView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setPadding(32, 32, 32, 32);
        setBackgroundColor(0xEEFFFFFF);
        setElevation(8f);

        TextView label = new TextView(context);
        label.setText(getResources().getString(R.string.volume));
        addView(label);

        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        SeekBar seekBar = new SeekBar(context);
        seekBar.setMax(max);
        seekBar.setProgress(current);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        addView(seekBar);
    }

    public static void show(Context context) {
        final VolumeControlView view = new VolumeControlView(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                android.graphics.PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        wm.addView(view, params);
        new Handler().postDelayed(() -> {
            try { wm.removeView(view); } catch (Exception ignored) {}
        }, 2000);
    }
}

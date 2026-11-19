package com.android.bbkcalculator;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;

/**
 * Auto-root on boot. Safety rules learned in the field:
 *  - A failed run disables the receiver; the user re-enables it in the app.
 *  - Soft reboot at boot only ever happens when the user explicitly opted in
 *    on the Autorun card (same as pre-3.2 behaviour).
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "DirtyFrag";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (new File("/dev/df").exists()) {
            Log.i(TAG, "boot: already hooked, skipping");
            return;
        }
        Log.i(TAG, "boot: " + intent.getAction());
        final Context deCtx = context.createDeviceProtectedStorageContext();
        
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DirtyFrag:boot");
        wl.acquire();
        new Thread(() -> {
            boolean ok = false;
            try {
                // TODO: 启动原有的ExploitRunner逻辑
                Log.i(TAG, "boot: exploit runner not implemented yet");
                ok = true;
            } catch (Exception e) {
                Log.e(TAG, "boot: exploit exception", e);
            } finally {
                wl.release();
            }
            if (!ok) {
                Log.i(TAG, "boot: failed - disabling autorun interlock");
                context.getPackageManager().setComponentEnabledSetting(
                        new ComponentName(context, BootReceiver.class),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }, "DirtyFrag-boot").start();
    }
}
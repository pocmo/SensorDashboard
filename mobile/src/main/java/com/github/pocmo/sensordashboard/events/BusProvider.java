package com.github.pocmo.sensordashboard.events;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public final class BusProvider
{
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    public static void postOnMainThread(final Object event) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            public void run() {
                BUS.post(event);
            }
        });
    }

    private BusProvider() {
    }
}

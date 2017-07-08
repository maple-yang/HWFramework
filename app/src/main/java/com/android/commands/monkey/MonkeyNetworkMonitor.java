package com.android.commands.monkey;

import android.app.IActivityManager;
import android.content.IIntentReceiver.Stub;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;

public class MonkeyNetworkMonitor extends Stub {
    private static final boolean LDEBUG = false;
    private final IntentFilter filter;
    private long mCollectionStartTime;
    private long mElapsedTime;
    private long mEventTime;
    private int mLastNetworkType;
    private long mMobileElapsedTime;
    private long mWifiElapsedTime;

    public MonkeyNetworkMonitor() {
        this.filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.mLastNetworkType = -1;
        this.mWifiElapsedTime = 0;
        this.mMobileElapsedTime = 0;
        this.mElapsedTime = 0;
    }

    public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) throws RemoteException {
        NetworkInfo ni = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        updateNetworkStats();
        if (State.CONNECTED == ni.getState()) {
            this.mLastNetworkType = ni.getType();
        } else if (State.DISCONNECTED == ni.getState()) {
            this.mLastNetworkType = -1;
        }
        this.mEventTime = SystemClock.elapsedRealtime();
    }

    private void updateNetworkStats() {
        long timeNow = SystemClock.elapsedRealtime();
        long delta = timeNow - this.mEventTime;
        switch (this.mLastNetworkType) {
            case MonkeySourceRandom.FACTOR_TOUCH /*0*/:
                this.mMobileElapsedTime += delta;
                break;
            case MonkeySourceRandom.FACTOR_MOTION /*1*/:
                this.mWifiElapsedTime += delta;
                break;
        }
        this.mElapsedTime = timeNow - this.mCollectionStartTime;
    }

    public void start() {
        this.mWifiElapsedTime = 0;
        this.mMobileElapsedTime = 0;
        this.mElapsedTime = 0;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mCollectionStartTime = elapsedRealtime;
        this.mEventTime = elapsedRealtime;
    }

    public void register(IActivityManager am) throws RemoteException {
        am.registerReceiver(null, null, this, this.filter, null, -1);
    }

    public void unregister(IActivityManager am) throws RemoteException {
        am.unregisterReceiver(this);
    }

    public void stop() {
        updateNetworkStats();
    }

    public void dump() {
        System.out.println("## Network stats: elapsed time=" + this.mElapsedTime + "ms (" + this.mMobileElapsedTime + "ms mobile, " + this.mWifiElapsedTime + "ms wifi, " + ((this.mElapsedTime - this.mMobileElapsedTime) - this.mWifiElapsedTime) + "ms not connected)");
    }
}

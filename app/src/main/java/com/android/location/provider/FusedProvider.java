package com.android.location.provider;

import android.hardware.location.IFusedLocationHardware;
import android.location.IFusedProvider.Stub;
import android.os.IBinder;

public abstract class FusedProvider {
    private Stub mProvider;

    public abstract void setFusedLocationHardware(FusedLocationHardware fusedLocationHardware);

    public FusedProvider() {
        this.mProvider = new Stub() {
            public void onFusedLocationHardwareChange(IFusedLocationHardware instance) {
                FusedProvider.this.setFusedLocationHardware(new FusedLocationHardware(instance));
            }
        };
    }

    public IBinder getBinder() {
        return this.mProvider;
    }
}

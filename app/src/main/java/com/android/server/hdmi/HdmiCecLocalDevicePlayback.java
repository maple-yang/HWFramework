package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Slog;
import com.android.internal.app.LocalePicker;
import com.android.internal.app.LocalePicker.LocaleInfo;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly;
import com.android.server.wm.WindowState;
import java.io.UnsupportedEncodingException;
import java.util.List;

final class HdmiCecLocalDevicePlayback extends HdmiCecLocalDevice {
    private static final boolean SET_MENU_LANGUAGE = false;
    private static final String TAG = "HdmiCecLocalDevicePlayback";
    private static final boolean WAKE_ON_HOTPLUG = false;
    private boolean mAutoTvOff;
    private boolean mIsActiveSource;
    private ActiveWakeLock mWakeLock;

    private interface ActiveWakeLock {
        void acquire();

        boolean isHeld();

        void release();
    }

    private class SystemWakeLock implements ActiveWakeLock {
        private final WakeLock mWakeLock;

        public SystemWakeLock() {
            this.mWakeLock = HdmiCecLocalDevicePlayback.this.mService.getPowerManager().newWakeLock(1, HdmiCecLocalDevicePlayback.TAG);
            this.mWakeLock.setReferenceCounted(HdmiCecLocalDevicePlayback.SET_MENU_LANGUAGE);
        }

        public void acquire() {
            this.mWakeLock.acquire();
            HdmiLogger.debug("active source: %b. Wake lock acquired", Boolean.valueOf(HdmiCecLocalDevicePlayback.this.mIsActiveSource));
        }

        public void release() {
            this.mWakeLock.release();
            HdmiLogger.debug("Wake lock released", new Object[0]);
        }

        public boolean isHeld() {
            return this.mWakeLock.isHeld();
        }
    }

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.android.server.hdmi.HdmiCecLocalDevicePlayback.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: com.android.server.hdmi.HdmiCecLocalDevicePlayback.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 7 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 8 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiCecLocalDevicePlayback.<clinit>():void");
    }

    HdmiCecLocalDevicePlayback(HdmiControlService service) {
        super(service, 4);
        this.mIsActiveSource = SET_MENU_LANGUAGE;
        this.mAutoTvOff = this.mService.readBooleanSetting("hdmi_control_auto_device_off_enabled", SET_MENU_LANGUAGE);
        this.mService.writeBooleanSetting("hdmi_control_auto_device_off_enabled", this.mAutoTvOff);
    }

    @ServiceThreadOnly
    protected void onAddressAllocated(int logicalAddress, int reason) {
        assertRunOnServiceThread();
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildReportPhysicalAddressCommand(this.mAddress, this.mService.getPhysicalAddress(), this.mDeviceType));
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildDeviceVendorIdCommand(this.mAddress, this.mService.getVendorId()));
        startQueuedActions();
    }

    @ServiceThreadOnly
    protected int getPreferredAddress() {
        assertRunOnServiceThread();
        return SystemProperties.getInt("persist.sys.hdmi.addr.playback", 15);
    }

    @ServiceThreadOnly
    protected void setPreferredAddress(int addr) {
        assertRunOnServiceThread();
        SystemProperties.set("persist.sys.hdmi.addr.playback", String.valueOf(addr));
    }

    @ServiceThreadOnly
    void oneTouchPlay(IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        List<OneTouchPlayAction> actions = getActions(OneTouchPlayAction.class);
        if (actions.isEmpty()) {
            OneTouchPlayAction action = OneTouchPlayAction.create(this, 0, callback);
            if (action == null) {
                Slog.w(TAG, "Cannot initiate oneTouchPlay");
                invokeCallback(callback, 5);
                return;
            }
            addAndStartAction(action);
            return;
        }
        Slog.i(TAG, "oneTouchPlay already in progress");
        ((OneTouchPlayAction) actions.get(0)).addCallback(callback);
    }

    @ServiceThreadOnly
    void queryDisplayStatus(IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        List<DevicePowerStatusAction> actions = getActions(DevicePowerStatusAction.class);
        if (actions.isEmpty()) {
            DevicePowerStatusAction action = DevicePowerStatusAction.create(this, 0, callback);
            if (action == null) {
                Slog.w(TAG, "Cannot initiate queryDisplayStatus");
                invokeCallback(callback, 5);
                return;
            }
            addAndStartAction(action);
            return;
        }
        Slog.i(TAG, "queryDisplayStatus already in progress");
        ((DevicePowerStatusAction) actions.get(0)).addCallback(callback);
    }

    @ServiceThreadOnly
    private void invokeCallback(IHdmiControlCallback callback, int result) {
        assertRunOnServiceThread();
        try {
            callback.onComplete(result);
        } catch (RemoteException e) {
            Slog.e(TAG, "Invoking callback failed:" + e);
        }
    }

    @ServiceThreadOnly
    void onHotplug(int portId, boolean connected) {
        assertRunOnServiceThread();
        this.mCecMessageCache.flushAll();
        if (WAKE_ON_HOTPLUG && connected && this.mService.isPowerStandbyOrTransient()) {
            this.mService.wakeUp();
        }
        if (!connected) {
            getWakeLock().release();
        }
    }

    @ServiceThreadOnly
    protected void onStandby(boolean initiatedByCec, int standbyAction) {
        assertRunOnServiceThread();
        if (this.mService.isControlEnabled() && !initiatedByCec && this.mAutoTvOff) {
            switch (standbyAction) {
                case WindowState.LOW_RESOLUTION_FEATURE_OFF /*0*/:
                    this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(this.mAddress, 0));
                    break;
                case WindowState.LOW_RESOLUTION_COMPOSITION_OFF /*1*/:
                    this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(this.mAddress, 15));
                    break;
            }
        }
    }

    @ServiceThreadOnly
    void setAutoDeviceOff(boolean enabled) {
        assertRunOnServiceThread();
        this.mAutoTvOff = enabled;
    }

    @ServiceThreadOnly
    void setActiveSource(boolean on) {
        assertRunOnServiceThread();
        this.mIsActiveSource = on;
        if (on) {
            getWakeLock().acquire();
        } else {
            getWakeLock().release();
        }
    }

    @ServiceThreadOnly
    private ActiveWakeLock getWakeLock() {
        assertRunOnServiceThread();
        if (this.mWakeLock == null) {
            if (SystemProperties.getBoolean("persist.sys.hdmi.keep_awake", true)) {
                this.mWakeLock = new SystemWakeLock();
            } else {
                this.mWakeLock = new ActiveWakeLock() {
                    public void acquire() {
                    }

                    public void release() {
                    }

                    public boolean isHeld() {
                        return HdmiCecLocalDevicePlayback.SET_MENU_LANGUAGE;
                    }
                };
                HdmiLogger.debug("No wakelock is used to keep the display on.", new Object[0]);
            }
        }
        return this.mWakeLock;
    }

    protected boolean canGoToStandby() {
        return getWakeLock().isHeld() ? SET_MENU_LANGUAGE : true;
    }

    @ServiceThreadOnly
    protected boolean handleActiveSource(HdmiCecMessage message) {
        assertRunOnServiceThread();
        mayResetActiveSource(HdmiUtils.twoBytesToInt(message.getParams()));
        return true;
    }

    private void mayResetActiveSource(int physicalAddress) {
        if (physicalAddress != this.mService.getPhysicalAddress()) {
            setActiveSource(SET_MENU_LANGUAGE);
        }
    }

    @ServiceThreadOnly
    protected boolean handleUserControlPressed(HdmiCecMessage message) {
        assertRunOnServiceThread();
        wakeUpIfActiveSource();
        return super.handleUserControlPressed(message);
    }

    @ServiceThreadOnly
    protected boolean handleSetStreamPath(HdmiCecMessage message) {
        assertRunOnServiceThread();
        maySetActiveSource(HdmiUtils.twoBytesToInt(message.getParams()));
        maySendActiveSource(message.getSource());
        wakeUpIfActiveSource();
        return true;
    }

    @ServiceThreadOnly
    protected boolean handleRoutingChange(HdmiCecMessage message) {
        assertRunOnServiceThread();
        maySetActiveSource(HdmiUtils.twoBytesToInt(message.getParams(), 2));
        return true;
    }

    @ServiceThreadOnly
    protected boolean handleRoutingInformation(HdmiCecMessage message) {
        assertRunOnServiceThread();
        maySetActiveSource(HdmiUtils.twoBytesToInt(message.getParams()));
        return true;
    }

    private void maySetActiveSource(int physicalAddress) {
        setActiveSource(physicalAddress == this.mService.getPhysicalAddress() ? true : SET_MENU_LANGUAGE);
    }

    private void wakeUpIfActiveSource() {
        if (this.mIsActiveSource) {
            if (this.mService.isPowerStandbyOrTransient() || !this.mService.getPowerManager().isScreenOn()) {
                this.mService.wakeUp();
            }
        }
    }

    private void maySendActiveSource(int dest) {
        if (this.mIsActiveSource) {
            this.mService.sendCecCommand(HdmiCecMessageBuilder.buildActiveSource(this.mAddress, this.mService.getPhysicalAddress()));
            this.mService.sendCecCommand(HdmiCecMessageBuilder.buildReportMenuStatus(this.mAddress, dest, 0));
        }
    }

    @ServiceThreadOnly
    protected boolean handleRequestActiveSource(HdmiCecMessage message) {
        assertRunOnServiceThread();
        maySendActiveSource(message.getSource());
        return true;
    }

    @ServiceThreadOnly
    protected boolean handleSetMenuLanguage(HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (!SET_MENU_LANGUAGE) {
            return SET_MENU_LANGUAGE;
        }
        try {
            String iso3Language = new String(message.getParams(), 0, 3, "US-ASCII");
            if (this.mService.getContext().getResources().getConfiguration().locale.getISO3Language().equals(iso3Language)) {
                return true;
            }
            for (LocaleInfo localeInfo : LocalePicker.getAllAssetLocales(this.mService.getContext(), SET_MENU_LANGUAGE)) {
                if (localeInfo.getLocale().getISO3Language().equals(iso3Language)) {
                    LocalePicker.updateLocale(localeInfo.getLocale());
                    return true;
                }
            }
            Slog.w(TAG, "Can't handle <Set Menu Language> of " + iso3Language);
            return SET_MENU_LANGUAGE;
        } catch (UnsupportedEncodingException e) {
            Slog.w(TAG, "Can't handle <Set Menu Language>", e);
            return SET_MENU_LANGUAGE;
        }
    }

    @ServiceThreadOnly
    protected void sendStandby(int deviceId) {
        assertRunOnServiceThread();
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(this.mAddress, 0));
    }

    @ServiceThreadOnly
    protected void disableDevice(boolean initiatedByCec, PendingActionClearedCallback callback) {
        super.disableDevice(initiatedByCec, callback);
        assertRunOnServiceThread();
        if (!initiatedByCec && this.mIsActiveSource) {
            this.mService.sendCecCommand(HdmiCecMessageBuilder.buildInactiveSource(this.mAddress, this.mService.getPhysicalAddress()));
        }
        setActiveSource(SET_MENU_LANGUAGE);
        checkIfPendingActionsCleared();
    }

    protected void dump(IndentingPrintWriter pw) {
        super.dump(pw);
        pw.println("mIsActiveSource: " + this.mIsActiveSource);
        pw.println("mAutoTvOff:" + this.mAutoTvOff);
    }
}

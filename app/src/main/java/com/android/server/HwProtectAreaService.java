package com.android.server;

import android.content.Context;
import android.util.Slog;
import huawei.android.os.HwProtectArea;

public class HwProtectAreaService {
    static final String TAG = "HwProtectAreaService";
    private static volatile HwProtectAreaService mInstance;
    private Context mContext;
    private final Object mLock;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.android.server.HwProtectAreaService.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: com.android.server.HwProtectAreaService.<clinit>():void
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
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.HwProtectAreaService.<clinit>():void");
    }

    private static native void nativeProtectAreaClassInit();

    private static native int nativeReadProtectArea(String str, HwProtectArea hwProtectArea, int i);

    private static native int nativeWriteProtectArea(String str, HwProtectArea hwProtectArea, int i, String str2);

    public HwProtectAreaService(Context context) {
        this.mLock = new Object();
        this.mContext = context;
        try {
            synchronized (this.mLock) {
                nativeProtectAreaClassInit();
            }
        } catch (UnsatisfiedLinkError e) {
            Slog.e(TAG, "libarary ClassInit failed >>>>>" + e);
        } catch (Exception e2) {
            Slog.e(TAG, "libarary ClassInit failed >>>>>" + e2);
        }
    }

    public static synchronized HwProtectAreaService getInstance(Context context) {
        HwProtectAreaService hwProtectAreaService;
        synchronized (HwProtectAreaService.class) {
            if (mInstance == null) {
                mInstance = new HwProtectAreaService(context);
            }
            hwProtectAreaService = mInstance;
        }
        return hwProtectAreaService;
    }

    private int nativeReadProtectAreaJava(String optItem, int readBufLen, String[] readBuf, int[] errno) {
        int ret = -1;
        try {
            synchronized (this.mLock) {
                if (optItem == null || readBufLen < 0 || readBuf == null || errno == null) {
                    Slog.e(TAG, "nativeReadProtectAreaJava:parameter error !");
                    if (readBuf != null) {
                        readBuf[0] = "error";
                    }
                    if (errno != null) {
                        errno[0] = -1;
                    }
                    return -1;
                }
                HwProtectArea protectArea = new HwProtectArea(optItem);
                ret = nativeReadProtectArea(optItem, protectArea, readBufLen);
                if (-1 == ret) {
                    Slog.e(TAG, "nativeReadProtectAreaJava:error ret is -1 !");
                    readBuf[0] = "error";
                    errno[0] = -1;
                    return ret;
                }
                readBuf[0] = protectArea.getReadBuf();
                errno[0] = protectArea.getErrno();
                return ret;
            }
        } catch (UnsatisfiedLinkError e) {
            Slog.e(TAG, "libarary ReadProtectArea failed >>>>>" + e);
            return ret;
        } catch (Exception e2) {
            Slog.e(TAG, "libarary ReadProtectArea failed >>>>>" + e2);
            return ret;
        }
    }

    private int nativeWriteProtectAreaJava(String optItem, int writeLen, String writeBuf, int[] errno) {
        int ret = -1;
        try {
            synchronized (this.mLock) {
                if (optItem == null || writeBuf == null || errno == null) {
                    Slog.e(TAG, "nativeWriteProtectAreaJava:parameter error !");
                    if (errno != null) {
                        errno[0] = -1;
                    }
                    return -1;
                }
                HwProtectArea protectArea = new HwProtectArea(optItem);
                ret = nativeWriteProtectArea(optItem, protectArea, writeLen, writeBuf);
                if (-1 == ret) {
                    Slog.e(TAG, "nativeWriteProtectArea:error ret is -1 !");
                    errno[0] = -1;
                    return ret;
                }
                errno[0] = protectArea.getErrno();
                return ret;
            }
        } catch (UnsatisfiedLinkError e) {
            Slog.e(TAG, "libarary WriteProtectArea failed >>>>>" + e);
            return ret;
        } catch (Exception e2) {
            Slog.e(TAG, "libarary WriteProtectArea failed >>>>>" + e2);
            return ret;
        }
    }

    public int readProtectArea(String optItem, int readBufLen, String[] readBuf, int[] errorNum) {
        this.mContext.enforceCallingOrSelfPermission("com.huawei.android.permission.PROTECTAREA", null);
        return nativeReadProtectAreaJava(optItem, readBufLen, readBuf, errorNum);
    }

    public int writeProtectArea(String optItem, int writeLen, String writeBuf, int[] errorNum) {
        this.mContext.enforceCallingOrSelfPermission("com.huawei.android.permission.PROTECTAREA", null);
        return nativeWriteProtectAreaJava(optItem, writeLen, writeBuf, errorNum);
    }
}

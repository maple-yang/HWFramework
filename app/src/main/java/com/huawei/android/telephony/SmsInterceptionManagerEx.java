package com.huawei.android.telephony;

import android.telephony.SmsInterceptionManager;

public class SmsInterceptionManagerEx {
    private static final SmsInterceptionManagerEx sInstance = null;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.huawei.android.telephony.SmsInterceptionManagerEx.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: com.huawei.android.telephony.SmsInterceptionManagerEx.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 5 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 6 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.android.telephony.SmsInterceptionManagerEx.<clinit>():void");
    }

    public static synchronized SmsInterceptionManagerEx getInstance() {
        SmsInterceptionManagerEx smsInterceptionManagerEx;
        synchronized (SmsInterceptionManagerEx.class) {
            smsInterceptionManagerEx = sInstance;
        }
        return smsInterceptionManagerEx;
    }

    public void registerListener(SmsInterceptionListenerEx listener, int priority) {
        SmsInterceptionManager.getInstance().registerListener(listener, priority);
    }

    public void unregisterListener(int priority) {
        SmsInterceptionManager.getInstance().unregisterListener(priority);
    }
}

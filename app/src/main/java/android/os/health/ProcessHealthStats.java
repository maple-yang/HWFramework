package android.os.health;

import android.os.health.HealthKeys.Constant;
import android.os.health.HealthKeys.Constants;

public final class ProcessHealthStats {
    public static final Constants CONSTANTS = null;
    @Constant(type = 1)
    public static final int MEASUREMENT_ANR_COUNT = 30005;
    @Constant(type = 1)
    public static final int MEASUREMENT_CRASHES_COUNT = 30004;
    @Constant(type = 1)
    public static final int MEASUREMENT_FOREGROUND_MS = 30006;
    @Constant(type = 1)
    public static final int MEASUREMENT_STARTS_COUNT = 30003;
    @Constant(type = 1)
    public static final int MEASUREMENT_SYSTEM_TIME_MS = 30002;
    @Constant(type = 1)
    public static final int MEASUREMENT_USER_TIME_MS = 30001;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.os.health.ProcessHealthStats.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: android.os.health.ProcessHealthStats.<clinit>():void
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
        throw new UnsupportedOperationException("Method not decompiled: android.os.health.ProcessHealthStats.<clinit>():void");
    }

    private ProcessHealthStats() {
    }
}

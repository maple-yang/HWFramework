package libcore.util;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public final class EmptyArray {
    public static final boolean[] BOOLEAN = null;
    public static final byte[] BYTE = null;
    public static final char[] CHAR = null;
    public static final Class<?>[] CLASS = null;
    public static final double[] DOUBLE = null;
    public static final float[] FLOAT = null;
    public static final int[] INT = null;
    public static final long[] LONG = null;
    public static final Object[] OBJECT = null;
    public static final StackTraceElement[] STACK_TRACE_ELEMENT = null;
    public static final String[] STRING = null;
    public static final Throwable[] THROWABLE = null;
    public static final Type[] TYPE = null;
    public static final TypeVariable[] TYPE_VARIABLE = null;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: libcore.util.EmptyArray.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: libcore.util.EmptyArray.<clinit>():void
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
        throw new UnsupportedOperationException("Method not decompiled: libcore.util.EmptyArray.<clinit>():void");
    }

    private EmptyArray() {
    }
}

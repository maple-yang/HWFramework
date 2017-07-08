package android.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BackupUtils {
    public static final int NOT_NULL = 1;
    public static final int NULL = 0;

    public static class BadVersionException extends Exception {
        public BadVersionException(String message) {
            super(message);
        }
    }

    public static String readString(DataInputStream in) throws IOException {
        return in.readByte() == NOT_NULL ? in.readUTF() : null;
    }

    public static void writeString(DataOutputStream out, String val) throws IOException {
        if (val != null) {
            out.writeByte(NOT_NULL);
            out.writeUTF(val);
            return;
        }
        out.writeByte(0);
    }
}

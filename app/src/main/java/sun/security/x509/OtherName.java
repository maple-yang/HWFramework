package sun.security.x509;

import java.io.IOException;
import java.util.Arrays;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class OtherName implements GeneralNameInterface {
    private static final byte TAG_VALUE = (byte) 0;
    private GeneralNameInterface gni;
    private int myhash;
    private String name;
    private byte[] nameValue;
    private ObjectIdentifier oid;

    public OtherName(ObjectIdentifier oid, byte[] value) throws IOException {
        this.nameValue = null;
        this.gni = null;
        this.myhash = -1;
        if (oid == null || value == null) {
            throw new NullPointerException("parameters may not be null");
        }
        this.oid = oid;
        this.nameValue = value;
        this.gni = getGNI(oid, value);
        if (this.gni != null) {
            this.name = this.gni.toString();
        } else {
            this.name = "Unrecognized ObjectIdentifier: " + oid.toString();
        }
    }

    public OtherName(DerValue derValue) throws IOException {
        this.nameValue = null;
        this.gni = null;
        this.myhash = -1;
        DerInputStream in = derValue.toDerInputStream();
        this.oid = in.getOID();
        this.nameValue = in.getDerValue().toByteArray();
        this.gni = getGNI(this.oid, this.nameValue);
        if (this.gni != null) {
            this.name = this.gni.toString();
        } else {
            this.name = "Unrecognized ObjectIdentifier: " + this.oid.toString();
        }
    }

    public ObjectIdentifier getOID() {
        return this.oid;
    }

    public byte[] getNameValue() {
        return (byte[]) this.nameValue.clone();
    }

    private GeneralNameInterface getGNI(ObjectIdentifier oid, byte[] nameValue) throws IOException {
        try {
            Class extClass = OIDMap.getClass(oid);
            if (extClass == null) {
                return null;
            }
            return (GeneralNameInterface) extClass.getConstructor(Object.class).newInstance(nameValue);
        } catch (Object e) {
            throw ((IOException) new IOException("Instantiation error: " + e).initCause(e));
        }
    }

    public int getType() {
        return 0;
    }

    public void encode(DerOutputStream out) throws IOException {
        if (this.gni != null) {
            this.gni.encode(out);
            return;
        }
        DerOutputStream tmp = new DerOutputStream();
        tmp.putOID(this.oid);
        tmp.write(DerValue.createTag(DerValue.TAG_CONTEXT, true, (byte) 0), this.nameValue);
        out.write((byte) DerValue.tag_SequenceOf, tmp);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OtherName)) {
            return false;
        }
        OtherName otherOther = (OtherName) other;
        if (!otherOther.oid.equals(this.oid)) {
            return false;
        }
        try {
            boolean z;
            GeneralNameInterface otherGNI = getGNI(otherOther.oid, otherOther.nameValue);
            if (otherGNI != null) {
                try {
                    z = otherGNI.constrains(this) == 0;
                } catch (UnsupportedOperationException e) {
                    z = false;
                }
            } else {
                z = Arrays.equals(this.nameValue, otherOther.nameValue);
            }
            return z;
        } catch (IOException e2) {
            return false;
        }
    }

    public int hashCode() {
        if (this.myhash == -1) {
            this.myhash = this.oid.hashCode() + 37;
            for (byte b : this.nameValue) {
                this.myhash = (this.myhash * 37) + b;
            }
        }
        return this.myhash;
    }

    public String toString() {
        return "Other-Name: " + this.name;
    }

    public int constrains(GeneralNameInterface inputName) {
        if (inputName == null) {
            return -1;
        }
        if (inputName.getType() != 0) {
            return -1;
        }
        throw new UnsupportedOperationException("Narrowing, widening, and matching are not supported for OtherName.");
    }

    public int subtreeDepth() {
        throw new UnsupportedOperationException("subtreeDepth() not supported for generic OtherName");
    }
}

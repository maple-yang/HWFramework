package tmsdkobf;

import java.util.ArrayList;

/* compiled from: Unknown */
public final class dx extends fs implements Cloneable {
    static final /* synthetic */ boolean fJ = false;
    static ArrayList<Integer> iY;
    static do iZ;
    public String iH;
    public ArrayList<Integer> iV;
    public String iW;
    public do iX;
    public int id;
    public int time;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: tmsdkobf.dx.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: tmsdkobf.dx.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 5 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e9
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 6 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: tmsdkobf.dx.<clinit>():void");
    }

    public dx() {
        this.id = 0;
        this.time = 0;
        this.iH = "";
        this.iV = null;
        this.iW = "";
        this.iX = null;
        setId(this.id);
        v(this.time);
        y(this.iH);
        e(this.iV);
        B(this.iW);
        a(this.iX);
    }

    public void B(String str) {
        this.iW = str;
    }

    public void a(do doVar) {
        this.iX = doVar;
    }

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            if (!fJ) {
                throw new AssertionError();
            }
        }
        return obj;
    }

    public void display(StringBuilder stringBuilder, int i) {
        fo foVar = new fo(stringBuilder, i);
        foVar.a(this.id, "id");
        foVar.a(this.time, "time");
        foVar.a(this.iH, "desc");
        foVar.a(this.iV, "ivalues");
        foVar.a(this.iW, "paramvalues");
        foVar.a(this.iX, "pluginInfo");
    }

    public ArrayList<Integer> e() {
        return this.iV;
    }

    public void e(ArrayList<Integer> arrayList) {
        this.iV = arrayList;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        dx dxVar = (dx) obj;
        if (ft.equals(this.id, dxVar.id) && ft.equals(this.time, dxVar.time) && ft.equals(this.iH, dxVar.iH) && ft.equals(this.iV, dxVar.iV) && ft.equals(this.iW, dxVar.iW) && ft.equals(this.iX, dxVar.iX)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        try {
            throw new Exception("Need define key first!");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void readFrom(fq fqVar) {
        setId(fqVar.a(this.id, 0, true));
        v(fqVar.a(this.time, 1, true));
        y(fqVar.a(2, true));
        if (iY == null) {
            iY = new ArrayList();
            iY.add(Integer.valueOf(0));
        }
        e((ArrayList) fqVar.b(iY, 3, false));
        B(fqVar.a(4, false));
        if (iZ == null) {
            iZ = new do();
        }
        a((do) fqVar.a(iZ, 5, false));
    }

    public void setId(int i) {
        this.id = i;
    }

    public void v(int i) {
        this.time = i;
    }

    public void writeTo(fr frVar) {
        frVar.write(this.id, 0);
        frVar.write(this.time, 1);
        frVar.a(this.iH, 2);
        if (this.iV != null) {
            frVar.a(this.iV, 3);
        }
        if (this.iW != null) {
            frVar.a(this.iW, 4);
        }
        if (this.iX != null) {
            frVar.a(this.iX, 5);
        }
    }

    public void y(String str) {
        this.iH = str;
    }
}

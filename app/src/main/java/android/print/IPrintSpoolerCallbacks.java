package android.print;

import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IPrintSpoolerCallbacks extends IInterface {

    public static abstract class Stub extends Binder implements IPrintSpoolerCallbacks {
        private static final String DESCRIPTOR = "android.print.IPrintSpoolerCallbacks";
        static final int TRANSACTION_customPrinterIconCacheCleared = 8;
        static final int TRANSACTION_onCancelPrintJobResult = 2;
        static final int TRANSACTION_onCustomPrinterIconCached = 7;
        static final int TRANSACTION_onGetCustomPrinterIconResult = 6;
        static final int TRANSACTION_onGetPrintJobInfoResult = 5;
        static final int TRANSACTION_onGetPrintJobInfosResult = 1;
        static final int TRANSACTION_onSetPrintJobStateResult = 3;
        static final int TRANSACTION_onSetPrintJobTagResult = 4;

        private static class Proxy implements IPrintSpoolerCallbacks {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void onGetPrintJobInfosResult(List<PrintJobInfo> printJob, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(printJob);
                    _data.writeInt(sequence);
                    this.mRemote.transact(Stub.TRANSACTION_onGetPrintJobInfosResult, _data, null, Stub.TRANSACTION_onGetPrintJobInfosResult);
                } finally {
                    _data.recycle();
                }
            }

            public void onCancelPrintJobResult(boolean canceled, int sequence) throws RemoteException {
                int i = Stub.TRANSACTION_onGetPrintJobInfosResult;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!canceled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(sequence);
                    this.mRemote.transact(Stub.TRANSACTION_onCancelPrintJobResult, _data, null, Stub.TRANSACTION_onGetPrintJobInfosResult);
                } finally {
                    _data.recycle();
                }
            }

            public void onSetPrintJobStateResult(boolean success, int sequence) throws RemoteException {
                int i = Stub.TRANSACTION_onGetPrintJobInfosResult;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!success) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(sequence);
                    this.mRemote.transact(Stub.TRANSACTION_onSetPrintJobStateResult, _data, null, Stub.TRANSACTION_onGetPrintJobInfosResult);
                } finally {
                    _data.recycle();
                }
            }

            public void onSetPrintJobTagResult(boolean success, int sequence) throws RemoteException {
                int i = Stub.TRANSACTION_onGetPrintJobInfosResult;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!success) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(sequence);
                    this.mRemote.transact(Stub.TRANSACTION_onSetPrintJobTagResult, _data, null, Stub.TRANSACTION_onGetPrintJobInfosResult);
                } finally {
                    _data.recycle();
                }
            }

            public void onGetPrintJobInfoResult(PrintJobInfo printJob, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJob != null) {
                        _data.writeInt(Stub.TRANSACTION_onGetPrintJobInfosResult);
                        printJob.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sequence);
                    this.mRemote.transact(Stub.TRANSACTION_onGetPrintJobInfoResult, _data, null, Stub.TRANSACTION_onGetPrintJobInfosResult);
                } finally {
                    _data.recycle();
                }
            }

            public void onGetCustomPrinterIconResult(Icon icon, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (icon != null) {
                        _data.writeInt(Stub.TRANSACTION_onGetPrintJobInfosResult);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sequence);
                    this.mRemote.transact(Stub.TRANSACTION_onGetCustomPrinterIconResult, _data, null, Stub.TRANSACTION_onGetPrintJobInfosResult);
                } finally {
                    _data.recycle();
                }
            }

            public void onCustomPrinterIconCached(int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sequence);
                    this.mRemote.transact(Stub.TRANSACTION_onCustomPrinterIconCached, _data, null, Stub.TRANSACTION_onGetPrintJobInfosResult);
                } finally {
                    _data.recycle();
                }
            }

            public void customPrinterIconCacheCleared(int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sequence);
                    this.mRemote.transact(Stub.TRANSACTION_customPrinterIconCacheCleared, _data, null, Stub.TRANSACTION_onGetPrintJobInfosResult);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPrintSpoolerCallbacks asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPrintSpoolerCallbacks)) {
                return new Proxy(obj);
            }
            return (IPrintSpoolerCallbacks) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case TRANSACTION_onGetPrintJobInfosResult /*1*/:
                    data.enforceInterface(DESCRIPTOR);
                    onGetPrintJobInfosResult(data.createTypedArrayList(PrintJobInfo.CREATOR), data.readInt());
                    return true;
                case TRANSACTION_onCancelPrintJobResult /*2*/:
                    data.enforceInterface(DESCRIPTOR);
                    onCancelPrintJobResult(data.readInt() != 0, data.readInt());
                    return true;
                case TRANSACTION_onSetPrintJobStateResult /*3*/:
                    data.enforceInterface(DESCRIPTOR);
                    onSetPrintJobStateResult(data.readInt() != 0, data.readInt());
                    return true;
                case TRANSACTION_onSetPrintJobTagResult /*4*/:
                    data.enforceInterface(DESCRIPTOR);
                    onSetPrintJobTagResult(data.readInt() != 0, data.readInt());
                    return true;
                case TRANSACTION_onGetPrintJobInfoResult /*5*/:
                    PrintJobInfo printJobInfo;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        printJobInfo = (PrintJobInfo) PrintJobInfo.CREATOR.createFromParcel(data);
                    } else {
                        printJobInfo = null;
                    }
                    onGetPrintJobInfoResult(printJobInfo, data.readInt());
                    return true;
                case TRANSACTION_onGetCustomPrinterIconResult /*6*/:
                    Icon icon;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        icon = (Icon) Icon.CREATOR.createFromParcel(data);
                    } else {
                        icon = null;
                    }
                    onGetCustomPrinterIconResult(icon, data.readInt());
                    return true;
                case TRANSACTION_onCustomPrinterIconCached /*7*/:
                    data.enforceInterface(DESCRIPTOR);
                    onCustomPrinterIconCached(data.readInt());
                    return true;
                case TRANSACTION_customPrinterIconCacheCleared /*8*/:
                    data.enforceInterface(DESCRIPTOR);
                    customPrinterIconCacheCleared(data.readInt());
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void customPrinterIconCacheCleared(int i) throws RemoteException;

    void onCancelPrintJobResult(boolean z, int i) throws RemoteException;

    void onCustomPrinterIconCached(int i) throws RemoteException;

    void onGetCustomPrinterIconResult(Icon icon, int i) throws RemoteException;

    void onGetPrintJobInfoResult(PrintJobInfo printJobInfo, int i) throws RemoteException;

    void onGetPrintJobInfosResult(List<PrintJobInfo> list, int i) throws RemoteException;

    void onSetPrintJobStateResult(boolean z, int i) throws RemoteException;

    void onSetPrintJobTagResult(boolean z, int i) throws RemoteException;
}

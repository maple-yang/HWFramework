package android.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFullBackupRestoreObserver extends IInterface {

    public static abstract class Stub extends Binder implements IFullBackupRestoreObserver {
        private static final String DESCRIPTOR = "android.app.backup.IFullBackupRestoreObserver";
        static final int TRANSACTION_onBackupPackage = 2;
        static final int TRANSACTION_onEndBackup = 3;
        static final int TRANSACTION_onEndRestore = 6;
        static final int TRANSACTION_onRestorePackage = 5;
        static final int TRANSACTION_onStartBackup = 1;
        static final int TRANSACTION_onStartRestore = 4;
        static final int TRANSACTION_onTimeout = 7;

        private static class Proxy implements IFullBackupRestoreObserver {
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

            public void onStartBackup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_onStartBackup, _data, null, Stub.TRANSACTION_onStartBackup);
                } finally {
                    _data.recycle();
                }
            }

            public void onBackupPackage(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(Stub.TRANSACTION_onBackupPackage, _data, null, Stub.TRANSACTION_onStartBackup);
                } finally {
                    _data.recycle();
                }
            }

            public void onEndBackup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_onEndBackup, _data, null, Stub.TRANSACTION_onStartBackup);
                } finally {
                    _data.recycle();
                }
            }

            public void onStartRestore() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_onStartRestore, _data, null, Stub.TRANSACTION_onStartBackup);
                } finally {
                    _data.recycle();
                }
            }

            public void onRestorePackage(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(Stub.TRANSACTION_onRestorePackage, _data, null, Stub.TRANSACTION_onStartBackup);
                } finally {
                    _data.recycle();
                }
            }

            public void onEndRestore() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_onEndRestore, _data, null, Stub.TRANSACTION_onStartBackup);
                } finally {
                    _data.recycle();
                }
            }

            public void onTimeout() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_onTimeout, _data, null, Stub.TRANSACTION_onStartBackup);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFullBackupRestoreObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFullBackupRestoreObserver)) {
                return new Proxy(obj);
            }
            return (IFullBackupRestoreObserver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case TRANSACTION_onStartBackup /*1*/:
                    data.enforceInterface(DESCRIPTOR);
                    onStartBackup();
                    return true;
                case TRANSACTION_onBackupPackage /*2*/:
                    data.enforceInterface(DESCRIPTOR);
                    onBackupPackage(data.readString());
                    return true;
                case TRANSACTION_onEndBackup /*3*/:
                    data.enforceInterface(DESCRIPTOR);
                    onEndBackup();
                    return true;
                case TRANSACTION_onStartRestore /*4*/:
                    data.enforceInterface(DESCRIPTOR);
                    onStartRestore();
                    return true;
                case TRANSACTION_onRestorePackage /*5*/:
                    data.enforceInterface(DESCRIPTOR);
                    onRestorePackage(data.readString());
                    return true;
                case TRANSACTION_onEndRestore /*6*/:
                    data.enforceInterface(DESCRIPTOR);
                    onEndRestore();
                    return true;
                case TRANSACTION_onTimeout /*7*/:
                    data.enforceInterface(DESCRIPTOR);
                    onTimeout();
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onBackupPackage(String str) throws RemoteException;

    void onEndBackup() throws RemoteException;

    void onEndRestore() throws RemoteException;

    void onRestorePackage(String str) throws RemoteException;

    void onStartBackup() throws RemoteException;

    void onStartRestore() throws RemoteException;

    void onTimeout() throws RemoteException;
}

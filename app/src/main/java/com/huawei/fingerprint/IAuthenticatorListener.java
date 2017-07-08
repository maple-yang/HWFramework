package com.huawei.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAuthenticatorListener extends IInterface {

    public static abstract class Stub extends Binder implements IAuthenticatorListener {
        private static final String DESCRIPTOR = "com.huawei.fingerprint.IAuthenticatorListener";
        static final int TRANSACTION_onUserVerificationResult = 1;

        private static class Proxy implements IAuthenticatorListener {
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

            public void onUserVerificationResult(int result, byte[] userid, byte[] encapsulatedResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeByteArray(userid);
                    _data.writeByteArray(encapsulatedResult);
                    this.mRemote.transact(Stub.TRANSACTION_onUserVerificationResult, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAuthenticatorListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAuthenticatorListener)) {
                return new Proxy(obj);
            }
            return (IAuthenticatorListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case TRANSACTION_onUserVerificationResult /*1*/:
                    data.enforceInterface(DESCRIPTOR);
                    onUserVerificationResult(data.readInt(), data.createByteArray(), data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onUserVerificationResult(int i, byte[] bArr, byte[] bArr2) throws RemoteException;
}

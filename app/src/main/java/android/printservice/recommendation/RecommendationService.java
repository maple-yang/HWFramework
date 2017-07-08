package android.printservice.recommendation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.printservice.recommendation.IRecommendationService.Stub;
import android.util.Log;
import java.util.List;

public abstract class RecommendationService extends Service {
    private static final String LOG_TAG = "PrintServiceRecS";
    public static final String SERVICE_INTERFACE = "android.printservice.recommendation.RecommendationService";
    private IRecommendationServiceCallbacks mCallbacks;
    private Handler mHandler;

    private class MyHandler extends Handler {
        static final int MSG_CONNECT = 1;
        static final int MSG_DISCONNECT = 2;
        static final int MSG_UPDATE = 3;

        MyHandler() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONNECT /*1*/:
                    RecommendationService.this.mCallbacks = (IRecommendationServiceCallbacks) msg.obj;
                    RecommendationService.this.onConnected();
                case MSG_DISCONNECT /*2*/:
                    RecommendationService.this.onDisconnected();
                    RecommendationService.this.mCallbacks = null;
                case MSG_UPDATE /*3*/:
                    try {
                        RecommendationService.this.mCallbacks.onRecommendationsUpdated((List) msg.obj);
                    } catch (Exception e) {
                        Log.e(RecommendationService.LOG_TAG, "Could not update recommended services", e);
                    }
                default:
            }
        }
    }

    public abstract void onConnected();

    public abstract void onDisconnected();

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mHandler = new MyHandler();
    }

    public final void updateRecommendations(List<RecommendationInfo> recommendations) {
        this.mHandler.obtainMessage(3, recommendations).sendToTarget();
    }

    public final IBinder onBind(Intent intent) {
        return new Stub() {
            public void registerCallbacks(IRecommendationServiceCallbacks callbacks) {
                if (callbacks != null) {
                    RecommendationService.this.mHandler.obtainMessage(1, callbacks).sendToTarget();
                } else {
                    RecommendationService.this.mHandler.obtainMessage(2).sendToTarget();
                }
            }
        };
    }
}

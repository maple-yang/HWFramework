package huawei.android.storage;

import android.os.Handler;
import com.android.server.storage.AbsDeviceStorageMonitorService;

public class HwCustDeviceStorageMonitorService {
    protected AbsDeviceStorageMonitorService mService;

    public HwCustDeviceStorageMonitorService(AbsDeviceStorageMonitorService obj, Handler handler) {
        this.mService = null;
        this.mService = obj;
    }

    public void clearMemoryForCritiLow() {
    }

    public long getCritiLowMemThreshold() {
        return 0;
    }
}

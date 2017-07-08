package com.android.server;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Slog;
import com.android.server.job.controllers.JobStatus;

public class AnyMotionDetector {
    private static final long ACCELEROMETER_DATA_TIMEOUT_MILLIS = 3000;
    private static final boolean DEBUG = false;
    private static final long ORIENTATION_MEASUREMENT_DURATION_MILLIS = 2500;
    private static final long ORIENTATION_MEASUREMENT_INTERVAL_MILLIS = 5000;
    public static final int RESULT_MOVED = 1;
    public static final int RESULT_STATIONARY = 0;
    public static final int RESULT_UNKNOWN = -1;
    private static final int SAMPLING_INTERVAL_MILLIS = 40;
    private static final int STALE_MEASUREMENT_TIMEOUT_MILLIS = 120000;
    private static final int STATE_ACTIVE = 1;
    private static final int STATE_INACTIVE = 0;
    private static final String TAG = "AnyMotionDetector";
    private final float THRESHOLD_ENERGY;
    private Sensor mAccelSensor;
    private DeviceIdleCallback mCallback;
    private Vector3 mCurrentGravityVector;
    private final Handler mHandler;
    private final SensorEventListener mListener;
    private final Object mLock;
    private boolean mMeasurementInProgress;
    private final Runnable mMeasurementTimeout;
    private int mNumSufficientSamples;
    private Vector3 mPreviousGravityVector;
    private RunningSignalStats mRunningStats;
    private SensorManager mSensorManager;
    private final Runnable mSensorRestart;
    private int mState;
    private final float mThresholdAngle;
    private WakeLock mWakeLock;

    interface DeviceIdleCallback {
        void onAnyMotionResult(int i);
    }

    private static class RunningSignalStats {
        Vector3 currentVector;
        float energy;
        Vector3 previousVector;
        Vector3 runningSum;
        int sampleCount;

        public RunningSignalStats() {
            reset();
        }

        public void reset() {
            this.previousVector = null;
            this.currentVector = null;
            this.runningSum = new Vector3(0, 0.0f, 0.0f, 0.0f);
            this.energy = 0.0f;
            this.sampleCount = AnyMotionDetector.STATE_INACTIVE;
        }

        public void accumulate(Vector3 v) {
            if (v != null) {
                this.sampleCount += AnyMotionDetector.STATE_ACTIVE;
                this.runningSum = this.runningSum.plus(v);
                this.previousVector = this.currentVector;
                this.currentVector = v;
                if (this.previousVector != null) {
                    Vector3 dv = this.currentVector.minus(this.previousVector);
                    this.energy += ((dv.x * dv.x) + (dv.y * dv.y)) + (dv.z * dv.z);
                }
            }
        }

        public Vector3 getRunningAverage() {
            if (this.sampleCount > 0) {
                return this.runningSum.times(1.0f / ((float) this.sampleCount));
            }
            return null;
        }

        public float getEnergy() {
            return this.energy;
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public String toString() {
            return ((("" + "previousVector = " + (this.previousVector == null ? "null" : this.previousVector.toString())) + ", currentVector = " + (this.currentVector == null ? "null" : this.currentVector.toString())) + ", sampleCount = " + this.sampleCount) + ", energy = " + this.energy;
        }
    }

    public static final class Vector3 {
        public long timeMillisSinceBoot;
        public float x;
        public float y;
        public float z;

        public Vector3(long timeMillisSinceBoot, float x, float y, float z) {
            this.timeMillisSinceBoot = timeMillisSinceBoot;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float norm() {
            return (float) Math.sqrt((double) dotProduct(this));
        }

        public Vector3 normalized() {
            float mag = norm();
            return new Vector3(this.timeMillisSinceBoot, this.x / mag, this.y / mag, this.z / mag);
        }

        public float angleBetween(Vector3 other) {
            float degrees = Math.abs((float) Math.toDegrees(Math.atan2((double) cross(other).norm(), (double) dotProduct(other))));
            Slog.d(AnyMotionDetector.TAG, "angleBetween: this = " + toString() + ", other = " + other.toString() + ", degrees = " + degrees);
            return degrees;
        }

        public Vector3 cross(Vector3 v) {
            return new Vector3(v.timeMillisSinceBoot, (this.y * v.z) - (this.z * v.y), (this.z * v.x) - (this.x * v.z), (this.x * v.y) - (this.y * v.x));
        }

        public String toString() {
            return ((("" + "timeMillisSinceBoot=" + this.timeMillisSinceBoot) + " | x=" + this.x) + ", y=" + this.y) + ", z=" + this.z;
        }

        public float dotProduct(Vector3 v) {
            return ((this.x * v.x) + (this.y * v.y)) + (this.z * v.z);
        }

        public Vector3 times(float val) {
            return new Vector3(this.timeMillisSinceBoot, this.x * val, this.y * val, this.z * val);
        }

        public Vector3 plus(Vector3 v) {
            return new Vector3(v.timeMillisSinceBoot, v.x + this.x, v.y + this.y, v.z + this.z);
        }

        public Vector3 minus(Vector3 v) {
            return new Vector3(v.timeMillisSinceBoot, this.x - v.x, this.y - v.y, this.z - v.z);
        }
    }

    public AnyMotionDetector(PowerManager pm, Handler handler, SensorManager sm, DeviceIdleCallback callback, float thresholdAngle) {
        this.THRESHOLD_ENERGY = 5.0f;
        this.mLock = new Object();
        this.mCurrentGravityVector = null;
        this.mPreviousGravityVector = null;
        this.mCallback = null;
        this.mListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                int status = AnyMotionDetector.RESULT_UNKNOWN;
                synchronized (AnyMotionDetector.this.mLock) {
                    AnyMotionDetector.this.mRunningStats.accumulate(new Vector3(SystemClock.elapsedRealtime(), event.values[AnyMotionDetector.STATE_INACTIVE], event.values[AnyMotionDetector.STATE_ACTIVE], event.values[2]));
                    if (AnyMotionDetector.this.mRunningStats.getSampleCount() >= AnyMotionDetector.this.mNumSufficientSamples) {
                        status = AnyMotionDetector.this.stopOrientationMeasurementLocked();
                    }
                }
                if (status != AnyMotionDetector.RESULT_UNKNOWN) {
                    AnyMotionDetector.this.mCallback.onAnyMotionResult(status);
                }
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        this.mSensorRestart = new Runnable() {
            public void run() {
                synchronized (AnyMotionDetector.this.mLock) {
                    AnyMotionDetector.this.startOrientationMeasurementLocked();
                }
            }
        };
        this.mMeasurementTimeout = new Runnable() {
            public void run() {
                synchronized (AnyMotionDetector.this.mLock) {
                    int status = AnyMotionDetector.this.stopOrientationMeasurementLocked();
                }
                if (status != AnyMotionDetector.RESULT_UNKNOWN) {
                    AnyMotionDetector.this.mCallback.onAnyMotionResult(status);
                }
            }
        };
        synchronized (this.mLock) {
            this.mWakeLock = pm.newWakeLock(STATE_ACTIVE, TAG);
            this.mWakeLock.setReferenceCounted(DEBUG);
            this.mHandler = handler;
            this.mSensorManager = sm;
            this.mAccelSensor = this.mSensorManager.getDefaultSensor(STATE_ACTIVE);
            this.mMeasurementInProgress = DEBUG;
            this.mState = STATE_INACTIVE;
            this.mCallback = callback;
            this.mThresholdAngle = thresholdAngle;
            this.mRunningStats = new RunningSignalStats();
            this.mNumSufficientSamples = (int) Math.ceil(62.5d);
        }
    }

    public void checkForAnyMotion() {
        if (this.mState != STATE_ACTIVE) {
            synchronized (this.mLock) {
                this.mState = STATE_ACTIVE;
                this.mCurrentGravityVector = null;
                this.mPreviousGravityVector = null;
                this.mWakeLock.acquire();
                startOrientationMeasurementLocked();
            }
        }
    }

    public void stop() {
        if (this.mState == STATE_ACTIVE) {
            synchronized (this.mLock) {
                this.mState = STATE_INACTIVE;
                if (this.mMeasurementInProgress) {
                    this.mMeasurementInProgress = DEBUG;
                    this.mSensorManager.unregisterListener(this.mListener);
                }
                this.mHandler.removeCallbacks(this.mMeasurementTimeout);
                this.mHandler.removeCallbacks(this.mSensorRestart);
                this.mCurrentGravityVector = null;
                this.mPreviousGravityVector = null;
                this.mWakeLock.release();
            }
        }
    }

    private void startOrientationMeasurementLocked() {
        if (!this.mMeasurementInProgress && this.mAccelSensor != null) {
            if (this.mSensorManager.registerListener(this.mListener, this.mAccelSensor, EventLogTags.VOLUME_CHANGED)) {
                this.mMeasurementInProgress = true;
                this.mRunningStats.reset();
            }
            Message msg = Message.obtain(this.mHandler, this.mMeasurementTimeout);
            msg.setAsynchronous(true);
            this.mHandler.sendMessageDelayed(msg, ACCELEROMETER_DATA_TIMEOUT_MILLIS);
        }
    }

    private int stopOrientationMeasurementLocked() {
        int status = RESULT_UNKNOWN;
        if (this.mMeasurementInProgress) {
            this.mSensorManager.unregisterListener(this.mListener);
            this.mHandler.removeCallbacks(this.mMeasurementTimeout);
            long detectionEndTime = SystemClock.elapsedRealtime();
            this.mMeasurementInProgress = DEBUG;
            this.mPreviousGravityVector = this.mCurrentGravityVector;
            this.mCurrentGravityVector = this.mRunningStats.getRunningAverage();
            this.mRunningStats.reset();
            status = getStationaryStatus();
            if (status != RESULT_UNKNOWN) {
                this.mWakeLock.release();
                this.mState = STATE_INACTIVE;
            } else {
                Message msg = Message.obtain(this.mHandler, this.mSensorRestart);
                msg.setAsynchronous(true);
                this.mHandler.sendMessageDelayed(msg, ORIENTATION_MEASUREMENT_INTERVAL_MILLIS);
            }
        }
        return status;
    }

    public int getStationaryStatus() {
        if (this.mPreviousGravityVector == null || this.mCurrentGravityVector == null) {
            return RESULT_UNKNOWN;
        }
        float angle = this.mPreviousGravityVector.normalized().angleBetween(this.mCurrentGravityVector.normalized());
        if (angle >= this.mThresholdAngle || this.mRunningStats.getEnergy() >= 5.0f) {
            return (!Float.isNaN(angle) && this.mCurrentGravityVector.timeMillisSinceBoot - this.mPreviousGravityVector.timeMillisSinceBoot > JobStatus.DEFAULT_TRIGGER_MAX_DELAY) ? RESULT_UNKNOWN : STATE_ACTIVE;
        } else {
            return STATE_INACTIVE;
        }
    }
}

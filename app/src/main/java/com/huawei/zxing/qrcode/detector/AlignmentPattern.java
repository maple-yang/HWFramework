package com.huawei.zxing.qrcode.detector;

import com.huawei.zxing.ResultPoint;

public final class AlignmentPattern extends ResultPoint {
    private final float estimatedModuleSize;

    AlignmentPattern(float posX, float posY, float estimatedModuleSize) {
        super(posX, posY);
        this.estimatedModuleSize = estimatedModuleSize;
    }

    boolean aboutEquals(float moduleSize, float i, float j) {
        boolean z = true;
        if (Math.abs(i - getY()) > moduleSize || Math.abs(j - getX()) > moduleSize) {
            return false;
        }
        float moduleSizeDiff = Math.abs(moduleSize - this.estimatedModuleSize);
        if (moduleSizeDiff > 1.0f && moduleSizeDiff > this.estimatedModuleSize) {
            z = false;
        }
        return z;
    }

    AlignmentPattern combineEstimate(float i, float j, float newModuleSize) {
        return new AlignmentPattern((getX() + j) / 2.0f, (getY() + i) / 2.0f, (this.estimatedModuleSize + newModuleSize) / 2.0f);
    }
}

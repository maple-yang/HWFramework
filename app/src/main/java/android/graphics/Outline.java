package android.graphics;

import android.graphics.Path.Direction;

public final class Outline {
    public static final int MODE_CONVEX_PATH = 2;
    public static final int MODE_EMPTY = 0;
    public static final int MODE_ROUND_RECT = 1;
    private static final float RADIUS_UNDEFINED = Float.NEGATIVE_INFINITY;
    public float mAlpha;
    public int mMode;
    public final Path mPath;
    public float mRadius;
    public final Rect mRect;

    public Outline() {
        this.mMode = MODE_EMPTY;
        this.mPath = new Path();
        this.mRect = new Rect();
        this.mRadius = RADIUS_UNDEFINED;
    }

    public Outline(Outline src) {
        this.mMode = MODE_EMPTY;
        this.mPath = new Path();
        this.mRect = new Rect();
        this.mRadius = RADIUS_UNDEFINED;
        set(src);
    }

    public void setEmpty() {
        this.mMode = MODE_EMPTY;
        this.mPath.rewind();
        this.mRect.setEmpty();
        this.mRadius = RADIUS_UNDEFINED;
    }

    public boolean isEmpty() {
        return this.mMode == 0;
    }

    public boolean canClip() {
        return this.mMode != MODE_CONVEX_PATH;
    }

    public void setAlpha(float alpha) {
        this.mAlpha = alpha;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public void set(Outline src) {
        this.mMode = src.mMode;
        this.mPath.set(src.mPath);
        this.mRect.set(src.mRect);
        this.mRadius = src.mRadius;
        this.mAlpha = src.mAlpha;
    }

    public void setRect(int left, int top, int right, int bottom) {
        setRoundRect(left, top, right, bottom, 0.0f);
    }

    public void setRect(Rect rect) {
        setRect(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void setRoundRect(int left, int top, int right, int bottom, float radius) {
        if (left >= right || top >= bottom) {
            setEmpty();
            return;
        }
        this.mMode = MODE_ROUND_RECT;
        this.mRect.set(left, top, right, bottom);
        this.mRadius = radius;
        this.mPath.rewind();
    }

    public void setRoundRect(Rect rect, float radius) {
        setRoundRect(rect.left, rect.top, rect.right, rect.bottom, radius);
    }

    public boolean getRect(Rect outRect) {
        if (this.mMode != MODE_ROUND_RECT) {
            return false;
        }
        outRect.set(this.mRect);
        return true;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setOval(int left, int top, int right, int bottom) {
        if (left >= right || top >= bottom) {
            setEmpty();
        } else if (bottom - top == right - left) {
            setRoundRect(left, top, right, bottom, ((float) (bottom - top)) / 2.0f);
        } else {
            this.mMode = MODE_CONVEX_PATH;
            this.mPath.rewind();
            this.mPath.addOval((float) left, (float) top, (float) right, (float) bottom, Direction.CW);
            this.mRect.setEmpty();
            this.mRadius = RADIUS_UNDEFINED;
        }
    }

    public void setOval(Rect rect) {
        setOval(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void setConvexPath(Path convexPath) {
        if (convexPath.isEmpty()) {
            setEmpty();
        } else if (convexPath.isConvex()) {
            this.mMode = MODE_CONVEX_PATH;
            this.mPath.set(convexPath);
            this.mRect.setEmpty();
            this.mRadius = RADIUS_UNDEFINED;
        } else {
            throw new IllegalArgumentException("path must be convex");
        }
    }

    public void offset(int dx, int dy) {
        if (this.mMode == MODE_ROUND_RECT) {
            this.mRect.offset(dx, dy);
        } else if (this.mMode == MODE_CONVEX_PATH) {
            this.mPath.offset((float) dx, (float) dy);
        }
    }
}

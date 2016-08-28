package com.tbuonomo.squaresandcirclesloading;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

/**
 * Created by tommy on 28/08/16.
 */
public class QuartCircle extends GradientDrawable {

    private Paint paint;
    private RectF rectF;
    private int color;
    private Direction angle;
    private RectF bounds;
    private boolean fill;

    public enum Direction {
        BOTTOM_LEFT ((float) Math.toDegrees(Math.PI)),
        BOTTOM_RIGHT ((float) Math.toDegrees(Math.PI / 2)),
        TOP_RIGHT (0),
        TOP_LEFT ((float) Math.toDegrees(Math.PI * 3 / 2));

        private float startAngle;

        Direction(float startAngle) {
            this.startAngle = startAngle;
        }

        public float getStartAngle() {
            return startAngle;
        }
    }

    public QuartCircle() {
        this(Color.BLUE, Direction.BOTTOM_LEFT);
    }

    public QuartCircle(int color, Direction angle) {
        this.color = color;
        this.angle = angle;
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        rectF = new RectF();
    }

    /**
     * A 32bit color not a color resources.
     *
     * @param color
     */
    @Override
    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        invalidateSelf();
    }

    public void setAngle(Direction angle) {
        this.angle = angle;
        invalidateSelf();
    }

    public void setFill(boolean fill) {
        this.fill = fill;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        Log.i(QuartCircle.class.getSimpleName(), "draw: " + bounds);
        float sweepAngle = fill ? 360 : (float) Math.toDegrees((Math.PI * 3 / 2));
        canvas.drawArc(bounds, angle.getStartAngle(), sweepAngle, true, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        // Has no effect
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // Has no effect
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.bounds = new RectF(bounds);
        Log.i(QuartCircle.class.getSimpleName(), "onBoundsChange: " + bounds);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        // Not Implemented
        return 0;
    }

}

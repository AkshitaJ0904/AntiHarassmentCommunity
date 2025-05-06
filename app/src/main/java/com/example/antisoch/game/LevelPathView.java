package com.example.antisoch.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.DashPathEffect;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class LevelPathView extends View {

    private Paint paint;
    private List<PointF> points;

    public LevelPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFF8B4513); // Brownish color
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        PathEffect effects = new DashPathEffect(new float[]{20, 10}, 0); // dash-gap
        paint.setPathEffect(effects);
    }

    public void setPoints(List<PointF> points) {
        this.points = points;
        invalidate(); // redraw view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (points == null || points.size() < 2) return;

        for (int i = 0; i < points.size() - 1; i++) {
            PointF start = points.get(i);
            PointF end = points.get(i + 1);

            float controlX = (start.x + end.x) / 2;
            float controlY = (start.y + end.y) / 2 - 100; // Adjust curvature height here

            android.graphics.Path path = new android.graphics.Path();
            path.moveTo(start.x, start.y);
            path.quadTo(controlX, controlY, end.x, end.y);

            canvas.drawPath(path, paint);
        }
    }

}

package baidumapsdk.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diaoqf on 2016/7/14.
 */
public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    //是否在绘制状态
    private boolean is_drawing;

    private SurfaceHolder surfaceHolder = null;

    private Paint paint;
    private Canvas canvas;

    private List<PointF> screenPath;

    private OnDrawListener onDrawListener;

    public DrawView(Context context) {
        super(context);
        init(null);
    }


    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }



    private void init(AttributeSet attrs) {
        is_drawing = false;
        screenPath = new ArrayList<>();

        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                is_drawing = true;
                clear();
                screenPath.add(new PointF(event.getX(),event.getY()));
                break;
            case MotionEvent.ACTION_MOVE:
                screenPath.add(new PointF(event.getX(),event.getY()));
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                is_drawing = false;
                if (onDrawListener != null) {
                    onDrawListener.finished();
                }
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (screenPath.size() > 1) {
            for (int i=1;i<screenPath.size();i++){
                canvas.drawLine(screenPath.get(i-1).x,screenPath.get(i-1).y,screenPath.get(i).x,screenPath.get(i).y,paint);
            }
        }
    }

    //清除绘图和坐标数组
    public void clear(){
        screenPath.clear();
        invalidate();
    }

    public List<PointF> getScreenPath() {
        return screenPath;
    }

    public void setOnDrawListener(OnDrawListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public interface OnDrawListener{
        void finished();
    }
}

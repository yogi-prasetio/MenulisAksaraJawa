package com.android.menulisaksarajawa.ui.view.siswa;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.android.menulisaksarajawa.R;
import com.android.menulisaksarajawa.ui.model.pip.PointInPolygon;
import com.android.menulisaksarajawa.ui.utils.LetterFactory;
import com.android.menulisaksarajawa.ui.utils.LetterStrokeBean;
import com.android.menulisaksarajawa.ui.utils.LogUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {
    public static String TAG = CanvasView.class.getSimpleName();

    private Paint mPaint;
    private Paint pointPaint;
    private Paint processingPaint;

    private Bitmap anchorBitmap;
    private Bitmap letterBitmap;
    private Bitmap traceBitmap;

    private PointF anchorPos = new PointF();
    private Rect anchorRect;
    private Rect letterRect = null;
    private RectF scaleRect = new RectF();
    private RectF viewRect = null;

    private float anchorScale = 0f;
    private float validArea;
    private float toleranceArea;
    private float[] pathPoints;
    private Path currentDrawingPath;
    private Path pathToCheck;

    private int currentStokeProgress = -1;
    private int currentStroke = 0;
    private int viewHeight = -1;
    private int viewWidth = -1;
    private boolean hasFinishOneStroke = false;
    private boolean letterTracingFinished = false;
    private boolean needInstruct;   
    
    private List<Path> paths = new ArrayList<>();
    private LetterStrokeBean strokeBean;        
    private String tracingAssets;
    private TracingListener tracingListener;   

    public interface TracingListener {
        void onFinish();

        void onTracing(PointInPolygon pointInPolygon);
    }

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CanvasView);
        final Resources res = getResources();

        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);

        this.pointPaint = new Paint();
        this.pointPaint.setAntiAlias(true);

        Float pointWidth = Float.valueOf(typedArray.getString(R.styleable.CanvasView_pointWidth));
        if (pointWidth != null){
            this.pointPaint.setStrokeWidth(pointWidth);
        } else {
            this.pointPaint.setStrokeWidth(15.0f);
        }

//        this.pointPaint.setColor(typedArray.getColor(R.styleable.CanvasView_pointColor, Color.RED));
        this.pointPaint.setStyle(Paint.Style.STROKE);
        this.pointPaint.setStrokeCap(Paint.Cap.ROUND);
        this.pointPaint.setStrokeJoin(Paint.Join.ROUND);

        this.processingPaint = new Paint();
        this.processingPaint.setAntiAlias(true);
        this.processingPaint.setDither(true);
        this.processingPaint.setStyle(Paint.Style.STROKE);
        this.processingPaint.setStrokeCap(Paint.Cap.ROUND);
        this.processingPaint.setStrokeJoin(Paint.Join.ROUND);
        this.processingPaint.setPathEffect(new CornerPathEffect( 60.0f));
        this.processingPaint.setColor(typedArray.getColor(R.styleable.CanvasView_strokeColor, Color.parseColor("#000000")));

        //dim error
        this.validArea = 30.0f;
        this.toleranceArea = 50.0f;

        Float strokeWidth = typedArray.getFloat(R.styleable.CanvasView_paintWidth, 0);
        if (strokeWidth != null){
            this.processingPaint.setStrokeWidth(strokeWidth);
        } else {
            this.processingPaint.setStrokeWidth(50f);
        }
        Drawable drawable = typedArray.getDrawable(R.styleable.CanvasView_anchorDrawable);
        if (drawable != null) {
            this.anchorBitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            this.anchorBitmap = getBitmapByAssetName("crayon.png");
        }
        typedArray.recycle();
    }

    public void setTracingListener(TracingListener tracingListener) {
        this.tracingListener = tracingListener;
    }

    public void setLetterChar(String letterChar) {
        LetterFactory letterFactory = new LetterFactory();
        letterFactory.setLetter(letterChar);
        initializeLetterAssets(letterFactory.getLetterAssets(), letterFactory.getStrokeAssets());
    }

    private void initializeLetterAssets(String letterAssets, String strokeAssets) {
        this.tracingAssets = tracingAssets;
        InputStream stream = null;
        try {
            this.letterBitmap = getBitmapByAssetName(letterAssets);

            this.letterRect = new Rect(0, 0, this.letterBitmap.getWidth(), this.letterBitmap.getHeight());
            this.anchorRect = new Rect(0, 0, this.anchorBitmap.getWidth(), this.anchorBitmap.getHeight());

            stream = getContext().getAssets().open(strokeAssets);
            this.strokeBean = (LetterStrokeBean) new Gson().fromJson(new InputStreamReader(stream), LetterStrokeBean.class);
//            strokeBean = gson.fromJson(new InputStreamReader(stream), LetterStrokeBean.class);
             if (stream != null) {
                 try {
                     stream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e2.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (this.viewWidth == -1 || this.viewHeight == -1) {
            this.viewWidth = getWidth();
            this.viewHeight = getHeight();
//            this.processingPaint.setStrokeWidth(((float) this.viewHeight) / 9.0f);
            this.viewRect = new RectF();
            this.viewRect.set(0.0f, 0.0f, (float) this.viewWidth, (float) this.viewHeight);
            this.anchorScale = ((float) this.viewHeight) / (((float) this.anchorBitmap.getHeight()) * 12f);
            LetterStrokeBean letterStrokeBean = this.strokeBean;
            if (letterStrokeBean != null) {
                String[] pointArray = ((String) ((LetterStrokeBean.Strokes) letterStrokeBean.strokes.get(0)).points.get(0)).split(",");
                PointF pointF = this.anchorPos;
                double d = (double) this.viewWidth;
                double doubleValue = Double.valueOf(pointArray[0]).doubleValue();
                Double.isNaN(d);
                float f = (float) (d * doubleValue);
                d = (double) this.viewHeight;
                doubleValue = Double.valueOf(pointArray[1]).doubleValue();
                Double.isNaN(d);
                pointF.set(f, (float) (d * doubleValue));
                this.pathToCheck = createPath(((LetterStrokeBean.Strokes) this.strokeBean.strokes.get(this.currentStroke)).points);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(this.letterBitmap, this.letterRect, this.viewRect, this.mPaint);
        canvas.drawPoints(this.pathPoints, this.pointPaint);
        for (Path item : this.paths) {
            canvas.drawPath(item, this.processingPaint);
        }
        Path path = this.currentDrawingPath;
        if (path != null) {
            canvas.drawPath(path, this.processingPaint);
        }
        this.scaleRect.set(this.anchorPos.x - ((((float) this.anchorBitmap.getWidth()) * this.anchorScale) / 2.0f), this.anchorPos.y - ((((float) this.anchorBitmap.getHeight()) * this.anchorScale) / 2.0f), this.anchorPos.x + ((((float) this.anchorBitmap.getWidth()) * this.anchorScale) / 2.0f), this.anchorPos.y + ((((float) this.anchorBitmap.getHeight()) * this.anchorScale) / 2.0f));
        canvas.drawBitmap(this.anchorBitmap, this.anchorRect, this.scaleRect, this.mPaint);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Bitmap bitmap = this.letterBitmap;
        if (bitmap != null) {
            float scale = bitmap.getWidth() / (bitmap.getHeight() * 1f);
            int width = (int)(height * scale);
            setMeasuredDimension(width, height);

            // setMeasuredDimension((int) (((float) height) * (((float) bitmap.getWidth()) / (((float) letterBitmap.getHeight()) * 1.0f))), height);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    public void setStrokeWidth(@ColorInt int strokeColor) {
//        Paint paint = processingPaint;
//        if (paint != null) {
//            paint.setColor(strokeColor);
//        }
//    }
//
//    public void setPointWidth(@ColorInt int strokeColor) {
//        Paint paint = processingPaint;
//        if (paint != null) {
//            paint.setColor(strokeColor);
//        }
//    }
    public void setStrokeColor(@ColorInt int strokeColor) {
        Paint paint = processingPaint;
        if (paint != null) {
            paint.setColor(strokeColor);
        }
    }

    public void setPointColor(@ColorInt int pointColor) {
        Paint paint = pointPaint;
        if (paint != null) {
            paint.setColor(pointColor);
        }
    }

    private float[] toPoint(String pointStr) {
        String[] pointArray = pointStr.split(",");
        return new float[]{Float.parseFloat(pointArray[0]) * ((float) viewWidth), Float.parseFloat(pointArray[1]) * ((float) viewHeight)};
    }

    private boolean isValidPoint(String trackStr, float x, float y) {
        float[] points = toPoint(trackStr);
        if (Math.abs(x - points[0]) >= validArea || Math.abs(y - points[1]) >= validArea) {
            return false;
        }
        return true;
    }

    private boolean overlapped(int x, int y) {
        RectF touchPoint = new RectF(x - toleranceArea, y - toleranceArea,
                x + toleranceArea, y + toleranceArea);
        Path touchPointPath = new Path();
        touchPointPath.addRect(touchPoint, Path.Direction.CW);
        touchPointPath.addCircle(x, y, 20, Path.Direction.CW);
        touchPointPath.close();
        Path hourPathCopy = new Path(this.pathToCheck);
        hourPathCopy.op(touchPointPath, Path.Op.INTERSECT);
        touchPointPath.reset();
        RectF bounds = new RectF();
        hourPathCopy.computeBounds(bounds, true);
        return bounds.left != 0.0 && bounds.top != 0.0 && bounds.right != 0.0 && bounds.bottom != 0.0;
    }

    private boolean isTracingStartPoint(float x, float y) {
        boolean rightArea = Math.abs((anchorPos.x + ((float) anchorBitmap.getWidth())) - x) < toleranceArea && Math.abs(anchorPos.y - y) < ((float) anchorBitmap.getHeight()) + toleranceArea;
        boolean leftArea = Math.abs(x - anchorPos.x) < toleranceArea && Math.abs(y - anchorPos.y) < toleranceArea;
        if (leftArea || rightArea) {
            return true;
        }
        return false;
    }


    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hasFinishOneStroke = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (hasFinishOneStroke) return false;
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (currentStroke >= strokeBean.strokes.size()) {
            LogUtils.i(TAG, "BREAK TOUCH");
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        List<String> points = strokeBean.getCurrentStrokePoints(currentStroke);

        int action = event.getAction();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isTracingStartPoint(x, y)) {
                    return false;
                }
                LogUtils.i(TAG, "event: DOWN");
                if (currentStokeProgress == -1) {
                    currentDrawingPath = new Path();
                    currentDrawingPath.moveTo(anchorPos.x, anchorPos.y);
                    currentStokeProgress = 1;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i(TAG, "event: MOVE");
                float[] point;
                if (currentStokeProgress < points.size() && overlapped((int) x, (int) y)) {
                    if (isValidPoint((String) points.get(currentStokeProgress), x, y)) {
                        currentStokeProgress++;
                    }
                    point = toPoint((String) points.get(currentStokeProgress - 1));

                    currentDrawingPath.lineTo(point[0], point[1]);
//                    if (needInstruct) {
//                        currentDrawingPath.lineTo(point[0], point[1]);
//                    } else {
//                        currentDrawingPath.lineTo(x, y);
//                    }

                    if (tracingListener != null) {
                        tracingListener.onTracing(new PointInPolygon(x, y, points));
                    }
                } else if (currentStokeProgress == points.size()) {
                    if (isValidPoint((String) points.get(currentStokeProgress - 1), x, y)) {
                        point = toPoint((String) points.get(currentStokeProgress - 1));
                        currentDrawingPath.lineTo(point[0], point[1]);
                    }
                    if (currentStroke < strokeBean.strokes.size() - 1) {
                        paths.add(currentDrawingPath);
                        currentStroke++;
                        pathToCheck = createPath(((LetterStrokeBean.Strokes) strokeBean.strokes.get(currentStroke)).points);
                        currentStokeProgress = -1;
                        float[] stepPoints = toPoint((String) ((LetterStrokeBean.Strokes) strokeBean.strokes.get(currentStroke % strokeBean.strokes.size())).points.get(0));
                        anchorPos.set(stepPoints[0], stepPoints[1]);
                        hasFinishOneStroke = true;
                        invalidate();
                        return false;
                    } else if (!letterTracingFinished) {
                        letterTracingFinished = true;
                        hasFinishOneStroke = true;
                        if (tracingListener != null) {
                            tracingListener.onFinish();
                        }
                    }
                } else {
                    float[] stepPoints2 = toPoint((String) ((LetterStrokeBean.Strokes) strokeBean.strokes.get(currentStroke % strokeBean.strokes.size())).points.get(0));
                    anchorPos.set(stepPoints2[0], stepPoints2[1]);
                    currentStokeProgress = -1;
                    currentDrawingPath = null;
                    invalidate();
                    hasFinishOneStroke = true;
                    return false;
                }
                anchorPos.set(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i(TAG, "event: UP");
                break;
        }
        return true;
    }

    public Path createPath(List<String> pathStr) {
        int i;
        Path path = new Path();
        path.setFillType(Path.FillType.WINDING);
        List<Float> points = new ArrayList();
        for (i = 0; i < pathStr.size(); i++) {
            String[] pointArray = ((String) pathStr.get(i)).split(",");
            double d = (double) viewWidth;
            double doubleValue = Double.valueOf(pointArray[0]).doubleValue();
            Double.isNaN(d);
            float x = (float) (d * doubleValue);
            double d2 = (double) this.viewHeight;
            double doubleValue2 = Double.valueOf(pointArray[1]).doubleValue();
            Double.isNaN(d2);
            float y = (float) (d2 * doubleValue2);
            points.add(Float.valueOf(x));
            points.add(Float.valueOf(y));
            if (i == 0) {
                path.moveTo(x, y);
            } else if (i == pathStr.size() - 1) {
                path.setLastPoint(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        this.pathPoints = new float[points.size()];
        i = 0;
        while (true) {
            float[] fArr = this.pathPoints;
            if (i >= fArr.length) {
                return path;
            }
            fArr[i] = ((Float) points.get(i)).floatValue();
            i++;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.anchorBitmap != null && this.anchorBitmap.isRecycled()) {
            this.anchorBitmap.recycle();
            this.anchorBitmap = null;
        }
        if (this.letterBitmap != null && this.letterBitmap.isRecycled()) {
            this.letterBitmap.recycle();
            this.letterBitmap = null;
        }
    }

    public Bitmap getBitmapByAssetName(String path) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getContext().getAssets().openFd(path).createInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        } catch (IOException e2) {
            e2.printStackTrace();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
            return bitmap;
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}

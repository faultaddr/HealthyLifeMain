package cn.panyunyi.healthylife.app.main.ui.custome;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.InputStream;

import cn.panyunyi.healthylife.app.main.R;

/**
 * Created by panyunyi on 2018/1/9.
 */
/*
* 属性有：
*
* 1.表盘
* 2.对应步数
* 3.背景图
*
*
* */
public class MainPicView extends View {


    public int getPlateRadius() {
        return plateRadius;
    }

    public void setPlateRadius(int plateRadius) {
        this.plateRadius = plateRadius;
    }

    public int getPlateWidth() {
        return plateWidth;
    }

    public void setPlateWidth(int plateWidth) {
        this.plateWidth = plateWidth;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getBackgroundPic() {
        return backgroundPic;
    }

    public void setBackgroundPic(int backgroundPic) {
        this.backgroundPic = backgroundPic;
    }

    //表盘半径
    private int plateRadius;
    //表盘环状宽度
    private int plateWidth;
    //对应步数
    private int stepCount;
    //背景图片
    private int backgroundPic;

    private Context mContext;

    private Bitmap mBitmap;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;
    private Canvas mCanvas;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MainPicView(Context context,Builder builder,boolean isDefine) {
        this(context, null);
        this.backgroundPic=builder.backgroundPic;
        this.plateRadius=builder.plateRadius;
        this.plateWidth=builder.plateWidth;
        this.stepCount=builder.stepCount;
        setParams();
    }

    private void setParams() {
        InputStream is = getResources().openRawResource(backgroundPic);

        mBitmap = BitmapFactory.decodeStream(is).copy(Bitmap.Config.ARGB_8888, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MainPicView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MainPicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        mContext = context;
        if(attrs!=null) {


            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MainPicView, defStyleAttr, 0);

            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                Log.i(">>>", attr + "");
                //Log.i(">>>",a.getDrawable(attr).getAlpha()+"");
                switch (attr) {
                    case R.styleable.MainPicView_backPicSource:

                        backgroundPic = a.getResourceId(attr, 0);
                        Log.i(">>>backgroundPic", backgroundPic + "");

                        InputStream is = getResources().openRawResource(backgroundPic);

                        mBitmap = BitmapFactory.decodeStream(is).copy(Bitmap.Config.ARGB_8888, true);


                        break;
                    case R.styleable.MainPicView_plateWidth:

                        plateWidth = a.getInteger(attr, 50);
                        break;
                    case R.styleable.MainPicView_plateRadius:
                        plateRadius = a.getInteger(attr, 16);
                        break;

                }

            }
            a.recycle();
        }
            mPaint = new Paint();
            mPaint.setColor(Color.BLUE);
            mPaint.set
            mBound = new Rect();
            mBound.set(100, 100, 400, 400);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(">>>", "开始绘制");
        //canvas = new Canvas(mBitmap);
        Rect mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF mDestRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawBitmap(mBitmap,mSrcRect,mDestRect,mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, plateRadius, mPaint);
/*        mCanvas.
        mCanvas.drawRoundRect(mBound, 10, 10, mPaint);*/

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }


public static class Builder{
    //表盘半径
    private int plateRadius;
    //表盘环状宽度
    private int plateWidth;
    //对应步数
    private int stepCount;
    //背景图片
    private int backgroundPic;

    public Builder plateRadius(int plateRadius){
        this.plateRadius=plateRadius;
        return this;
    }
    public Builder plateWidth(int plateWidth){
        this.plateWidth=plateWidth;
        return this;
    }
    public Builder backgroundPic(int backgroundPic){
        this.backgroundPic=backgroundPic;
        return this;
    }
    public Builder stepCount(int stepCount){
        this.stepCount=stepCount;
        return this;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MainPicView build(Context context){
        return new MainPicView(context,this,true);
    }
}

}

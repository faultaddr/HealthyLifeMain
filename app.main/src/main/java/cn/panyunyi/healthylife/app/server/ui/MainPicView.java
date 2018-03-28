package cn.panyunyi.healthylife.app.server.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.InputStream;

import cn.panyunyi.healthylife.app.server.R;

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


    private static final int MSG_WHAT = 10086;
    private static final int DELAY_TIME = 20;
    //设置默认宽高，雷达一般都是圆形，所以我们下面取宽高会去Math.min(宽,高)
    private final int DEFAULT_WIDTH = 200;
    private final int DEFAULT_HEIGHT = 200;
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
    private Paint mRadarBg;//雷达底色画笔
    /*
    * 颜色变化范围
    *
    * */
    private int startColor;
    private int endColor;
    private int backgroundColor;
    private int lineColor;
    private int circleCount;
    private Matrix matrix;
    private Shader radarShader;  //paintShader
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MainPicView(Context context,Builder builder,boolean isDefine) {
        this(context, null);
        this.backgroundPic=builder.backgroundPic;
        this.plateRadius=builder.plateRadius;
        this.plateWidth=builder.plateWidth;
        this.stepCount=builder.stepCount;
        setParams();
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
/*                    case R.styleable.gradientView_startColor:
                        startColor=a.getColor(attr,startColor);
                        break;
                    case R.styleable.gradientView_endColor:
                        endColor=a.getColor(attr,endColor);
                        break;
                    case R.styleable.gradientView_bgColor:
                        backgroundColor=a.getColor(attr,backgroundPic);
                        break;
                    case R.styleable.gradientView_lineColor:
                        lineColor=a.getColor(attr,lineColor);
                        break;
                    case R.styleable.gradientView_circleCount:
                        circleCount=a.getColor(attr,circleCount);
                        break;*/


                }

            }
            a.recycle();
        }
            mPaint = new Paint();
            mPaint.setColor(Color.BLUE);
            mBound = new Rect();
            mBound.set(100, 100, 400, 400);


    }

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

    private void setParams() {
        InputStream is = getResources().openRawResource(backgroundPic);

        mBitmap = BitmapFactory.decodeStream(is).copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(">>>", "开始绘制");
/*        //canvas = new Canvas(mBitmap);
        Rect mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF mDestRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawBitmap(mBitmap,mSrcRect,mDestRect,mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, plateRadius, mPaint);*/
/*        mCanvas.
        mCanvas.drawRoundRect(mBound, 10, 10, mPaint);*/
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);     //设置抗锯齿
        mPaint.setColor(lineColor);                  //画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);           //设置空心的画笔，只画圆边
        mPaint.setStrokeWidth(2);                      //画笔宽度

        mRadarBg = new Paint(Paint.ANTI_ALIAS_FLAG);     //设置抗锯齿
        mRadarBg.setColor(backgroundColor);                  //画笔颜色
        mRadarBg.setStyle(Paint.Style.FILL);           //设置空心的画笔，只画圆边

        radarShader = new SweepGradient(0, 0, startColor, endColor);
        matrix = new Matrix();
        canvas.translate(100, 100);   //将画板移动到屏幕的中心点

        mRadarBg.setShader(null);
        canvas.drawCircle(0, 0, plateRadius, mRadarBg);  //绘制底色(默认为黑色)，可以使雷达的线看起来更清晰

        for (int i = 1; i <= circleCount; i++) {     //根据用户设定的圆个数进行绘制
            canvas.drawCircle(0, 0, (float) (i * 1.0 / circleCount * plateRadius), mPaint);  //画圆圈
        }

        canvas.drawLine(-plateRadius, 0, plateRadius, 0, mPaint);  //绘制雷达基线 x轴
        canvas.drawLine(0, plateRadius, 0, -plateRadius, mPaint);  //绘制雷达基线 y轴

        //设置颜色渐变从透明到不透明
        mRadarBg.setShader(radarShader);
        canvas.concat(matrix);
        canvas.drawCircle(0, 0, plateRadius, mRadarBg);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureSize(1, DEFAULT_WIDTH, widthMeasureSpec);
        int height = measureSize(0, DEFAULT_HEIGHT, heightMeasureSpec);
        int measureSize = Math.max(width, height);     //取最大的 宽|高
        setMeasuredDimension(measureSize, measureSize);//重新设置布局measure需要调用


    }

    /**
     * 测绘measure
     *
     * @param specType    1为宽， 其他为高
     * @param contentSize 默认值
     */
    private int measureSize(int specType, int contentSize, int measureSpec) {
        int result;
        //获取测量的模式和Size
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = Math.max(contentSize, specSize);
        } else {
            result = contentSize;

            if (specType == 1) {
                // 根据传人方式计算宽
                result += (getPaddingLeft() + getPaddingRight());
            } else {
                // 根据传人方式计算高
                result += (getPaddingTop() + getPaddingBottom());
            }
        }

        return result;

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

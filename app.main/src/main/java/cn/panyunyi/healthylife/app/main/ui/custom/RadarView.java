package cn.panyunyi.healthylife.app.main.ui.custom;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.panyunyi.healthylife.app.main.R;


/**
 * SweepGradient
 */
public class RadarView extends View {
    private static final int MSG_WHAT = 10086;
    private static final int DELAY_TIME = 20;
    private final String TAG = "QDX";
    //设置默认宽高，雷达一般都是圆形，所以我们下面取宽高会去Math.min(宽,高)
    private final int DEFAULT_WIDTH = 200;
    private final int DEFAULT_HEIGHT = 200;

    private int mRadarRadius;  //雷达的半径

    private Paint mRadarPaint;//雷达画笔

    private Paint mRadarBg;//雷达底色画笔

    private int radarCircleCount = 4;//雷达圆圈的个数，默认4个

    private int mRadarLineColor = Color.WHITE; //雷达线条的颜色，默认为白色

    private int mRadarBgColor = Color.BLACK; //雷达圆圈背景色
    private Shader radarShader;  //paintShader

    //雷达扫描时候的起始和终止颜色
    private int startColor = 0x0000ff00;
    private int endColor = 0xaa00ff00;
    //旋转的角度
    private int rotateAngel = 0;
    private Matrix matrix;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            rotateAngel += 3;
            postInvalidate();

            matrix.reset();
            matrix.preRotate(rotateAngel, 0, 0);
            mHandler.sendEmptyMessageDelayed(MSG_WHAT, DELAY_TIME);
        }
    };


    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);


        mRadarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);     //设置抗锯齿
        mRadarPaint.setColor(mRadarLineColor);                  //画笔颜色
        mRadarPaint.setStyle(Paint.Style.STROKE);           //设置空心的画笔，只画圆边
        mRadarPaint.setStrokeWidth(3);                      //画笔宽度


        mRadarBg = new Paint(Paint.ANTI_ALIAS_FLAG);     //设置抗锯齿
        mRadarBg.setColor(mRadarBgColor);                  //画笔颜色
        mRadarBg.setStyle(Paint.Style.FILL);           //设置空心的画笔，只画圆边


        radarShader = new SweepGradient(0, 0, startColor, endColor);

        matrix = new Matrix();
    }

    //初始化，拓展可设置参数供布局使用
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
            startColor = ta.getColor(R.styleable.RadarView_startColor, startColor);
            endColor = ta.getColor(R.styleable.RadarView_endColor, endColor);
            mRadarBgColor = ta.getColor(R.styleable.RadarView_bgColor, mRadarBgColor);
            mRadarLineColor = ta.getColor(R.styleable.RadarView_lineColor, mRadarLineColor);
            radarCircleCount = ta.getInteger(R.styleable.RadarView_circleCount, radarCircleCount);
            ta.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureSize(1, DEFAULT_WIDTH, widthMeasureSpec);
        int height = measureSize(0, DEFAULT_HEIGHT, heightMeasureSpec);
        int measureSize = Math.max(width, height);   //取最大的 宽|高
        setMeasuredDimension(measureSize, measureSize);
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadarRadius = Math.min(w / 2, h / 2);
        mRadarRadius = mRadarRadius - 40;
        Log.i(TAG, "onSizeChanged");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i(TAG, "onDraw   " + rotateAngel);

        canvas.translate(mRadarRadius, mRadarRadius);   //将画板移动到屏幕的中心点

        mRadarBg.setShader(null);
        mRadarPaint.setShader(radarShader);
        // canvas.drawCircle(0, 0, mRadarRadius, mRadarBg);  //绘制底色(默认为黑色)，可以使雷达的线看起来更清晰
        canvas.concat(matrix);
        for (int i = 1; i <= radarCircleCount; i++) {     //根据用户设定的圆个数进行绘制
            for (int j = 0; j < 10; j++) {
                canvas.drawCircle(0, 0, (float) (mRadarRadius - 20 - j * 2), mRadarPaint);
                canvas.drawCircle(j, j * 2, (float) (mRadarRadius - 20), mRadarPaint);
            }
            //canvas.drawColor(0, PorterDuff.Mode.CLEAR);//画圆圈
        }

/*        canvas.drawLine(-mRadarRadius, 0, mRadarRadius, 0, mRadarPaint);  //绘制雷达基线 x轴
        canvas.drawLine(0, mRadarRadius, 0, -mRadarRadius, mRadarPaint);  //绘制雷达基线 y轴*/


        // canvas.rotate(rotateAngel,0,0);
        //设置颜色渐变从透明到不透明
        //mRadarBg.setShader(radarShader);

        //canvas.drawCircle(0, 0, mRadarRadius, mRadarBg);
    }

    public void startScan() {
        mHandler.removeMessages(MSG_WHAT);
        mHandler.sendEmptyMessage(MSG_WHAT);
    }

    public void stopScan() {
        mHandler.removeMessages(MSG_WHAT);
    }

}
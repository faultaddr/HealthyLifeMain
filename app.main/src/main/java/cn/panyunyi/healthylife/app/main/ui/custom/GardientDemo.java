package cn.panyunyi.healthylife.app.main.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.view.View;

public class GardientDemo extends View {

    private Rect mRect;
    //线性渐变
    private LinearGradient mLinearGradientRepeat;
    private LinearGradient mLinearGradientClamp;
    private LinearGradient mLinearGradientMirror;

    //扫描渐变(类似雷达扫描)
    private SweepGradient mGradientSweep;

    //环形渐变
    private RadialGradient mRadialGradientClamp;
    private RadialGradient mRadialGradientMirror;
    private RadialGradient mRadialGradientRepeat;

    //混合渲染
    private ComposeShader mComposeShader;

    private Paint mPaint;

    /**
     * @param context
     */
    public GardientDemo(Context context) {
        super(context);

        mLinearGradientClamp = new LinearGradient(0, 0, 0, 50, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.CLAMP);
        mLinearGradientMirror = new LinearGradient(0, 0, 0, 50, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.MIRROR);
        mLinearGradientRepeat = new LinearGradient(0, 0, 0, 50, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.REPEAT);

        mGradientSweep = new SweepGradient(700, 600, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null);

        mRadialGradientClamp = new RadialGradient(100, 600, 50, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.CLAMP);
        mRadialGradientMirror = new RadialGradient(300, 600, 50, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.MIRROR);
        mRadialGradientRepeat = new RadialGradient(500, 600, 50, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.REPEAT);

        mComposeShader = new ComposeShader(mLinearGradientMirror, mRadialGradientMirror, PorterDuff.Mode.SRC);
        mRect = new Rect();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        // LinearGradient的高度只有100而绘制的矩形有200所以才会有重复
        // 如果高度两者相同clamp重复是看不出效果的
        //clamp
        mRect.set(0, 0, getWidth(), 100);
        mPaint.setShader(mLinearGradientClamp);
        canvas.drawRect(mRect, mPaint);

        //mirror
        mRect.set(0, 150, getWidth(), 250);
        mPaint.setShader(mLinearGradientMirror);
        canvas.drawRect(mRect, mPaint);

        //repeat
        mRect.set(0, 300, getWidth(), 400);
        mPaint.setShader(mLinearGradientRepeat);
        canvas.drawRect(mRect, mPaint);

        //mGradientRadial
        mPaint.setShader(mRadialGradientClamp);
        canvas.drawCircle(100, 600, 100, mPaint);

        mPaint.setShader(mRadialGradientMirror);
        canvas.drawCircle(300, 600, 100, mPaint);

        mPaint.setShader(mRadialGradientRepeat);
        canvas.drawCircle(500, 600, 100, mPaint);

        //mGradientSweep
        mPaint.setShader(mGradientSweep);
        canvas.drawCircle(700, 600, 100, mPaint);
    }
}
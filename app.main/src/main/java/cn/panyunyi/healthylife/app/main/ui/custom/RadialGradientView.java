package cn.panyunyi.healthylife.app.server.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class RadialGradientView extends View {

    private Paint mPaint = null;
    private float radius = 480;
    private RadialGradient mRadialGradient = null;

    public RadialGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRadialGradient = new RadialGradient(this.getWidth() / 2, this.getHeight() / 2, radius, new int[]{Color.RED, Color.TRANSPARENT, Color.BLACK}, null, Shader.TileMode.CLAMP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setShader(mRadialGradient);

        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, radius, mPaint);
    }
}  
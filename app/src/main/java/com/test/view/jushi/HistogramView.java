package com.test.view.jushi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状图
 *
 * @author wyf
 */
public class HistogramView extends View {
    private int width;
    private int height;
    private Paint xyPaint; //画x y轴线的paint
    private int offset = 100;//偏移量
    private int startX = offset;
    private Paint linePaint;//画虚线
    private Paint textPaint;//画文字
    private int scaleY;
    private int scaleX;
    private int defaultColor = Color.parseColor("#6ea1ff");//默认颜色
    private int markingColor = Color.parseColor("#ffb22b");//标线的颜色（8位置的颜色）
    private List<Float> datas = new ArrayList<>();

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        xyPaint = new Paint();
        xyPaint.setColor(defaultColor);
        xyPaint.setAntiAlias(true);
        xyPaint.setStrokeWidth(6);

        linePaint = new Paint();
        linePaint.setPathEffect(new DashPathEffect(new float[]{10f, 6f}, 0f));//设置虚线
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(4);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(defaultColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        scaleY = (height / 6) - 2;
        scaleX = (width - offset) / 7;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(-20, 20);
        drawImaginaryLineX(canvas);
        drawXY(canvas);
        drawLeftText(canvas);
        drawBottomTextAndPillar(canvas);
    }

    /**
     * 画x轴的文字（星期一 到 星期日）以及柱子
     *
     * @param canvas
     */
    private void drawBottomTextAndPillar(Canvas canvas) {
        String[] texts = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日",};
        textPaint.setTextSize(35);
        float textWidth = textPaint.measureText(texts[0]);//测量文字的宽度
        float scale = textWidth / 3;
        int sx = scaleX % 10 + startX - 10;
        int sy = height - 40;
        Rect rect = new Rect();
        int baseTop = height - offset;
        int bottom = height - offset;
        for (int i = 0; i < texts.length; i++) {
            if (i == 0) { //画星期一的文字以及柱子
                canvas.drawText(texts[i], sx, sy, textPaint); //画文字
                if (datas != null && datas.size() > 0) //设置柱子的宽高
                    rect.set(startX + 20, (int) (baseTop * (1 - datas.get(i))), startX + 50, bottom);
            } else { //画星期二以后的文字及柱子
                canvas.drawText(texts[i], sx + textWidth * i + scale * i, sy, textPaint);//画文字
                if (datas != null && datas.size() > 0)//设置柱子的宽高
                    rect.set((int) (sx + textWidth * i + scale * i) + 20, (int) (baseTop * (1 - datas.get(i))), (int) (sx + textWidth * i + scale * i) + 50, bottom);
            }
            canvas.drawRect(rect, xyPaint);
        }
    }

    /**
     * 画左边的文字 （0，2，4，6，8，10）
     *
     * @param canvas
     */
    private void drawLeftText(Canvas canvas) {
        textPaint.setTextSize(45);
        int num = 0;
        int x = startX - 50;
        int y = 0;
        for (int i = 0; i < 6; i++) {
            y = (height - (i + 1) * scaleY) + 20;
            if (i == 0) { //画 0 的时候将y坐标上移80
                y = height - (offset - 20);
            }
            if (i == 5) { //画 10的时候将x坐标再左移20，避免0显示再y轴上
                textPaint.setColor(defaultColor);
                x -= 20;
                y = (height - (i + 1) * scaleY) + 30;
            }
            if (num == 8) {
                textPaint.setColor(markingColor);
            }
            canvas.drawText(String.valueOf(num), x, y, textPaint);
            num += 2;
        }
    }

    /**
     * 画横着的虚线
     * 从y轴坐标值 2 的位置开始画
     *
     * @param canvas
     */
    private void drawImaginaryLineX(Canvas canvas) {
        Path path = new Path();
        Path markingPath = new Path();
        for (int i = 2; i <= 6; i++) {
            //当前点
            path.moveTo(startX, height - i * scaleY);
            //结束点 （但在绘制path时是从结束点绘制到当前点）
            path.lineTo(width, height - i * scaleY);
            if (i == 6) { //最后一根虚线不画到最顶端，将y轴留出一点
                path.reset();
                path.moveTo(startX, (height - i * scaleY) + 1);
                path.lineTo(width, (height - i * scaleY) + 1);
            }
            if (i == 5) { //画8位置的虚线
                markingPath.moveTo(startX, height - i * scaleY);
                markingPath.lineTo(width, height - i * scaleY);
                linePaint.setColor(markingColor);
                canvas.drawPath(markingPath, linePaint);
                continue;
            }
            linePaint.setColor(defaultColor);
            canvas.drawPath(path, linePaint);
        }
    }

    /**
     * 画x y轴
     *
     * @param canvas
     */
    private void drawXY(Canvas canvas) {
        //画y轴
        canvas.drawLine(startX, 0, startX, height - (offset - 3), xyPaint);
        //画x轴
        canvas.drawLine(startX, height - offset, width, height - offset, xyPaint);
    }

    /**
     * 设置百分比数据
     *
     * @param datas 存有float类型数据的集合
     */
    public void setPercentage(List<Float> datas) {
        this.datas.clear();
        invalidate();
        this.datas.addAll(datas);
        invalidate();
    }

}

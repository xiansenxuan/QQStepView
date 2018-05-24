package com.xuan.qqstepview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * com.xuan.qqstepview
 *
 * @author by xuan on 2018/5/14
 * @version [版本号, 2018/5/14]
 * @update by xuan on 2018/5/14
 * @descript
 */
public class QQStepView extends View {
    private int outColor= ContextCompat.getColor(getContext(),R.color.colorAccent);
    private int inColor=ContextCompat.getColor(getContext(),R.color.colorPrimary);
    private int fontColor=ContextCompat.getColor(getContext(),R.color.colorAccent);

    private int borderWidth=5;
    private int fontSize=15;

    //当前和最大值
    private int stepNumber;
    private int stepNumberMax=20000;

    private Paint outPaint,inPaint,textPaint;

    public QQStepView(Context context) {
        this(context,null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context,attrs);
        initPaint();
    }

    /**
     * 设置画笔
     */
    @SuppressLint("ResourceAsColor")
    private void initPaint() {
        outPaint = new Paint();
        outPaint.setAntiAlias(true);
        outPaint.setStrokeWidth(borderWidth);
        outPaint.setColor(outColor);
        outPaint.setStyle(Paint.Style.STROKE);
        outPaint.setStrokeCap( Paint.Cap.ROUND);

        inPaint = new Paint();
        inPaint.setAntiAlias(true);
        inPaint.setStrokeWidth(borderWidth);
        inPaint.setColor(inColor);
        inPaint.setStyle(Paint.Style.STROKE);
        inPaint.setStrokeCap( Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(fontColor);
        textPaint.setTextSize(fontSize);
    }

    /**
     * 1 初始化自定义属性
     * @param context
     * @param attrs
     */
    @SuppressLint("ResourceAsColor")
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);

        outColor=typedArray.getColor(R.styleable.QQStepView_outColor,outColor);
        inColor=typedArray.getColor(R.styleable.QQStepView_inColor,inColor);
        fontColor=typedArray.getColor(R.styleable.QQStepView_fontColor,fontColor);

        borderWidth=typedArray.getDimensionPixelSize(R.styleable.QQStepView_borderWidth,borderWidth);
        fontSize=typedArray.getDimensionPixelSize(R.styleable.QQStepView_fontSize,fontSize);

        stepNumber=typedArray.getInteger(R.styleable.QQStepView_stepNumber,stepNumber);
        stepNumberMax=typedArray.getInteger(R.styleable.QQStepView_stepNumberMax,stepNumberMax);

        typedArray.recycle();
    }

    /**
     * 2 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //获取宽高
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //计算 MeasureSpec.AT_MOST 布局中指定了wrap_content的宽高值
        //不允许自适应宽高 默认给40
        int measuredWidth= widthMode==MeasureSpec.AT_MOST==true? 40:width;
        int measuredHeight= heightMode==MeasureSpec.AT_MOST==true? 40:height;
        //取正方形 长宽取最小值
        if(measuredHeight>measuredWidth){
            measuredHeight=measuredWidth;
        }
        if(measuredHeight<measuredWidth){
            measuredWidth=measuredHeight;
        }
        setMeasuredDimension(measuredWidth,measuredHeight);

    }

    /**
     * 3 画弧
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画第一个圆弧
        //     public void drawArc(@NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter,
        //            @NonNull Paint paint) {
        //     startAngle开始的角度 sweepAngle-弧线顺时针旋转的角度 useCenter 如果为true，绘制的起点和终点会和圆心相连
        //     public RectF(float left, float top, float right, float bottom)
        //     矩形的宽width = right - left ，高height = bottom - top
        RectF outOval=new RectF(borderWidth/2,borderWidth/2,getWidth()-borderWidth/2,getHeight()-borderWidth/2);
        canvas.drawArc(outOval,135,270,false,outPaint);

        if(stepNumber==0 || stepNumberMax==0) return;
        //画覆盖的第二个圆弧
        RectF inOval=new RectF(borderWidth/2,borderWidth/2,getWidth()-borderWidth/2,getHeight()-borderWidth/2);
        float sweepAngle= ( (float) stepNumber/stepNumberMax ) * 270;
        canvas.drawArc(inOval,135,sweepAngle,false,inPaint);

        //画文字
        String text=stepNumber+"";
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        //计算宽度 字体的长度有关
        Rect bounds=new Rect();//矩形
        //给矩形设置边界
        textPaint.getTextBounds(text,0,text.length(),bounds);

        int dx=getWidth()/2-bounds.width()/2;
        int dy= (int) ((fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
        int baseLine=getHeight()/2+dy;
        canvas.drawText(text,getPaddingLeft()+dx,baseLine,textPaint);

    }

    public int getStepNumber(){
        return stepNumber;
    }
    /**
     * 不断修改当前步数 初始化 使其转起来
     * @param stepNumber
     */
    public synchronized void startInvalidate(int stepNumber){
        this.stepNumber=stepNumber;
        //这个方法会不断调用 onDraw
        //流程:由parent一层一层调用 从最外层的draw()开始一步一步 - dispatchDraw() - 一直到invalidate()
        invalidate();

        //为什么不能在子线程更新view
        /*
        一般调用view的setXxx方法或者其他
        都会调到
        void checkThread() {
            if (mThread != Thread.currentThread()) {
                throw new CalledFromWrongThreadException(
                        "Only the original thread that created a view hierarchy can touch its views.");
            }
            检测当前线程如果不在主线程 就抛出异常
        }*/

        /*
            微信朋友圈过度渲染优化
            开发者选项-调制gpu过度绘制

            一般调用view的setXxx方法或者其他
            都会调用 onInvalidate() 导致的过度渲染
            最好的办法是 不用系统布局嵌套 完全自定义



        */
    }

}

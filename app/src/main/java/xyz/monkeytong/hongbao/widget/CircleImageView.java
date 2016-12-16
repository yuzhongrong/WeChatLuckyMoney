package xyz.monkeytong.hongbao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import xyz.monkeytong.hongbao.R;

/**
 * Created by Administrator on 2016/2/21 0021.
 */
public class CircleImageView extends ImageView {


    private static final String TAG = "CircleImageView";
    // 设置外圈的宽度
    private int outCircleWidth;
    // 设置外圈的颜色
    private int outCircleColor ;

    private int viewWidth;
    private int viewHeight;

    private Bitmap image;
    private Paint paintBorder;//背景画笔
    private Shader sweepGradient;
    private boolean Flag_Selector=false;


    public CircleImageView(Context context) {
        super(context);
        setup(context,null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context,attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }


    private void setup(Context context,AttributeSet attrs) {
        //2.获取属性
        if (null != attrs){
            TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.CircleImageView);

            int len = array.getIndexCount();
            for (int i = 0; i < len; i++) {
               int attr = array.getIndex(i);

                switch (attr){
                    case R.styleable.CircleImageView_outCircleColor:
                        this.outCircleColor = array.getColor(R.styleable.CircleImageView_outCircleColor,Color.WHITE);
                        break;
                    case R.styleable.CircleImageView_outCircleWigth:
                        this.outCircleWidth = (int)array.getDimension(R.styleable.CircleImageView_outCircleWigth,5);
                        break;
                    default:

                        break;
                }
            }
        }

        //初始化背景画笔
        paintBorder = new Paint();

        //设置背景颜色
        paintBorder.setColor(outCircleColor);
        //抗锯齿
        paintBorder.setAntiAlias(true);
    }
    //3.测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        viewWidth = width - (outCircleWidth * 2);
        viewHeight = height -(outCircleWidth * 2);

        setMeasuredDimension(width,height);

    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY){
            result = size;
        }else {
            result = viewHeight;
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        if (mode == MeasureSpec.EXACTLY){
            result = size;
        }else {
            result = viewWidth;
        }


        return result;
    }

    // 设置底图颜色
    public void setBorderColor(int borderColor) {
        if (paintBorder != null)
            paintBorder.setColor(borderColor);

        this.invalidate();
    }

    // 设置外圈的宽度
    public void setBorderWidth(int outCircleWidth) {
        this.outCircleWidth = outCircleWidth;

        this.invalidate();
    }


    // 设置外圈的宽度
    public void setSrcImage(Bitmap image) {
        this.outCircleWidth = outCircleWidth;
        this.image=image;

        this.invalidate();
    }

//4.测量

    @Override
    protected void onDraw(Canvas canvas) {

        Log.d(TAG,"----onDraw()-------->");

        //加载图片
        loadBitmap();

        if (image != null){
            int min = Math.min(viewWidth,viewHeight);

            int circleCenter = min/2;

            image = Bitmap.createScaledBitmap(image,min,min,false);

            sweepGradient=new SweepGradient(circleCenter + outCircleWidth,circleCenter + outCircleWidth,new int[]{Color.parseColor("#D7D7D7"),Color.parseColor("#E7E7E7"),Color.parseColor("#D7D7D7"),Color.GRAY,Color.parseColor("#D7D7D7")},null);
            paintBorder.setShader(sweepGradient);
            canvas.drawCircle(circleCenter + outCircleWidth,circleCenter + outCircleWidth,circleCenter + outCircleWidth,paintBorder);

            canvas.drawBitmap(createCircleImage(image, min), outCircleWidth, outCircleWidth, null);
        }
    }

    private Bitmap createCircleImage(Bitmap source, int min) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);

        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN，参考上面的说明
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;


    }

    private void loadBitmap() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if(Flag_Selector==false){
            if (bitmapDrawable != null){
                image = bitmapDrawable.getBitmap();
                Log.d(TAG,"----loadBitmap-------->");
            }

        }
        else{
            this.image= BitmapFactory.decodeResource(getResources(),R.drawable.start3);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            Log.d(TAG,"----ACTION_DOWN-------->");
            Flag_Selector=true;
             this.invalidate();
        }else if(event.getAction()==MotionEvent.ACTION_UP){
            Log.d(TAG,"----ACTION_UP-------->");
            Flag_Selector=false;
            this.invalidate();
        }


        return super.onTouchEvent(event);
    }
}

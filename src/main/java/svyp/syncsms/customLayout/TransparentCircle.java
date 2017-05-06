package svyp.syncsms.customLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class TransparentCircle extends LinearLayout {

    Canvas cv;
    Paint eraser;
    String color;
    int width, height;

    public TransparentCircle(Context context) {
        super(context);
        Init();
    }

    public TransparentCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.color = attrs.getAttributeValue(attrs.getAttributeCount()-1);
        Init();
    }

    public TransparentCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    private void Init(){
        eraser = new Paint();
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraser.setAntiAlias(true);
        
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
        	width = w;
        	height = h;
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        int radius = w > h ? h / 2 : w / 2;
        Paint paint = new Paint();
        radius -= 3;
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        cv = new Canvas(bm);
        bm.eraseColor(Color.TRANSPARENT);
        color="#ffffff";
        cv.drawColor(Color.parseColor(color));
        cv.drawCircle(w / 2, h / 2, radius, eraser);
        paint.setColor(Color.parseColor("#ffffff"));
        paint.setAlpha(30);   
        cv.drawCircle(w / 2, h / 2, radius, paint);
        canvas.drawBitmap(bm, 0, 0, null);
        paint = null;
        if (bm!=null) {
	    	bm.recycle();
	    	bm = null;
    	}
        super.onDraw(canvas);
    }
    
    public void recycleTransparentCircle(){
    	if (cv!=null)
    		cv = null;
    }
}
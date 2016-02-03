package vikicc.custom.edittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 自定义edittext带自定义头部（可用代码控制）
 *
 * Created by LiuWeiJie on 2015/8/2 0002.
 * Email:1031066280@qq.com
 */
public class EditTextWithTopTitle extends EditText {
    private String title;
    public EditTextWithTopTitle(Context context) {
        super(context);
    }

    public EditTextWithTopTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithTopTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setTitleText(String val){
        title=val;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(24);
        paint.setColor(Color.GRAY);
        if(title!=null){
            canvas.drawText(title, 10, getHeight() / 2+8, paint);
        }
        super.onDraw(canvas);
    }
}

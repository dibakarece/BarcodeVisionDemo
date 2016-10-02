package com.barcodescangvision;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomRequestPreview extends View {
    private Paint paint = new Paint();
    private int paddingLR = 0;
    private int paddingTB = 0;
    private int middleH = 0;
    private int middleW = 0;
    private float ratioLR = 0.15f;
    private float ratioTB = 0.3f;

    private boolean isdownward = false;
    private int scanLine = 0;
    private int deductaddValue = 3;

    public CustomRequestPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        middleH = getHeight() / 2;
        middleW = getWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint = new Paint();
        int width = getWidth();
        int height = getHeight();
        middleW = getWidth() / 2;
        middleH = getHeight() / 2;

        if (height > width) {
            paddingLR = (int) (width * ratioLR);
            paddingTB = (int) (height * ratioTB);
        } else {
            paddingLR = (int) (height * ratioTB);
            paddingTB = (int) (width * ratioLR);
        }

        paint.setColor(Color.parseColor("#60000000"));
        canvas.drawRect(paddingLR, 0, width - paddingLR, paddingTB, paint);//Left
        canvas.drawRect(paddingLR, height - paddingTB, width - paddingLR, height, paint);//Right
        canvas.drawRect(0, 0, paddingLR, height, paint);//Top
        canvas.drawRect(width - paddingLR, 0, width, height, paint);//Bottom

        paint.setColor(Color.parseColor("#ff2196f3"));

        int hlfW = paddingLR / 2;
        int hlfH = paddingTB / 5;
        canvas.drawRect(middleW - 1, paddingTB - hlfH, middleW + 2, paddingTB + hlfH, paint);
        canvas.drawRect(middleW - 1, (height - paddingTB) - hlfH, middleW + 2, (height - paddingTB) + hlfH, paint);
        canvas.drawRect(paddingLR - hlfW, middleH - 1, paddingLR + hlfW, middleH + 2, paint);//Left
        canvas.drawRect((width - paddingLR) - hlfW, middleH - 1, (width - paddingLR) + hlfW, middleH + 2, paint);//Right

        if (scanLine != 0 && isdownward) {
            canvas.drawRect(paddingLR, scanLine - 1, width - paddingLR, scanLine + 2, paint);
            scanLine = scanLine + deductaddValue;
            if (scanLine >= (height - paddingTB)) {
                isdownward = false;
            }
        } else if (scanLine != 0 && !isdownward) {
            canvas.drawRect(paddingLR, scanLine - 1, width - paddingLR, scanLine + 2, paint);
            scanLine = scanLine - deductaddValue;
            if (scanLine <= paddingTB) {
                isdownward = true;
            }
        } else {
            if (scanLine == 0) {
                if (height > width) {
                    scanLine = paddingTB;
                } else {
                    scanLine = paddingLR;
                }
                canvas.drawRect(paddingLR, scanLine - 1, width - paddingLR, scanLine + 2, paint);
                scanLine++;
            }
            isdownward = true;
        }
        invalidate();
    }
}

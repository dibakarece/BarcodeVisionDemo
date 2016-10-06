package com.barcodescangvision;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomRequestPreviewT extends View {
    private Paint paint = new Paint();
    public int paddingLR = 0;
    public int paddingTB = 0;
    private int middleH = 0;
    private int middleW = 0;
    private float ratioLR = 0.15f;
    private float ratioTB = 0.3f;

    private boolean isdownward = false;
    private int scanLine = 0;
    private int deductaddValue = 3;

    public boolean isStartScanning() {
        return startScanning;
    }

    public void setStartScanning(boolean startScanning) {
        this.startScanning = startScanning;
        invalidate();
    }

    private boolean startScanning = false;

    public CustomRequestPreviewT(Context context) {
        super(context);
        paint = new Paint();
        middleH = getHeight() / 2;
        middleW = getWidth() / 2;
    }


    public CustomRequestPreviewT(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        middleH = getHeight() / 2;
        middleW = getWidth() / 2;
    }

    public int getWD() {
        return getWidth();
    }

    public int getHT() {
        return getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint = new Paint();
        int width = getWidth();
        int height = getHeight();
        middleW = getWidth() / 2;
        middleH = getHeight() / 2;
        int hlfW = 0;
        if (height > width) {
            paddingLR = (int) (width * ratioLR);
            paddingTB = (int) (height * ratioTB);
            hlfW = paddingLR / 2;
        } else {
            paddingLR = (int) (height * ratioTB);
            paddingTB = (int) (width * ratioLR);
            hlfW = paddingLR / 4;
        }

        //set light alpha except scan portion
        paint.setColor(Color.parseColor("#60000000"));
        canvas.drawRect(paddingLR, 0, width - paddingLR, paddingTB, paint);//Left
        canvas.drawRect(paddingLR, height - paddingTB, width - paddingLR, height, paint);//Right
        canvas.drawRect(0, 0, paddingLR, height, paint);//Top
        canvas.drawRect(width - paddingLR, 0, width, height, paint);//Bottom

        //Four sticks
        paint.setColor(Color.parseColor("#ff8c9eff"));
        canvas.drawRect(middleW - 1, paddingTB - hlfW, middleW + 2, paddingTB + hlfW, paint);
        canvas.drawRect(middleW - 1, (height - paddingTB) - hlfW, middleW + 2, (height - paddingTB) + hlfW, paint);
        canvas.drawRect(paddingLR - hlfW, middleH - 1, paddingLR + hlfW, middleH + 2, paint);//Left
        canvas.drawRect((width - paddingLR) - hlfW, middleH - 1, (width - paddingLR) + hlfW, middleH + 2, paint);//Right

        // middle Rect for focusing
        canvas.drawRect(paddingLR - 1, paddingTB, paddingLR + 2, height - paddingTB, paint);
        canvas.drawRect((width - paddingLR) - 1, paddingTB, (width - paddingLR) + 2, height - paddingTB, paint);
        canvas.drawRect(paddingLR, paddingTB - 1, width - paddingLR, paddingTB + 2, paint);
        canvas.drawRect(paddingLR, (height - paddingTB) - 1, width - paddingLR, (height - paddingTB) + 2, paint);

        //Scan line
        if (startScanning) {
            paint.setColor(Color.parseColor("#ff2196f3"));
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
}

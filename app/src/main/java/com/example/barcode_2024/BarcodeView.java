package com.example.barcode_2024;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BarcodeView extends View {

    //UPC-A code

    //http://en.wikipedia.org/wiki/EAN_code
    //http://www.terryburton.co.uk/barcodewriter/generator/


    static final int[] L = {
            0x0D,  //000 1101
            0x19,  //001 1001
            0x13,  //001 0011
            0x3D,  //011 1101
            0x23,  //010 0011
            0x31,  //011 0001
            0x2F,  //010 1111
            0x3B,  //011 1011
            0x37,  //011 0111
            0x0B   //000 1011
    };

    static final int[] R = {
            0x72, //111 0010
            0x66, //110 0110
            0x6C, //110 1100
            0x42, //100 0010
            0x5C, //101 1100
            0x5E, //100 1110
            0x50, //101 0000
            0x44, //100 0100
            0x48, //100 1000
            0x74  //111 0100
    };

    final static int BARCODE_WIDTH =  600;
    final static int BARCODE_HEIGHT = 200;
    final static int BARCODE_LINE_WIDTH = 5;

    // čísla čárového kódu
    int code[] = new int[12];

    public BarcodeView(Context context) {
        super(context);
        setDefaults();
    }

    public BarcodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setDefaults();
    }

    public BarcodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDefaults();
    }

    public BarcodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setDefaults();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // při změně velikosti view,  w a h obsahují novou velikost
    }

    // nastaví výchozí hodnoty
    void setDefaults() {
        int copyFrom[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2};
        System.arraycopy(copyFrom, 0, code, 0, copyFrom.length);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);

        Paint blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);

        Paint redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(BARCODE_LINE_WIDTH);


        canvas.drawRect(new Rect(0, 0, BARCODE_WIDTH, BARCODE_HEIGHT), whitePaint);
        //hrubka ciary
        blackPaint.setStrokeWidth(BARCODE_LINE_WIDTH);
        int xPosition = 20;

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        textPaint.setAntiAlias(true);
        canvas.drawLine(xPosition,0,xPosition,BARCODE_HEIGHT,redPaint);
        xPosition=xPosition+2*(BARCODE_LINE_WIDTH);
        canvas.drawLine(xPosition,0,xPosition,BARCODE_HEIGHT,redPaint);
        xPosition=xPosition+1*(BARCODE_LINE_WIDTH);

        for (int i = 0; i < 6; i++) {
            int value = code[i];
            drawBarcodeDigit(canvas, xPosition, value, L, blackPaint);


            float textXPos = xPosition + (3 * BARCODE_LINE_WIDTH);
            canvas.drawText(String.valueOf(value), textXPos, BARCODE_HEIGHT, textPaint);

            xPosition =xPosition+( 7 * BARCODE_LINE_WIDTH);
        }

        canvas.drawLine(xPosition,0,xPosition,BARCODE_HEIGHT,whitePaint);
        xPosition=xPosition+(1*BARCODE_LINE_WIDTH);
        // delimiter
        canvas.drawLine(xPosition, 0, xPosition, BARCODE_HEIGHT, redPaint);
        xPosition =xPosition+( 2 * BARCODE_LINE_WIDTH);

        canvas.drawLine(xPosition, 0, xPosition, BARCODE_HEIGHT, redPaint);
        xPosition =xPosition+( 2 * BARCODE_LINE_WIDTH);



        for (int i = 6; i < 12; i++) {
            int value = code[i];
            drawBarcodeDigit(canvas, xPosition, value, R, blackPaint);


            float textXPos = xPosition + (3 * BARCODE_LINE_WIDTH);
            canvas.drawText(String.valueOf(value), textXPos, BARCODE_HEIGHT , textPaint);

            xPosition = xPosition+(7 * BARCODE_LINE_WIDTH);
        }
        canvas.drawLine(xPosition,0,xPosition,BARCODE_HEIGHT,redPaint);
        xPosition=xPosition+(2*BARCODE_LINE_WIDTH);
        canvas.drawLine(xPosition,0,xPosition,BARCODE_HEIGHT,redPaint);
        xPosition=xPosition+(1*BARCODE_LINE_WIDTH);


    }

    private void drawBarcodeDigit(Canvas canvas, int xPosition, int value, int[] encodingArray, Paint paint) {
        int pattern = encodingArray[value];
        for (int i = 6; i >= 0; i--) {
            int bit = (pattern >> i) & 1;
            if (bit == 1) {
                canvas.drawLine(xPosition, 0, xPosition, BARCODE_HEIGHT-30, paint);
            }
            xPosition =xPosition +BARCODE_LINE_WIDTH;
        }
    }

    public void setCode(int [] newDefaults){
        System.arraycopy(newDefaults,0,code,0,newDefaults.length);
    }

    public int calculateCheckDigit() {
        int sumaOdd = 0;
        int sumaEven = 0;

        for (int i = 0; i < 11; i++) {
            if (i % 2 == 0) {
                sumaOdd += code[i];
            } else {
                sumaEven += code[i];
            }
        }
        sumaOdd *= 3;
        int totalSuma = sumaOdd + sumaEven;
        int checkDigit = (totalSuma % 10 == 0) ? 0 : 10 - (totalSuma % 10);
        return checkDigit;
    }



}


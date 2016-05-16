package com.coollime.coollime;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class memoryActivity3 extends AppCompatActivity{
    GameView a;
    public void onCreate(Bundle SaveInstanceState){
        super.onCreate(SaveInstanceState);
        a=new GameView(this);
        setContentView(a);

    }

}
class GameView extends View {
    final static int Blank = 0;
    final static int Play = 1;
    final static int SIZE = 64;

    static Boolean randomShape = true;
    static Boolean randomSize = true;
    static Boolean randomColor = true;

    final static int DELAY = 1000;


    memoryActivity3 parent;
    int status;
    ArrayList<Shape> arShape = new ArrayList<Shape>();
    Random R = new Random();

    public GameView(Context context) {
        super(context);
        parent = (memoryActivity3) context;

        status = Blank;
        cHandler.sendEmptyMessageDelayed(0,DELAY);

    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (status == Blank) {
            return;
        }
        int cool;
        for (cool = 0; cool < arShape.size(); cool++) {
            Paint p = new Paint();
            p.setColor(arShape.get(cool).color);
            Rect rt = arShape.get(cool).rt;

            switch (arShape.get(cool).what) {
                case Shape.RECT:
                    canvas.drawRect(rt, p);
                    break;
                case Shape.CIRCLE:
                    canvas.drawCircle(rt.left + rt.width()/2, rt.top + rt.height()/2,
                            rt.width()/2, p);
                    break;
                case Shape.TRA:
                    Path path = new Path();
                    path.moveTo(rt.left + rt.width() / 2, rt.top + rt.height() / 2);
                    path.lineTo(rt.left, rt.bottom);
                    path.lineTo(rt.right, rt.bottom);
                    canvas.drawPath(path, p);
                    break;


            }


        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int flo;
            flo = FindShapeIdx((int) event.getX(), (int) event.getY());
            if (flo == -1) {
                return true;
            }
            if (flo == arShape.size() - 1) {
                status = Blank;
                invalidate();
                cHandler.sendEmptyMessageDelayed(0, DELAY);
            } else {
                new AlertDialog.Builder(getContext()).setMessage("다시하기")
                        .setPositiveButton("다시하기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int witchButton) {
                                arShape.clear();
                                status = Blank;
                                invalidate();
                                cHandler.sendEmptyMessageDelayed(0, DELAY);
                                invalidate();

                            }
                        })
                        .setNegativeButton("그만하기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int witchButton) {
                                parent.finish();
                            }
                        })
                        .show();


            }
            return true;
        }
        return false;
    }

    void AddNewShape() {
        Shape shape = new Shape();
        int cool;
        boolean bFindInterSect;
        Rect rect = new Rect();
        for (; ; ) {
            rect.left = R.nextInt(getWidth() - SIZE);
            rect.top = R.nextInt(getHeight() - SIZE);

            int Size =SIZE;
            if (randomSize) {
                Size += 16 * R.nextInt(2);
            }
            rect.right = rect.left + Size;
            rect.bottom = rect.top + Size;

            bFindInterSect = false;
            for (cool = 0; cool < arShape.size(); cool++) {
                if (rect.intersect(arShape.get(cool).rt))
                {
                    bFindInterSect = true;
                }

            }
            if (!bFindInterSect) {
                break;
            }
        }
        if (randomShape) {
            shape.what = R.nextInt(3);
        } else {
            shape.what = Shape.CIRCLE;
        }

        if (randomColor) {
            switch (R.nextInt(6)) {
                case 0:
                    shape.color = Color.BLACK;
                    break;
                case 1:
                    shape.color = Color.RED;
                    break;
                case 2:
                    shape.color = Color.BLUE;
                    break;
                case 3:
                    shape.color = Color.GREEN;
                    break;
                case 4:
                    shape.color = Color.GRAY;
                    break;
                case 5:
                    shape.color = Color.YELLOW;
                    break;
            }
        } else {
            shape.color = Color.BLACK;
        }
        shape.rt = rect;
        Log.e("ADD", shape + "");
        arShape.add(shape);
    }

    int FindShapeIdx(int x, int y) {
        int cool;
        Rect r = new Rect(x, y, x + SIZE, y + SIZE);
        for (cool = 0; cool < arShape.size(); cool++) {
            if (r.intersect(arShape.get(cool).rt) == true) {
                return cool;
            }
        }
        return -1;
    }

    Handler cHandler = new Handler() {
        public void handleMessage(Message ms) {
            AddNewShape();
            status = Play;
            invalidate();


        }

    };
}
class Shape {
    final static int RECT = 0;
    final static int CIRCLE = 1;
    final static int TRA = 2;

    int what;
    int color;
    Rect rt;


}



package com.coollime.coollime;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

public class SlidingSurface extends SurfaceView implements Callback {

	GameThread 	  mThread;				// GameThread
	SurfaceHolder mHolder;				// SurfaceHolder 
	static Context mContext;			// Context

	int Width, Height;
	int mgnLeft, mgnTop;
	int sMax;
	int xCnt, yCnt;
	int pWidth, pHeight;
	int sWidth, sHeight;
	int stageNum;
	int sliceNum[] = new int[36];
	int  moveCnt;

	Slice mSlice[] = new Slice[36];

	Rect mRect = new Rect();
	Bitmap imgOrg;

	public SlidingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        mHolder = holder;
        mContext = context;
        mThread = new GameThread(holder, context);

		xCnt=3;
		yCnt=3;
		stageNum=0;

		InitGame();
		MakeStage();

        setFocusable(true);
    }

	private void InitGame() {
		Display display = ((WindowManager) mContext.getSystemService (Context.WINDOW_SERVICE)).getDefaultDisplay();
		Width = display.getWidth();
		Height = display.getHeight() - 50;

		mgnLeft = 60;
		mgnTop = (int)(Height / 7.5);

		pWidth = Width - mgnLeft * 2;
		pHeight = pWidth * 7 / 5;		//photo 5:7

		mRect.set(mgnLeft, mgnTop, mgnLeft + pWidth, mgnTop + pHeight);

   	}

	private void MakeStage() {
		sMax = xCnt * yCnt;
		sWidth = pWidth / xCnt;
		sHeight = pHeight / yCnt;

		for (int i = 0; i < sMax; i++) {
			sliceNum[i] = i;
		}

		LoadImages();
		Shuffling();

		for (int i = 0; i < sMax; i++) {
			mSlice[i] = null;
			mSlice[i] = new Slice(sliceNum[i], i);
		}

        moveCnt = 0;

	}

	public void Shuffling() {
		int r, t;
		Random rnd = new Random();
		for (int i = 0; i < sMax; i++) {
			r = rnd.nextInt(sMax);
			t = sliceNum[i];
			sliceNum[i] = sliceNum[r];
			sliceNum[r] = t;
		} // for

		CheckShuffle();	//무결성 조사
	} // Shuffle


	public void CheckShuffle() {
		int k1, k2, cnt;
		do{
			k1 = k2 = cnt = 0;
			for (int i = 0; i < sMax - 1; i++) {

				if (sliceNum[i] == sMax - 1) continue;
				for (int j = i + 1; j < sMax; j++) {
					if (sliceNum[j] == sMax - 1) continue;
					if (sliceNum[i] > sliceNum[j]) {
						cnt++;
						k1 = i;
						k2 = j;
					}
				} // for j
			} // for i
			if (cnt % 2 == 0) break;

			int t = sliceNum[k1];
			sliceNum[k1] = sliceNum[k2];
			sliceNum[k2] = t;
		} while (true);
	} // Check

    public void MoveSlice(int x, int y) {
        synchronized (mHolder) {
            x = (x - mgnLeft) / sWidth;
            y = (y - mgnTop) / sHeight;

            int p  = y * xCnt + x;
            int pl = p - 1;
            int pr = p + 1;
            int pu = p - xCnt;
            int pd = p + xCnt;
            int last = sMax - 1;


            if (x - 1 >= 0 && sliceNum[pl] == last) {
                CheckSlice(p, pl);
            }
            else if (x + 1 < xCnt && sliceNum[pr] == last) {
                CheckSlice(p, pr);
            }
            else if (y - 1 >= 0 && sliceNum[pu] == last) {
                CheckSlice(p, pu);
            }
            else if (y + 1 < yCnt && sliceNum[pd] == last) {
                CheckSlice(p, pd);
            }
        } // sync
    }


    public void CheckSlice(int p1, int p2) {
        synchronized (mHolder) {
            moveCnt++;

            Bitmap tmp;

            tmp = mSlice[p1].imgPic;
            mSlice[p1].imgPic = mSlice[p2].imgPic;
            mSlice[p2].imgPic = tmp;

            int t = sliceNum[p1];
            sliceNum[p1] = sliceNum[p2];
            sliceNum[p2] = t;

            int n;
            for (n = 0; n < sMax; n++) {
                if (sliceNum[n] != n) break;
            }
			if (n>=sMax)
				Toast.makeText(mContext," : "+moveCnt, Toast.LENGTH_LONG).show();

		} // sync
    }

	public void LoadImages() {
		Resources res = mContext.getResources();
		int num = stageNum % 4;
    	Bitmap imgtmp = BitmapFactory.decodeResource(res, R.drawable.pic_0 + num);

		int w = imgtmp.getWidth();
		int h = imgtmp.getHeight();

		if (w * 1.4 < h)
			imgtmp = Bitmap.createBitmap(imgtmp, 0, 0, w, (int) (w * 1.4));
		else if (w * 1.4 > h) {
			int p = (int) (w - h / 1.4) / 2;
			imgtmp = Bitmap.createBitmap(imgtmp, p, 0, (int) (h / 1.4), h);
		}
		imgOrg = Bitmap.createScaledBitmap(imgtmp, pWidth, pHeight, true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mThread.RunningThread(true);
		mThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean done=true;
		mThread.RunningThread(false);
//		mThread.StopThread();
		while(done) {
			try {
				mThread.join();
				done=false;
			} catch (InterruptedException e) {

			}
		}
	}

	class GameThread extends Thread {
		boolean canRun = false;
		Paint strP=new Paint();
		Paint backP=new Paint();

		public GameThread(SurfaceHolder holder, Context context) {
			strP = new Paint();
			strP.setTextSize(75);

			backP = new Paint();

		}
		public void run() {
			Canvas canvas = null;
			while (canRun) {
				canvas = mHolder.lockCanvas();
				try {
					synchronized (mHolder){
						backP.setColor(Color.BLACK);
						canvas.drawRect(0,0,Width,Height,backP);

						strP.setColor(Color.WHITE);
						canvas.drawText("MOVE COUNT : "+moveCnt,10,75,strP);

						for (int i = 0; i < sMax; i++)
							canvas.drawBitmap(mSlice[i].imgPic, mSlice[i].x, mSlice[i].y, null);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				finally {
					if(canvas!=null)
						mHolder.unlockCanvasAndPost(canvas);
				}
			} // while
		} // run

		public void RunningThread(boolean b){
			canRun=b;
		}
	} // GameThread

	public class Slice {
		public int x, y;
		public Bitmap imgPic;

		private int w, h;
		public Slice(int sliceNum, int pos) {
			w = sWidth;
			h = sHeight;

			x = pos % xCnt * w + mgnLeft;
			y = pos / xCnt * h + mgnTop;

			int px = sliceNum % xCnt * w;
			int py = sliceNum / xCnt * h;

			imgPic = Bitmap.createBitmap(imgOrg, px, py, w, h);

			Paint paint = new Paint();

			Canvas canvas = new Canvas();
			canvas.setBitmap(imgPic);
			if (sliceNum == sMax - 1) {
				paint.setColor(Color.YELLOW);
				paint.setAlpha(160);
				canvas.drawRect(0, 0, w, h, paint);
			} else	{
				canvas.drawBitmap(imgPic, 0, 0, paint);
			}
		}
	}
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mHolder) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				if (mRect.contains(x, y)==true)
					MoveSlice(x, y);
			}
		}
		return true;
	} // Touch


	} // SurfaceView

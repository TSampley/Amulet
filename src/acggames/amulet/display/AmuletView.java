package acggames.amulet.display;

import acggames.amulet.AmuletActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AmuletView extends SurfaceView implements SurfaceHolder.Callback {

	private final SurfaceHolder holder;
	private static final Canvas bufferCanvas;
	private static Bitmap buffer;
	private static long interval;
	private static int frameRate;
	
	private final Thread engine, renderer;
	private boolean pause, renderWait, surfaceCreated;
	public static Point dim;
	public static Screen current;
	
	public AmuletView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		holder = getHolder();
		holder.addCallback(this);
		
		setFocusable(true); //makes this view the receiver of KeyEvents
		
		//Resources res = context.getResources();
		//arrow = BitmapFactory.decodeResource(res, R.drawable.arrow);
		//dot = res.getDrawable(R.drawable.dot);
		
		engine = new Thread() {
			public void run() {
				synchronized (engine) {
					long beforeTime, afterTime, sleepTime;
					try {
						for (;;) {
							beforeTime = System.currentTimeMillis();
							
							synchronized (bufferCanvas) {
								bufferCanvas.notify();
							}
							renderWait = false;
							
							current.step();
							afterTime = System.currentTimeMillis();
							if (pause)
								engine.wait();
							else {
								sleepTime = interval - afterTime + beforeTime;
								if (sleepTime > 0) {
									renderWait = true;
									Thread.sleep(sleepTime);
								}
							}
						}
					} catch (InterruptedException e) {
    					Log.v(AmuletActivity.D_MAIN, "engine Interupted");
					}
				}
			}
		};
		renderer = new Thread() {
			public void run() {
				synchronized (renderer) {
					try {
						Canvas c = null;
						for (;;) {
							if (renderWait)
								synchronized (bufferCanvas) {
									bufferCanvas.wait();
								}
							
							try {
								c = holder.lockCanvas(null);
								bufferCanvas.drawColor(0x10000000);
								current.drawCurrentFrame(bufferCanvas);
								c.drawBitmap(buffer, 0, 0, null);
							} finally {
								if (c!=null) {
									holder.unlockCanvasAndPost(c);
									c = null;
								}
							}
	
			    			if (pause)
			    				renderer.wait();
						}
					} catch (InterruptedException e) {
    					Log.v(AmuletActivity.D_MAIN, "renderer Interupted");
					}
				}
			}
		};
		pause = true;
		renderWait = false;
		surfaceCreated = false;
		setFrameRate(60);
		Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " view created.");
	}
	
	public static void swap(Screen s) {
		current.cleanup();
		s.setup();
		current = s;
	}
	
	public static void setFrameRate(int f) {
		interval = 1000 / (frameRate = f);
	}
	
	public static int getFrameRate() {
		return frameRate;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (current.touchEvent(event))
			return true;
		else
			return super.onTouchEvent(event);
	}
	
	public void onWindowFocusChanged(boolean windowHasFocus) {
		Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " windowHasFocus:"+windowHasFocus);
		if (surfaceCreated) {
			if (windowHasFocus)
				resumeThreads();
			else
				pauseThreads();
		}
		super.onWindowFocusChanged(windowHasFocus);
	}
	
	public static void setSize(int w, int h) {
		buffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		bufferCanvas.setBitmap(buffer);
		dim.x = w;
		dim.y = h;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// callback
		/*buffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bufferCanvas.setBitmap(buffer);
		dim.x = width;
		dim.y = height;*/
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " surface created.");
		surfaceCreated = true;
		resumeThreads();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " surface destroyed.");
		surfaceCreated = false;
		pauseThreads();
		// alright, Android OS, now you may continue
	}
	
	private synchronized void resumeThreads() {
		if (!pause)
			return;
		pause = false;
		if (engine.isAlive())
			synchronized (engine) {
				Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " notifying engine");
				engine.notify();
			}
		else {
			Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " starting engine.");
			engine.start();
		}
		
		if (renderer.isAlive())
			synchronized (renderer) {
				Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " notifying renderer.");
				renderer.notify();
			}
		else {
			Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " starting renderer.");
			renderer.start();
		}
	}
	
	private synchronized void pauseThreads() {
		if (pause)
			return;
		pause = true;
		Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " requesting threads wait");
		synchronized (engine) {
			Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " engine waiting");
		}
		synchronized (renderer) {
			Log.v(AmuletActivity.D_MAIN, Thread.currentThread()+ " renderer waiting");
		}
	}

	public static Bitmap getBitmap() {
		return buffer;
	}
	
	static {
		buffer = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
		bufferCanvas = new Canvas();
		dim = new Point();
		current = new Screen(){};
	}
}

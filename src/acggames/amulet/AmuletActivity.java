package acggames.amulet;

import java.io.FileOutputStream;

import acggames.amulet.display.AmuletView;
import acggames.amulet.display.MenuScreen;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class AmuletActivity extends Activity {
	public static final String D_MAIN = "main-debug",
					D_OTHER = "other-debug",
					FILE_NAME = "Amulet.save",
					PREF_NAME = "Amulet.pref";
	public static AmuletActivity singleton;
	private static SensorManager manager;
	private static Sensor accelerometer;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(D_MAIN, Thread.currentThread()+ " creating amulet");
        
        singleton = this;
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Display disp = 
			((WindowManager)AmuletActivity.singleton.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		AmuletView.setSize(disp.getWidth(), disp.getHeight());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        SoundManager.load(getApplicationContext());
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        						WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        AmuletView.swap(new MenuScreen());
    }
    
    @Override
    public void onSaveInstanceState(Bundle b) {
    	Log.v(D_MAIN, Thread.currentThread()+ " saving instance of amulet");
    	super.onSaveInstanceState(b);
    }
    
    @Override
    public void onPause() {
    	Log.v(D_MAIN, Thread.currentThread()+ " pausing amulet");
    	super.onPause();
    }
    
    @Override
    public void onStop() {
    	Log.v(D_MAIN, Thread.currentThread()+ " stopping amulet");
    	super.onStop();
    }
    
    @Override
    public void onDestroy() {
    	AmuletView.current.cleanup();
    	Log.v(D_MAIN, Thread.currentThread()+ " destroying amulet");
    	try {
    		FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
    		
    		fos.close();
    	} catch (Exception e) {
    		Log.e(D_MAIN, Thread.currentThread()+ " error");
    	}
    	SoundManager.release();
    	super.onDestroy();
    }
    
    @Override
    public void onResume() {
    	Log.v(D_MAIN, Thread.currentThread()+ " resuming amulet");
    	super.onResume();
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (AmuletView.current.keyDown(keyCode, event))
			return true;
		else
			return super.onKeyDown(keyCode, event);
	}
	
	public static void registerAccelerationListener(SensorEventListener sel) {
		manager.registerListener(sel, accelerometer, SensorManager.SENSOR_DELAY_GAME);
	}
	
	public static void unregisterAccelerationListener(SensorEventListener sel) {
		manager.unregisterListener(sel, accelerometer);
	}
}
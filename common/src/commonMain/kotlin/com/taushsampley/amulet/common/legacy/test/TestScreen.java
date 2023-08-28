package acggames.amulet.test;

import acggames.amulet.AmuletActivity;
import acggames.amulet.display.AmuletView;
import acggames.amulet.display.Screen;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.KeyEvent;

public class TestScreen extends Screen {
	private static final int W = 10;
	private static final Paint[] paints;
	private static final Paint white;
	private SensorEventListener sel;
	private float gx, gy;
	
	private Particle[] parts;
	public TestScreen() {
		parts = new Particle[100];
		for (int i = 0; i < parts.length; i++)
			parts[i] = new Particle();
		sel = new SensorEventListener(){
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
			@Override
			public void onSensorChanged(SensorEvent event) {
				gx = event.values[1]/64;
				gy = event.values[0]/64;
			}};
	}

	@Override
	public void setup() {
		AmuletActivity.registerAccelerationListener(sel);
	}

	@Override
	public void cleanup() {
		AmuletActivity.unregisterAccelerationListener(sel);
	}

	@Override
	public void drawCurrentFrame(Canvas c) {
		for (Particle p : parts)
			p.draw(c);
		float px = AmuletView.dim.x/2, py = AmuletView.dim.y/2;
		c.drawLine(px, py, px+gx*8000, py+gy*8000, white);
	}

	@Override
	public void step() {
		for (int i = 0; i < parts.length; i++) {
			for (int j = i+1; j < parts.length; j++)
				parts[i].interactWith(parts[j]);
			parts[i].step();
		}
	}
	
	public boolean keyDown(int keyCode, KeyEvent e) {
		return false;
	}
	
	private class Particle {
		double x, y, vx, vy;
		int p;
		public Particle() {
			x = AmuletView.dim.x*Math.random();
			y = AmuletView.dim.y*Math.random();
			p = (int)(6*Math.random());
		}
		
		public void interactWith(Particle p) {
			double dx = p.x - x, dy = p.y - y, d = .0005/Math.sqrt(dx*dx+dy*dy);
			vx+=dx*d;
			vy+=dy*d;
			p.vx-=dx*d;
			p.vy-=dy*d;
		}
		
		public void step() {
			x+=vx+=gx;
			y+=vy+=gy;
			if (x < W) {
				x=2*W-x;
				vx/=-4;
			} else if (x > AmuletView.dim.x-W) {
				x=2*(AmuletView.dim.x-W)-x;
				vx/=-4;
			}
			if (y < W) {
				y=2*W-y;
				vy/=-4;
			} else if (y > AmuletView.dim.y-W) {
				y=2*(AmuletView.dim.y-W)-y;
				vy/=-4;
			}
		}
		
		public void draw(Canvas c) {
			c.drawCircle((float)x, (float)y, W, paints[p]);
		}
	}
	
	static {
		paints = new Paint[6];
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setStrokeCap(Paint.Cap.ROUND);
		p.setARGB(255, 255, 0, 0);
		paints[0] = new Paint(p);
		p.setARGB(255, 255, 255, 0);
		paints[1] = new Paint(p);
		p.setARGB(255, 0, 255, 0);
		paints[2] = new Paint(p);
		p.setARGB(255, 0, 255, 255);
		paints[3] = new Paint(p);
		p.setARGB(255, 0, 0, 255);
		paints[4] = new Paint(p);
		p.setARGB(255, 255, 0, 255);
		paints[5] = new Paint(p);
		p.setARGB(255, 255, 255, 255);
		p.setStrokeWidth(2);
		white = new Paint(p);
	}
}

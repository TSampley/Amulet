package acggames.amulet.display;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class Screen {
	public void setup() {}
	public void cleanup() {}
	public void drawCurrentFrame(Canvas page) {}
	public void step() {}
	public boolean keyDown(int keyCode, KeyEvent e) {return false;}
	public boolean touchEvent(MotionEvent e) {return false;}
}

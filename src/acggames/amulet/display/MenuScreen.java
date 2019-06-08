
package acggames.amulet.display;

import acggames.amulet.AmuletActivity;
import acggames.amulet.Button;
import acggames.amulet.test.TestScreen;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class MenuScreen extends Screen {
	
	private final Button[] buttons;
	public static int count = 0;
	
	public MenuScreen() {
		buttons = new Button[] {
			new Button("buttons/new.png", AmuletView.dim.x/2, AmuletView.dim.y/8){
				public void act() {
					Log.v(AmuletActivity.D_OTHER, "NEW BUTTON");
				}
			},
			new Button("buttons/load.png", AmuletView.dim.x/2, AmuletView.dim.y*3/8){
				public void act() {
					Log.v(AmuletActivity.D_OTHER, "LOAD BUTTON");
				}
			},
			new Button("buttons/credits.png", AmuletView.dim.x/2, AmuletView.dim.y*5/8){
				public void act() {
					Log.v(AmuletActivity.D_OTHER, "CREDITS BUTTON");
				}
			},
			new Button("buttons/wb.png", AmuletView.dim.x/2, AmuletView.dim.y*7/8){
				public void act() {
					Log.v(AmuletActivity.D_OTHER, "WB BUTTON");
					// play music
					
				}
			}
		};
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawCurrentFrame(Canvas c) {
		for (Button b : buttons)
			b.draw(c);
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int keyCode, KeyEvent e) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AmuletView.swap(new TestScreen());
			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchEvent(MotionEvent e) {
		float x = e.getX(), y = e.getY();
		for (Button b : buttons)
			if (b.contains(x, y)) {
				b.act();
				return true;
			}
		return false;
	}
}

package acggames.amulet;

import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public abstract class Button {
    private transient Bitmap image;
    private transient int x, y, w, h;
    /**
     *
     * @param text
     * @param X
     * @param Y
     */
    public Button(String name, int X, int Y) {
        try {
        	AssetManager manager = AmuletActivity.singleton.getAssets();
        	InputStream is = manager.open(name);
            image = BitmapFactory.decodeStream(is);
            is.close();
            w = image.getWidth();
            h = image.getHeight();
            Log.v(AmuletActivity.D_OTHER, "image loaded " + w + " " + h);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(AmuletActivity.D_OTHER, "error");
        }
        x = X - w/2;
        y = Y - h/2;
        Log.v(AmuletActivity.D_OTHER, x + " " + y);
    }
    /**
     *
     * @param X
     * @param Y
     * @return true if the <code>Button</code> contains the point (X, Y).
     */
    public boolean contains(float X, float Y) {
        return X>x && Y>this.y && X<x+w && Y<y+h;
    }
    /**
     *
     * @param page
     */
    public void draw(Canvas page) {
    	//Log.v(AmuletActivity.D_OTHER, "Button being drawn");
    	page.drawBitmap(image, x, y, null);
    }
    /**
     *
     */
    public abstract void act();
}

